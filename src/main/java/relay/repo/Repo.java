package relay.repo;

import relay.domain.Server;

public interface Repo {

    void saveServer(Server s);

    String getAesKeyOf(String domain);

}
