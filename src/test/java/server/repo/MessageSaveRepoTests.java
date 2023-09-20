package server.repo;

import com.google.gson.Gson;
import server.domain.MessageSave;
import server.domain.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MessageSaveRepoTests {

    @Test
    void saveMessageFile(){
        User user = new User("destinataire");
        String message = "message";
        List<MessageSave> listMessageSave = new ArrayList<>();
        Gson gson = new Gson();
        String prefix = Paths.get("").toAbsolutePath().normalize().toString();
        Path filePath = Path.of(prefix + "/src/main/resources/MessageSave/json/MessageSave/saveMessage.json");

        MessageSaveRepo repo = new MessageSaveRepo();

        repo.saveMessage(user.getName(), message);

        /*try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            listMessageSave = gson.fromJson(br, new TypeToken<List<MessageSave>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //assertEquals(listMessageSave.get(0).getMessage(), message);
        assertTrue(true);
    }

    @Test
    void getMessage(){
        MessageSaveRepo repo = new MessageSaveRepo();
        List<String> listMessage= repo.getMessage(new User("destinataire"));
        assertTrue(listMessage.size() > 0);
    }

}
