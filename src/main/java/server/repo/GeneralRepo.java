package server.repo;

import server.domain.*;
import server.repo.exceptions.RegisterException;
import server.utils.Config;

import java.util.*;

public class GeneralRepo implements GRepo {

    private UserRepo uRepo;
    private FollowerRepo fRepo;
    private TrendRepo tRepo;
    private RemoteTrendRepo rtRepo;
    private MessageSaveRepo msRepo;

    public GeneralRepo(){
        this.uRepo = new UserRepo(Config.U_REPO_PATH);
        this.tRepo = new TrendRepo(Config.T_REPO_PATH);
        this.fRepo = new FollowerRepo(Config.F_REPO_PATH);
        this.rtRepo = new RemoteTrendRepo(Config.RT_REPO_PATH);
        this.msRepo = new MessageSaveRepo();
    }

    @Override
    public void saveUser(DtoUser user) throws RegisterException {
        uRepo.save(user);
    }

    @Override
    public String getSalt(User user) {
        return uRepo.getSalt(user);
    }

    @Override
    public String getHash(User user) {
        return uRepo.getHash(user);
    }

    @Override
    public List<DtoUser> getUsers() {
        return uRepo.get();
    }

    @Override
    public void saveFollow(User followed, User follower) { fRepo.save(followed, follower); }

    @Override
    public Map<User, Set<User>> getLocalFollows() {

        return fRepo.get();
    }

    @Override
    public Set<User> getFollowersOf(User u) {
        Set<User> users = getLocalFollows().get(u);
        return users == null ? new HashSet<>():users;
    }

    @Override
    public Set<User> getRemoteFollowersOf(User u) {
        Set<User> users = getRemoteFollows().get(u);
        return users == null ? new HashSet<>():users;
    }

    public void saveLocalTrend(String trend, User u) {
        tRepo.save(trend, u);

        //TODO : Rework this
        /*
        tRepo.saveTrend(dtoTrends, u);
        if(trends.containsKey(dtoTrends.toString())){
            this.trends.get(dtoTrends.toString()).add(u);
        } else {
            List<User> l = new ArrayList<>();
            l.add(u);
            this.trends.put(dtoTrends.toString(), l);
        }
        */

    }

    @Override
    public void saveRemoteTrend(String trend, String domain){ rtRepo.save(trend, domain);}

    @Override
    public Map<String, Set<User>> getLocalTrends() {
        return tRepo.get();
    }

    @Override
    public Map<User, Set<User>> getRemoteFollows() {
        return null;
    }

    @Override
    public Map<String, Set<String>> getRemoteTrends() {
        return rtRepo.get();
    }

    @Override
    public Set<User> getUsersFor(Set<String> trend) {

        Set<User> users = new HashSet<>();

        for (String s : trend) {
            users.addAll(getLocalTrends().get(s));
        }

        return users;
    }

    @Override
    public Set<String> getDomainsFor(Set<String> trend) {
        Set<String> users = new HashSet<>();

        for (String s : trend) {
            users.addAll(getRemoteTrends().get(s));
        }

        return users;
    }

    public boolean containsRemoteTrend(String trend) {
       return rtRepo.get().containsKey(trend);
    }

    @Override
    public void saveMessage(String destinataire, String message) {
        msRepo.saveMessage(destinataire, message);
    }

    @Override
    public List<String> getMessage(User user) {
        return msRepo.getMessage(user);
    }
}
