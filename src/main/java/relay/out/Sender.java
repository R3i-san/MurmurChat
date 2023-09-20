package relay.out;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Sender {

    private DatagramSocket socket;

    public Sender(){

    }

    public void say(Socket to, String msg) {

        try {
            PrintWriter toClient = new PrintWriter(
                    new OutputStreamWriter(to.getOutputStream(),
                            StandardCharsets.UTF_8), true);
            toClient.print(msg);
            toClient.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
