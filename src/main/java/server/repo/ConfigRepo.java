package server.repo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import server.domain.DtoConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ConfigRepo {

    private Path filePath;

    public ConfigRepo(String path){
        String prefix = Paths.get("").toAbsolutePath().normalize().toString();
        filePath = Path.of(prefix + path);
    }

    public List<DtoConfig> getConfigs() {

        List<DtoConfig> config = new ArrayList<>();

        Gson gson = new Gson();

        try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {

            config = gson.fromJson(br, new TypeToken<List<DtoConfig>>(){}.getType());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;

    }

    private void createFile() throws IOException{
        if(!Files.exists(filePath)){
            Files.createFile(filePath);
        }
    }

}
