package server.repo;

import server.repo.exceptions.RegisterException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import server.domain.DtoUser;
import server.domain.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class UserRepo {

    private Path filePath;

    List<DtoUser> users;

    public UserRepo(String path) {
        String prefix = Paths.get("").toAbsolutePath().normalize().toString();
        filePath = Path.of(prefix + path);
        users = load();
    }

    List<DtoUser> load(){

        List<DtoUser> items = null;

        Gson gson = new Gson();

        try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {

            items = gson.fromJson(br, new TypeToken<List<DtoUser>>(){}.getType());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }

    public void save(DtoUser u) throws RegisterException {

        if(users.contains(u)){
            throw new RegisterException(u.name + " already registered.");
        }

        createFile();

        Gson gson = new Gson();
        List<DtoUser> users = get();
        users.add(u);

        try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING)) {

            gson.toJson(users, bw);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getSalt(User user) {
        List<DtoUser> users = get();
        for (DtoUser u : users) {
            if (u.name.equals(user.getName())) {
                System.out.println(u.bcrypthash.substring(6, 29));
                return u.bcrypthash.substring(7, 29);
            }
        }

        return null;
    }

    public String getHash(User user) {
        List<DtoUser> users = get();
        for (DtoUser u : users) {
            if (u.name.equals(user.getName())) {
                System.out.println(u.bcrypthash.substring(6, 29));
                return u.bcrypthash;
            }
        }

        return null;
    }


    List<DtoUser> get(){ return this.users; }

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
