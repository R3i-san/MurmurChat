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

public class TrendRepo {

    private Path filePath;
    private Map<String, Set<User>> trends;

    public TrendRepo(String path){
        String prefix = Paths.get("").toAbsolutePath().normalize().toString();
        filePath = Path.of(prefix + path);
        createFile();
        trends = load();
    }

    public void save(String t, User u) {

        Gson gson = new Gson();

        if(trends.containsKey(t)){
            trends.get(t).add(u);
        } else {
            Set<User> users = new HashSet<>();
            users.add(u);
            trends.put(t, users);
        }

        try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            gson.toJson(trends, bw);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Map<String, Set<User>> load(){
        Map<String, Set<User>> items = null;

        Gson gson = new Gson();

        try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {

            items = gson.fromJson(br, new TypeToken<Map<String, Set<User>>>(){}.getType());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return items == null ? new HashMap<>():items;
    }

    public Map<String, Set<User>> get(){ return new HashMap<>(trends); }

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
