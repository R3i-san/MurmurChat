package server.domain;

import server.utils.Config;

import java.util.Objects;

public class User {

    private String name;
    private String domain;

    public User(){
        this("unauthenticated");
    }

    public User(String username){
        this(username, Config.DOMAIN_NAME);
    }

    public User(String username, String domain){
        this.name = username;
        this.domain = domain;
    }

    public String getName(){
        return this.name;
    }
    public String getDomain(){
        return this.domain;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public boolean equals(Object o){

        if(o == this){
            return true;
        }

        if(o instanceof User){
            return ((User) o).name.equals(this.name) && ((User) o).domain.equals(this.domain);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, domain);
    }

    @Override
    public String toString(){
        return this.name;
    }

}
