package server.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DtoFollow {

    private Map<User, List<User>> follows;

    public DtoFollow(){
        this.follows = new HashMap<>();
    }

}
