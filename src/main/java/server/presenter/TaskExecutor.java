package server.presenter;

import server.domain.*;
import server.infra.parsing.CommandType;
import server.infra.exceptions.GrammarException;
import server.infra.parsing.MessageParser;
import server.infra.parsing.Parser;
import server.repo.GRepo;
import server.utils.Config;
import server.view.in.ClientHandler;
import server.view.in.Executable;

import java.util.*;

public class TaskExecutor implements Runnable, Executable {
    private final QueueTask queueTask;
    private final Parser parser;
    private final Map<String, Set<String>> messageReceived;
    private final GRepo repo;
    private final ConnectedUsers users;
    private ClientHandler ch;

    public TaskExecutor(QueueTask queue, GRepo repo, ConnectedUsers users, ClientHandler ch) {
        this.queueTask = queue;
        this.parser = new MessageParser(this, queue);
        this.messageReceived = new HashMap<>();
        this.repo = repo;
        this.users = users;
        this.ch = ch;
    }

    public TaskExecutor(QueueTask queueTask, Parser parser, GRepo repo, ConnectedUsers users) {
        this.queueTask = queueTask;
        this.parser = parser;
        this.messageReceived = new HashMap<>();
        this.repo = repo;
        this.users = users;
    }

    @Override
    public void run() {
        boolean running = true;

        while (running) {
            // Récupérer la prochaine tâche de la queue (méthode bloquante)
            Task task = queueTask.getNext();
            // traitement
            try {
                if (task.getType().equals(CommandType.MSG)) {
                    parser.extractTags(task);

                } else if (task.getType().equals(CommandType.FOLLOW)) {
                    parser.follow(task);

                } else if ((task.getType().equals(CommandType.REGISTER))) {
                    parser.register(task);

                } else if ((task.getType().equals(CommandType.CONNECT))) {
                    parser.connect(task);

                } else if ((task.getType().equals(CommandType.CONFIRM))) {
                    parser.confirm(task);

                } else if ((task.getType().equals(CommandType.DISCONNECT))) {
                    parser.disconnect(task);
                }
                System.out.println("[TaskExecutor] Task processed : " + task.getCommand());
            } catch (GrammarException ge){
                System.out.println("[TaskExecutor] Task failed : " + task.getCommand());
                System.out.println(ge.getMessage());
            }
        }
    }

    @Override
    public void register(UserSocket socket, String username, String saltSize, String bcryptHash){
        ch.registerClient(socket, username, saltSize, bcryptHash);
    }

    @Override
    public void connect(UserSocket socket, String challenge) {
        ch.sendParam(socket, challenge);
    }

    @Override
    public void confirm(UserSocket socket, String username) {
        ch.confirmConnection(socket, username, null);
    }

    @Override
    public void disconnect(UserSocket socket) {
        ch.disconnectClient(socket);
    }

    @Override
    public void identifyDest(Task task, Set<String> trends, String msg){
        Set<User> followers = repo.getFollowersOf(task.getAsker());
        Set<User> toSend = new HashSet<>(followers);
        toSend.addAll(repo.getUsersFor(trends));

        //sending to user's followers
        for(User u : followers) {
            sendMsg(task, msg, u);
            toSend.remove(u);
        }

        //sending to followers of trends
        for (User follower : toSend) {
            sendMsg(task, msg, follower);
        }
    }

    @Override
    public void generateSend(Task task, String value, String domain, String command) {

       String send =  String.format("SEND %s %s %s@%s %s\r\n", task.getId(),
               task.getSrc(), value, domain, command);

       ch.sayToRelay(send);
    }

    @Override
    public void saveTrend(Task task, String trend, String domain, String user) {
        if (domain.equals(Config.DOMAIN_NAME)) {
            repo.saveLocalTrend(trend, users.getUser(task.getSrc()));
        } else {

            repo.saveRemoteTrend(trend, domain);
            generateSend(task, trend, domain, task.getCommand());
        }
        System.out.println("[EXECUTOR] " + user + " follow trend " + trend);
    }

    @Override
    public void saveFollow(Task task, String follower, String domain, String followed) {

        User sender = task.getSrc().getUser();

        if(follower.equals(sender.getName())){
            return;
        }

        if (domain.equals(Config.DOMAIN_NAME)) {
            repo.saveFollow(new User(follower), task.getSrc().getUser());
        } else {
            String send = String.format("SEND %s %s %s@%s %s \r\n", task.getId(), followed,
                    follower, domain, task.getCommand());
        }
        System.out.println("[EXECUTOR] "+ followed + "follow user " + follower);

    }

    private void sendMsg(Task task, String msg, User user) {
        UserSocket us = users.getUserSocket(user.getName());
        String msgs = String.format("MSGS %s %s\r\n", task.getSrc().toString(), msg);

        // client distant
        if(!user.getDomain().equals(Config.DOMAIN_NAME)){
            generateSend(task, user.getName(), user.getDomain(), msgs);
        // client connecté
        } else if (us != null) {
            ch.sayToClient(us, msgs);
        // client non connecté
        } else {
            repo.saveMessage(user.getName() ,msgs);
        }
    }

    private void sendMsgUserCo(Task task, User user, String message){

    }
}
