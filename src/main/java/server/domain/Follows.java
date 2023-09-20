package server.domain;

import java.util.*;

public class Follows {

    private Map<User, List<User>> follows;

    public Follows(Map<User, List<User>> f){
        this.follows = Collections.synchronizedMap(f);
    }

    public void addFollower(User followed, User newFollower){

        if(follows.containsKey(followed)){

            List<User> followers = follows.get(followed);
            if(!followers.contains(newFollower)){
                followers.add(newFollower);
            }

        } else {
            List<User> l = new ArrayList<>();
            l.add(newFollower);
            follows.put(followed, l);
        }
    }


}
