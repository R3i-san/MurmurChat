package server.domain;

import java.net.Socket;

public class RelaySocket {

    private String name;
    private Socket socket;

    private boolean relay;

    public RelaySocket(String name, Socket s){
        this.name = name;
        this.socket = s;
    }

    public String getDomain(){
        return socket.getInetAddress().getHostName();
    }

    public Socket getSocket(){ return this.socket; }

    /*@Override
    public String toString(){
        return user.getName() + "@" + Config.DOMAIN_NAME;
    }*/
}
