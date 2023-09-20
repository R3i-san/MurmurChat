package server.domain;

import server.infra.parsing.CommandType;
import server.utils.Config;

public class Task {
    private int id = 1;
    private final CommandType type;
    private final String command;
    private final UserSocket source;
    private final UserSocket destination;

    public Task(CommandType type, UserSocket source, UserSocket destination, String command) {
        this.id++;
        this.type = type;
        this.source = source;
        this.destination = destination;
        this.command = command;
    }

    public String getId() {
        return id + "@" + Config.DOMAIN_NAME;
    }

    public CommandType getType(){
        return this.type;
    }

    public UserSocket getSrc(){
        return this.source;
    }

    public User getAsker(){
        return getSrc().getUser();
    }

    public UserSocket getDestination(){
        return this.source;
    }

    public String getCommand() {
        return command;
    }
}
