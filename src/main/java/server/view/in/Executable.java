package server.view.in;

import server.domain.Task;
import server.domain.UserSocket;

import java.util.Set;

public interface Executable {


    void register(UserSocket socket, String username, String saltSize, String bcryptHash);

    void connect(UserSocket socket, String challenge);
    void confirm (UserSocket socket, String username);

    void disconnect(UserSocket socket);

    void identifyDest(Task task, Set<String> tags, String userMessage);

    void generateSend(Task task, String value, String domain, String command);

    void saveTrend(Task task, String trend, String domain, String user);

    void saveFollow(Task task, String follower, String domain, String followed);
}
