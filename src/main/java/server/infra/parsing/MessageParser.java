package server.infra.parsing;

import server.domain.QueueTask;
import server.domain.Task;
import server.infra.exceptions.GrammarException;
import server.utils.Config;
import server.view.in.Executable;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;

import static server.infra.parsing.Grammar.*;

public class MessageParser implements Parser {

    private final Executable executor;

    private QueueTask queue;

    public MessageParser(Executable executor, QueueTask queue) {
        this.executor = executor;
        this.queue = queue;
    }

    /*public void parseTask(Task task) {

        this.task = task;
        Matcher checker = CHECKER_2.toPattern().matcher(task.getCommand());

        if (!checker.matches()) {
            System.out.println("[PARSER] Incorrect request : " + task.getCommand());
        }

        switch (checker.group(1)) {
            case "FOLLOW":
                follow(task, "");
                break;
            case "MSG":
                extractTags();
                break;
        }

    }*/

    public void follow(Task task) throws GrammarException {

        Matcher matcher = FOLLOW.toPattern().matcher(task.getCommand());

        check(matcher);

        String value = matcher.group(1);
        String domain = matcher.group(2);

        if(!domain.equals(Config.DOMAIN_NAME)){
            executor.generateSend(task, value, domain,task.getCommand());
        }

        if (matcher.group(1).charAt(0) == '#') { // follow trend
            executor.saveTrend(task, value, domain, task.getAsker().getName());
        } else { // follow user
            executor.saveFollow(task, value, domain, task.getAsker().getName());
        }
    }

    private void check(Matcher matcher) throws GrammarException{
        if(!matcher.matches()){
            throw new GrammarException("[PARSER] incorrect request");
        }
    }

    @Override
    public void register(Task t) throws GrammarException {
        String cmd = t.getCommand();
        Matcher matcher = REGISTER.toPattern().matcher(cmd);

        check(matcher);

        String username = matcher.group(1);
        String saltSize = matcher.group(2);
        String bcrypt = matcher.group(3);

        executor.register(t.getSrc(), username, saltSize, bcrypt);

    }

    @Override
    public void connect(Task t) throws GrammarException {
        String cmd = t.getCommand();
        Matcher matcher = CONNECT.toPattern().matcher(cmd);

        check(matcher);

        String challenge = matcher.group(1);

        executor.connect(t.getSrc(), challenge);
    }

    @Override
    public void confirm(Task t) throws GrammarException {
        String cmd = t.getCommand();
        Matcher matcher = CONFIRM.toPattern().matcher(cmd);

        check(matcher);

        String username = matcher.group(1);
        executor.confirm(t.getSrc(), username);
    }

    @Override
    public void disconnect(Task t) throws GrammarException{
        String cmd = t.getCommand();
        Matcher matcher = DISCONNECT.toPattern().matcher(cmd);

        check(matcher);

        executor.disconnect(t.getSrc());
    }

    @Override
    public void extractTags(Task task) {
        Matcher m = Grammar.MSG.toPattern().matcher(task.getCommand());
        if (!m.find()) {
            return;
        }

        String userMessage = m.group(1);
        Matcher matcher = TAG.toPattern().matcher(task.getCommand());

        Set<String> tags = new HashSet<>();

        while (matcher.find()) {
            tags.add(matcher.group());
        }

        executor.identifyDest(task, tags, userMessage);

    }
}
