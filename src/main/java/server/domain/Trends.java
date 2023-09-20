package server.domain;

import java.util.*;

public class Trends {

    private Map<String, List<User>> trends;


    public Trends(Map<String, List<User>> t)
    {
        this.trends = t;
    }

    public void add(User u, String s){
        if(trends.containsKey(s)){
            this.trends.get(s).add(u);
        } else {
            List<User> l = new ArrayList<>();
            l.add(u);
            this.trends.put(s, l);
        }
    }
    
}
