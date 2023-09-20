package server.domain;

public class MessageSave {

    //private Task task;
    private String destinataire;
    private String message;

    public MessageSave(String user, String message){
        //this.task = task;
        this.destinataire = user;
        this.message = message;
    }

    //public Task getTask(){ return task; }

    public String getDestinataire(){ return destinataire; }

    public String getMessage() {
        return message;
    }

}
