package server.infra.parsing;

import server.domain.UserSocket;
import server.infra.exceptions.GrammarException;

public interface Filter {

    void parseRequest(UserSocket client, String request) throws GrammarException;

}
