package server.utils;

import java.net.NetworkInterface;

public class Config {

    public static NetworkInterface NETWORK_INTERFACE;

    public static int UNICAST_CLIENT_PORT;
    public static int UNICAST_RELAY_PORT;
    public static int BROADCAST_PORT;

    public static int SALT_SIZE;

    public static String BROADCAST_ADDRESS;
    public static String DOMAIN_NAME;

    public static String PUBLIC_AES_KEY;
    public static String AES_KEY ;

    public static String U_REPO_PATH;
    public static String F_REPO_PATH;
    public static String T_REPO_PATH;
    public static String RT_REPO_PATH;

    public static int BROADCAST_WAIT_TIME;
    public static int AES_KEY_SIZE;
    public static int GCM_IV_LENGTH;
    public static int GCM_TAG_LENGTH;
}
