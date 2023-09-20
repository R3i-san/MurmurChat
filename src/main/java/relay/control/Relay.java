package relay.control;

import relay.repo.Repo;
import relay.domain.Server;
import relay.in.UnicastReceiver;
import relay.out.Sender;
import relay.domain.ConnectedServers;
import relay.in.ServerHandler;
import relay.utils.AesTools;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.regex.Matcher;

import static relay.utils.AesTools.encrypt;
import static relay.utils.Grammar.*;

public class Relay implements ServerHandler {

    Repo repo;
    ConnectedServers connectedServers;
    Sender sender;
    public Relay(Repo repo){

        this.repo = repo;
        this.connectedServers = new ConnectedServers();
        this.sender = new Sender();
    }

    @Override
    public String getAesKeyOf(String domain) {
        return repo.getAesKeyOf(domain);
    }

    @Override
    public void connect(String received) {
        
        String[] infos = received.split("\\s");
        int port = Integer.parseInt(infos[1]);
        String domain = infos[2];

        Server s = new Server(domain, port);

        try {
            Socket socket = new Socket(InetAddress.getByName(domain), port);
            if(!connectedServers.isConnected(s)) {
                UnicastReceiver rcv = new UnicastReceiver(socket, this);
                Thread t = new Thread(rcv);
                connectedServers.add(s, rcv);
                t.start();
                rcv.attachThread(t);
                System.out.println("[RELAY]" + domain + " accepted");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try {
            Socket socket = new Socket(relay.repo.domain, port);

            if(!connectedServers.isConnected(s)) {
                UnicastReceiver rcv = new UnicastReceiver(socket, this);
                Thread t = new Thread(rcv);
                connectedServers.add(s, rcv);
                t.start();
                rcv.attachThread(t);
                System.relay.repo.out.println("[RELAY]" + relay.repo.domain + " accepted");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void disconnect(Socket socket) {
     this.connectedServers.remove(new Server(socket.getInetAddress().getHostName(), 0));
    }

    public void transfer(Socket from, String msg){

        System.out.println(msg);

        Matcher domainMatcher = EXTRACT_DOMAIN.toPattern().matcher(msg);

        if (!domainMatcher.find()) {
            sender.say(from, encrypt("Unreachable destination",
                    AesTools.generateIV(), getAesKeyOf(from.getInetAddress().getHostName())));
            return;
        }

        String domain = domainMatcher.group(3);
        sender.say(connectedServers.get(domain).getSocket(), encrypt(msg, AesTools.generateIV(),
                getAesKeyOf(domain)));

        //sender.say(connectedServers.get(relay.repo.domain).getSocket(), encrypt(msg,
        //AesTools.generateIV(), getAesKeyOf(relay.repo.domain)));
    }
}
