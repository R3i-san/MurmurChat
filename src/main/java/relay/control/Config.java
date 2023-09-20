package relay.control;

public class Config {

    public static final int UNICAST_PORT           = 23113;
    public static final int BROADCAST_PORT         = 23000;

    public static final String PUBLIC_AES_KEY = "g5KUyp12bE4LPGZJ7URYwspVUa2RMjuB4FudTnNe3ZY=";

    public static final int AES_KEY_SIZE           = 256;
    public static final int GCM_IV_LENGTH          = 12;
    public static final int GCM_TAG_LENGTH         = 16;

    public static final String BROADCAST_ADDRESS   = "224.1.1.255";
    public static final String REPO_PATH           = "/src/main/resources/servers.json";
}
