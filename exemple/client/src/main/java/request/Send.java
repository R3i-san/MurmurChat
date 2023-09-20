package request;
import java.io.*;
import java.net.*;
import java.nio.charset.*;

public class Send {

    public static final String DESTINATION="192.168.133.26";
    public static final int PORT = 8000;
    public Send(String destination, int port) {
        System.out.println("Connexion à "+ destination
                + " sur le port " + port);

        try(Socket server = new Socket(destination,port)) {

            BufferedReader fromServer = new BufferedReader(
                    new InputStreamReader(server.getInputStream(),
                            Charset.forName("UTF-8")));

            PrintWriter toServer = new PrintWriter(new
                    OutputStreamWriter(server.getOutputStream(),
                    Charset.forName("UTF-8")), true);

            String ligne = readLine(fromServer);
            System.out.println("Information reçue: " +
                    (ligne==null?"":ligne));

        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    private String readLine(BufferedReader b) throws IOException
    { String line = b.readLine();
        if(line != null && line.length() > 2 &&
                line.startsWith("\uFEFF"))
            return line.substring("\uFEFF".length());
        return line;
    }

    public static void main(String[] args) {
        if(args.length == 0)
            new Send(DESTINATION, PORT);
        else
            new Send(args[0], Integer.parseInt(args[1]));
    }
}




