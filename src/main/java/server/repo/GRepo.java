package server.repo;

import server.domain.*;
import server.repo.exceptions.RegisterException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GRepo {

    void saveUser(DtoUser user) throws RegisterException;

    String getSalt(User user);

    String getHash(User user);

    List<DtoUser> getUsers();

    void saveFollow(User follower, User followed);
    void saveLocalTrend(String dtoTrend, User u);


    Map<User, Set<User>> getLocalFollows();
    Map<String, Set<User>> getLocalTrends();

    Map<User, Set<User>> getRemoteFollows();
    Map<String, Set<String>> getRemoteTrends();


    Set<User> getFollowersOf(User u);
    Set<User> getRemoteFollowersOf(User u);

    Set<User> getUsersFor(Set<String> trend);
    Set<String> getDomainsFor(Set<String> trend);


    void saveRemoteTrend(String trend, String domain);

    boolean containsRemoteTrend(String domain);

    void saveMessage(String destinataire, String message);

    List<String> getMessage(User user);

}
