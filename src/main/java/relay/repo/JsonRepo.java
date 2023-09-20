package relay.repo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import relay.domain.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

public class JsonRepo implements Repo {

    private Path filePath;

    private Set<Server> servers;

    public JsonRepo(String path){
        String prefix = Paths.get("").toAbsolutePath().normalize().toString();
        filePath = Path.of(prefix + path);
        servers = load();
    }

    @Override
    public String getAesKeyOf(String domain) {
        for (Server server : servers) {
            if (domain.equals(server.getDomain())) {
                return server.getAesKey();
            }
        }

        return null;
    }

    @Override
    public void saveServer(Server s) {

        try {
            createFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Set<Server> servers = load();
        servers.add(s);

        try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            gson.toJson(servers, bw);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private Set<Server> load() {

        HashSet<Server> users = null;
        Gson gson = new Gson();

        try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {

            users = gson.fromJson(br, new TypeToken<Set<Server>>(){}.getType());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return users == null ? new HashSet<>():users;
    }

    private void createFile() throws IOException{
        if(!Files.exists(filePath)){
            Files.createFile(filePath);
        }
    }

}
