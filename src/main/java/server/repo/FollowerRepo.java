package server.repo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import server.domain.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class FollowerRepo {

    Path filePath;
    private Map<User, Set<User>> follows;

    public FollowerRepo(String path){
        String prefix = Paths.get("").toAbsolutePath().normalize().toString();
        filePath = Path.of(prefix + path);
        createFile();
        follows = load();
    }

    public void save(User followed, User follower){

        createFile();

        Gson gson = new Gson();

        if(follows.containsKey(followed)){
            follows.get(followed).add(follower);
        } else {
            Set<User> users = new HashSet<>();
            users.add(follower);
            follows.put(followed, users);
        }

        try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            gson.toJson(follows, bw);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<User, Set<User>> load(){
        Map<String, Set<User>> items = null;
        Map<User, Set<User>> loading = new HashMap<>();
        Gson gson = new Gson();

        try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {

            items = gson.fromJson(br, new TypeToken<Map<String, Set<User>>>(){}.getType());

        } catch (IOException e) {
            e.printStackTrace();
        }
        items = items == null ? new HashMap<>() : items;
        for (String s : items.keySet()){
            loading.put(new User(s), items.get(s));
        }
        return loading;
    }

    Map<User, Set<User>> get(){ return new HashMap<>(this.follows); }

    private void createFile() {
        if(!Files.exists(filePath)){
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
