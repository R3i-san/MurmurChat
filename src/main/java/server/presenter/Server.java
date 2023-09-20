package server.presenter;

import server.domain.ReceiverFactory;
import server.domain.*;
import server.infra.parsing.JsonToHtml;
import server.repo.GRepo;
import server.repo.exceptions.RegisterException;
import server.utils.Config;
import server.view.in.ClientHandler;
import server.view.in.ClientReceiver;
import org.apache.commons.codec.digest.DigestUtils;
import server.view.in.RelayReceiver;
import server.view.out.Sender;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.*;
import java.util.*;

import static server.utils.AesTools.encrypt;
import static server.utils.AesTools.generateIV;

public class Server implements ClientHandler, RelayHandler {

    private ReceiverFactory rcvFact;
    private ConnectedUsers users;
    private RelaySocket relay;
    private Map<UserSocket, ClientReceiver> receivers;
    private Sender sender;
    private GRepo repo;

    public Server(GRepo repo, ConnectedUsers users) {

        this.receivers = Collections.synchronizedMap(new HashMap<>());
        this.repo = repo;

        this.users = users;
        this.sender = new Sender();

        System.out.println("Server initialized.");
    }

    public void setReceiverFactory(ReceiverFactory rcvFact) {
        this.rcvFact = rcvFact;
    }

    /**
     * Creates an unauthenticated user and a new receiver to listen to it
     * @param socket
     */
    private void initializeClient(UserSocket socket) {
        createReceiver(socket);
        users.add(socket);
    }

    /**
     * Creates a new receiver to listen a specific user represented by a socket
     * @param socket The user socket which to receiver listens onto
     */
    private void createReceiver(UserSocket socket) {
        ClientReceiver rcvr = rcvFact.popClientReceiver(socket, this);
        Thread relayThrd = new Thread(rcvr);
        rcvr.attachThread(relayThrd);
        relayThrd.start();
        receivers.put(socket, rcvr);
    }

    /**
     * initializes a user that has just been accepted, and a receiver that is linked to it
     * @param socket user's socket
     */
    @Override
    synchronized public void onAcceptNewClient(SSLSocket socket) {
        UserSocket us = new UserSocket(new User(), socket);
        initializeClient(us);
        sayToClient(us, "HELLO " + Config.DOMAIN_NAME + " " + receivers.get(us).getR22() + "\r\n");
    }

    @Override
    public void onAcceptNewRelay(Socket socket) {
        System.out.println("[receivers] " + receivers.size());

        if (relay == null) {
            this.relay = new RelaySocket("Relay", socket);

            RelayReceiver rcv = rcvFact.popRelayReceiver(relay, this);
            Thread relayThrd = new Thread(rcv);
            rcv.attachThread(relayThrd);
            relayThrd.start();
            //receivers.put(socket, rcv);
            System.out.println("[SERVER] relay accepted");
        }
    }

    @Override
    public void onAcceptWebRequest(SSLSocket s) {
        sender.say(new UserSocket(new User("web"), s), JsonToHtml.getHtml(repo));
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the receivers and removes a client of the map when it is disconnected
     * @param socket user's socket
     */
    @Override
    synchronized public void onClientClosed(UserSocket socket) {
        sayToClient(socket, "+OK gerrara here\r\n");
        try {
            receivers.get(socket).stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
        receivers.remove(socket);
        users.remove(socket);
    }


    synchronized public void registerClient(UserSocket socket, String username, String saltSize, String bcryptHash) {
        try {
            repo.saveUser(new DtoUser(username, bcryptHash));
            sayToClient(socket, "+OK request accepted. You are now registered and connected as " + username + "\r\n");
            socket.setName(username);
            System.out.println("New client registered : " + username);
        } catch (RegisterException e) {
            sayToClient(socket, "-ERR " + e.getMessage());
        }

    }

    @Override
    synchronized public void sendParam(UserSocket socket, String username) {
        System.out.println("Sending PARAM");
        User u = users.getUser(socket);
        u.setName(username);
        sayToClient(socket, "PARAM " + Config.SALT_SIZE + " " + repo.getSalt(u));
    }

    @Override
    synchronized public void confirmConnection(UserSocket socket, String clientChallenge, QueueTask queue) {

        User u = users.getUser(socket);
        String random22 = receivers.get(socket).getR22();
        String challenge = DigestUtils.sha3_256Hex(random22 + repo.getHash(u));

        if (challenge.equals(clientChallenge)) {
            sayToClient(socket, "+OK you are now connected\r\n");
            List<String> messages = repo.getMessage(socket.getUser());
            for (String message : messages) {
                this.sender.say(socket, message);
            }
        } else {
            sayToClient(socket, "-ERR connection refused\r\n");
        }
    }

    @Override
    public void disconnectClient(UserSocket socket) {
        onClientClosed(socket);
    }

    @Override
    public void sayToClient(UserSocket to, String msg) {
        sender.say(to, msg);
    }

    @Override
    public void sayToRelay(String msg) {
        //sender.say(relay.getSocket(), encrypt(msg, generateIV(), Config.AES_KEY));

        if (relay != null) {
            System.out.println("[SERVER] Sending ro relay");
            sender.say(relay, encrypt(msg, generateIV(), Config.AES_KEY));
        } else {
            System.out.println("[SERVER] Relay no longer enable");
        }
    }
}
