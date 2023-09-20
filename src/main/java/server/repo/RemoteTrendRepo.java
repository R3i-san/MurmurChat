package server.repo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class RemoteTrendRepo {

    Path filePath;
    Map<String, Set<String>> trends;

    public RemoteTrendRepo(String path){
        String prefix = Paths.get("").toAbsolutePath().normalize().toString();
        filePath = Path.of(prefix + path);
        createFile();
        trends = load();
    }

    private Map<String, Set<String>> load() {
        Map<String, Set<String>> items = null;

        Gson gson = new Gson();

        try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {

            items = gson.fromJson(br, new TypeToken<Map<String, Set<String>>>(){}.getType());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return items == null ? new HashMap<>() : items;
    }

    public Map<String, Set<String>> get(){ return trends; }

    void save(String trend, String domain) {

        Gson gson = new Gson();
        Map<String, Set<String>> tempTrend = get();
        if(trends.containsKey(trend)){
            trends.get(trend).add(domain);
        } else {
            Set<String> s = new HashSet<>();
            s.add(domain);
            trends.put(trend, s);
        }

        try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            gson.toJson(tempTrend, bw);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
