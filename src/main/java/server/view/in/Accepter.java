package server.view.in;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Accepter implements Runnable {

    private ServerSocket listener;
    private ClientHandler clientHandler;

    private ClientType type;

    public Accepter(ServerSocket listener, ClientType type){
        this.listener = listener;
        this.type = type;
        System.out.println("Accepter initialized.");
    }

    @Override
    public void run() {

        boolean running = true;
        Socket socket;
        while(running){
            try {
                socket = listener.accept();
                if(type == ClientType.RELAY) {
                    clientHandler.onAcceptNewRelay(socket);
                } else if(type == ClientType.USER) {
                    clientHandler.onAcceptNewClient((SSLSocket)socket);
                } else {
                    clientHandler.onAcceptWebRequest((SSLSocket)socket);
                }
            } catch (IOException ioe) {
                System.out.println("Client cannot be accepted");
            }
        }
    }

    public void setSubscriber (ClientHandler subscriber) {
        this.clientHandler = subscriber;
    }

}
