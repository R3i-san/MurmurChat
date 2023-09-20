package server.repo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import server.domain.MessageSave;
import server.domain.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class MessageSaveRepo {

    private Path filePath;

    public MessageSaveRepo(){
        String prefix = Paths.get("").toAbsolutePath().normalize().toString();
        filePath = Path.of(prefix + "/src/main/resources/json/MessageSave/saveMessage.json");
    }

    public void saveMessage(String destinataire, String message) {

        List<MessageSave> listMessageSave;
        Gson gson = new Gson();

        createFile();

        listMessageSave = loads();

        if(listMessageSave == null){
            listMessageSave = new ArrayList<>();
        }

        listMessageSave.add(new MessageSave(destinataire, message));

        write(listMessageSave);
    }

    public List<String> getMessage(User destinataire) {
        List<MessageSave> listMessageSave;
        List<String> listMessage = new ArrayList<>();
        List<MessageSave> listRemoveMessage = new ArrayList<>();

        listMessageSave = loads();

        for(MessageSave message : listMessageSave){
            if(message.getDestinataire().equals(destinataire.getName())){
                listMessage.add(message.getMessage());
            }else{
                listRemoveMessage.add(message);
            }
        }

        write(listRemoveMessage);

        return listMessage;
    }

    private void createFile(){
        try {
            if(!Files.exists(filePath)){
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<MessageSave> loads(){
        List<MessageSave> listMessageSave = new ArrayList<>();
        Gson gson = new Gson();
        try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            listMessageSave = gson.fromJson(br, new TypeToken<List<MessageSave>>(){}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listMessageSave;
    }

    private void write(List<MessageSave> list){
        Gson gson = new Gson();
        try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8,
                StandardOpenOption.TRUNCATE_EXISTING)) {
            gson.toJson(list, bw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
