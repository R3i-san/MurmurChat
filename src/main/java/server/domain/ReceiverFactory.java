package server.domain;

import server.infra.parsing.TaskFilter;
import server.view.in.ClientHandler;
import server.view.in.ClientReceiver;
import server.view.in.RelayReceiver;

import java.util.Random;

public class ReceiverFactory {

    private final QueueTask asyncQueue;
    private final QueueTask syncQueue;

    public ReceiverFactory(QueueTask asyncQueue, QueueTask syncQueue){
        this.asyncQueue = asyncQueue;
        this.syncQueue = syncQueue;
    }

    private String generateR22(){
        Random r = new Random();
        char[] c = new char[22];
        for(int i = 0; i < 22; i++){
            c[i]= (char) (r.nextInt(0x7a - 0x21) + 0x21);
        }

        return new String(c);
    }

    public ClientReceiver popClientReceiver(UserSocket socket, ClientHandler ch){
        return new ClientReceiver(socket, new TaskFilter(ch, asyncQueue, syncQueue), generateR22());
    }

    public RelayReceiver popRelayReceiver(RelaySocket socket, ClientHandler ch){
        return new RelayReceiver(socket, new TaskFilter(ch, asyncQueue, syncQueue));
    }
}
