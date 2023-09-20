package server.utils;

public class ExecutorUtil {
    public static String generateMsgs(String userMessage, String sourceDomain) {
        return "MSGS" + " " + sourceDomain + " " + userMessage + "\r\n";
    }

    public static String generateSend(){
        return null;
    }

}
