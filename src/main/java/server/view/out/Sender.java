package server.view.out;
;
import server.domain.RelaySocket;
import server.domain.UserSocket;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Sender {

    synchronized public void say(UserSocket to, String msg){
        try {
            PrintWriter toClient = new PrintWriter(
                    new OutputStreamWriter(to.getSocket().getOutputStream(),
                            StandardCharsets.UTF_8), true);
            toClient.print(msg);
            toClient.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized public void say(RelaySocket to, String msg){
        try {
            PrintWriter toClient = new PrintWriter(
                    new OutputStreamWriter(to.getSocket().getOutputStream(),
                            StandardCharsets.UTF_8), true);
            toClient.print(msg);
            toClient.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}