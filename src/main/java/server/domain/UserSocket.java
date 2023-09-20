package server.domain;

import server.utils.Config;

import java.net.Socket;

public class UserSocket {

    private User user;
    private Socket socket;

    public UserSocket(User u, Socket s){
        this.user = u;
        this.socket = s;
    }

    public User getUser(){ return this.user; }

    public String getName(){
        return this.user.getName();
    }


    public void setName(String username){
        this.user.setName(username);
    }

    public String getDomain(){
        return socket.getInetAddress().getHostName();
    }

    public Socket getSocket(){ return this.socket; }

    @Override
    public String toString(){
        return user.getName() + "@" + Config.DOMAIN_NAME;
    }
}
