package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreadedServer {
    private static final int DEFAULT_PORT = 12346;
    private List<ClientRunnable> clientList;

    public ThreadedServer(int port) {
        clientList = Collections.synchronizedList(new ArrayList<>());
        ServerSocket server;
        Socket client;
        try {
            server = new ServerSocket(port);
            while(true) {
                client = server.accept();
                ClientRunnable runnable = new ClientRunnable(client, this);
                clientList.add(runnable);
                (new Thread(runnable)).start();
            }

        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public void broadcastToAllClients(ClientRunnable cr, String message) {
        System.out.printf("[broadcastAll] Message envoyé : %s\n", message);
        for(ClientRunnable c : clientList) {
            if(c != cr){
                c.sendMessage(message);
            }
        }
    }

    public static void main(String[] args) {
        new ThreadedServer(DEFAULT_PORT);
    }
}