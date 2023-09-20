package relay.in;

import java.net.Socket;

public interface ServerHandler {

    String getAesKeyOf(String domain);

    void connect(String domain);
    void disconnect(Socket socket);
    void transfer(Socket to, String msg);

}
