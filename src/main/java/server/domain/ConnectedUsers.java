package server.domain;

import java.util.*;

public class ConnectedUsers {

    private Set<UserSocket> users;

    public ConnectedUsers(){
        this.users = Collections.synchronizedSet(new HashSet<>());
    }

    public void add(UserSocket s){
        this.users.add(s);
        System.out.println("[users] " + users.size());
    }

    public void remove(UserSocket s){
        this.users.remove(s);
    }

    public boolean isConnected(UserSocket us){
        return users.contains(us);
    }

    public User getUser(UserSocket s){

        Iterator<UserSocket> it = users.iterator();

        while (it.hasNext()){
            UserSocket u = it.next();
            if(u.getName().equals(s.getName())){
                return u.getUser();
            }

        }
        return null;
    }

    public UserSocket getUserSocket(String username) {

        Iterator<UserSocket> it = users.iterator();

        while(it.hasNext()){
            UserSocket us = it.next();
            if(us.getName().equals(username)){
                return us;
            }
        }
        return null;
    }
}
