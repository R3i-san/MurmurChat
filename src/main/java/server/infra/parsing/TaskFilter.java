package server.infra.parsing;

import server.domain.QueueTask;
import server.domain.Task;
import server.domain.UserSocket;
import server.infra.exceptions.GrammarException;
import server.view.in.ClientHandler;

import java.util.regex.Matcher;

import static server.infra.parsing.Grammar.CHECKER;
import static server.infra.parsing.Grammar.*;

public class TaskFilter implements Filter {

    private final ClientHandler server;
    private final QueueTask asyncQueue;
    private final QueueTask syncQueue;

    public TaskFilter(ClientHandler server, QueueTask asyncQueue, QueueTask syncQueue) {
        this.server = server;
        this.asyncQueue = asyncQueue;
        this.syncQueue = syncQueue;
    }

    synchronized public void parseRequest(UserSocket client, String command) throws GrammarException {
        Matcher checker = CHECKER.toPattern().matcher(command);

        if (!checker.matches()) {
            throw new GrammarException("[Parser] Incorrect request : " + command);
        }

        switch (checker.group(1)) {
            case "CONNECT":
                connect(client, command);
                break;
            case "REGISTER":
                register(client, command);
                break;
            case "CONFIRM":
                confirm(client, command);
                break;
            case "FOLLOW":
                follow(client, command);
                break;
            case "MSG":
                msg(client, command);
                break;
            case "DISCONNECT":
                disconnect(client, command);
                break;
            case "SEND":
                send(client, command);
                break;
        }
    }

    private void connect(UserSocket asker, String command) throws GrammarException {
        Matcher matcher = CONNECT.toPattern().matcher(command);

        if (matcher.matches()) {
            /*String username = matcher.group(1);
            server.sendParam(client, username);*/
            syncQueue.add(new Task(CommandType.CONNECT, asker, null, command));
        } else {
            throw new GrammarException("[Parser] Incorrect request : " + command);
        }
    }

    private void confirm(UserSocket asker, String command) throws GrammarException {
        Matcher matcher = CONFIRM.toPattern().matcher(command);

        if (matcher.matches()) {
            /*String sha3Hex = matcher.group(1);
            server.confirmConnection(client, sha3Hex);*/
            syncQueue.add(new Task(CommandType.CONFIRM, asker, null, command));
        } else {
            throw new GrammarException("[Parser] Incorrect request : " + command);
        }
    }

    private void register(UserSocket asker, String command) throws GrammarException {
        Matcher matcher = REGISTER.toPattern().matcher(command);

        if (matcher.matches()) {
            /*String username = matcher.group(1);
            String saltSize = matcher.group(2);
            String bcryptHash = matcher.group(3);
            server.registerClient(client, username, saltSize, bcryptHash);*/
            syncQueue.add(new Task(CommandType.REGISTER, asker, null, command));
        } else {
            throw new GrammarException("[Parser] Incorrect request : " + command);
        }
    }

    private void disconnect(UserSocket asker, String command) throws GrammarException {
        Matcher matcher = DISCONNECT.toPattern().matcher(command);

        if (matcher.matches()) {
            //server.onClientClosed(client);
            syncQueue.add(new Task(CommandType.DISCONNECT, asker, null, command));
        } else {
            throw new GrammarException("[Parser] Incorrect request : " + command);
        }
    }

//  Asynchrone  --------------------------------------------------------------------------------------------------------
    private void follow(UserSocket asker, String command) throws GrammarException {
        Matcher matcher = FOLLOW.toPattern().matcher(command);

        if (matcher.matches()) {
            asyncQueue.add(new Task(CommandType.FOLLOW, asker, null, command));
        } else {
            throw new GrammarException("[Parser] Incorrect request : " + command);
        }
    }

    private void msg(UserSocket asker, String command) throws GrammarException {
        Matcher matcher = MSG.toPattern().matcher(command);

        if (matcher.matches()) {
            asyncQueue.add(new Task(CommandType.MSG, asker,  null, command));
        } else {
            throw new GrammarException("[Parser] Incorrect request : " + command);
        }

        //server.onNewRequest(askerSocket, message);
    }

    private void send(UserSocket asker, String command) throws GrammarException {
        Matcher matcher = SEND.toPattern().matcher(command);

        if (matcher.matches()) {

            String request = matcher.group(4);
            parseRequest(asker, request);

        } else {
            throw new GrammarException("[Parser] Incorrect request : " + command);
        }

    }
}
