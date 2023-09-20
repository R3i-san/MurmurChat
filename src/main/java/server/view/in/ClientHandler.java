package server.view.in;

import server.domain.QueueTask;
import server.domain.UserSocket;

import javax.net.ssl.SSLSocket;
import java.net.Socket;

public interface ClientHandler {

    void onAcceptNewClient(SSLSocket s);

    void onAcceptNewRelay(Socket s);

    void onAcceptWebRequest(SSLSocket s);

    void onClientClosed(UserSocket socket);

    void sayToClient(UserSocket to, String msg);

    void sayToRelay(String msg);

    void sendParam(UserSocket socket, String username);

    void registerClient(UserSocket socket, String username, String saltSize, String bcryptHash);

    void confirmConnection(UserSocket socket, String sha3Hex, QueueTask tasks);

    void disconnectClient(UserSocket socket);
}
