package relay.domain;

import relay.in.UnicastReceiver;

import java.util.HashMap;

import java.util.Map;

public class ConnectedServers {

    private Map<Server, UnicastReceiver> servers;

    public ConnectedServers(){
        this.servers = new HashMap<>();
    }

    public UnicastReceiver get(String domain){
        return servers.get(new Server(domain, 0));
    }

    public boolean isConnected(Server s){
        return servers.containsKey(s);
    }

    public void add(Server s, UnicastReceiver r){
        this.servers.put(s, r);
        System.out.println("Clients connected : " + servers.size());
    }

    public void remove(Server s){
        this.servers.remove(s);
        System.out.println("Clients connected : " + servers.size());
    }
}
