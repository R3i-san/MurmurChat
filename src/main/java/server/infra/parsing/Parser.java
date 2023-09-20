package server.infra.parsing;

import server.domain.Task;
import server.infra.exceptions.GrammarException;

public interface Parser {

    void register(Task t) throws GrammarException;

    void connect(Task t) throws GrammarException;

    void confirm(Task t) throws GrammarException;

    void extractTags(Task task) throws GrammarException;

    void follow(Task t) throws GrammarException;

    void disconnect(Task t) throws GrammarException;


}
