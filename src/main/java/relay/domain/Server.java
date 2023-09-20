package relay.domain;

import java.util.Objects;

public class Server {

    private String domain;
    private String aesKey;

    public Server(String domain, int port){
        this.domain = domain;
    }

    public String getDomain(){
        return this.domain;
    }
    public String getAesKey(){
        return this.aesKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return Objects.equals(domain, server.domain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain);
    }
}
