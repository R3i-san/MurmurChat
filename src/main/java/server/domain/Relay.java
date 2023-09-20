package server.domain;

import java.net.Socket;

public class Relay {

    private Socket socket;

    public Relay(Socket socket){
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}
