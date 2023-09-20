package server;
import java.io.*;
import java.net.*;
import java.nio.charset.*;
public class Server {
    public static final int PORT = 58001;
    private boolean stop = false;
    private boolean isStarted = false;
    private boolean isConnected = false;
    public Server(int port) {
        ServerSocket ecoute = null;
        Socket client = null;
        try {
            ecoute = new ServerSocket(port);
            isStarted = true;
            System.out.println("Démarrage du serveur sur le port "+
                    port);
            while(!stop) {
                client = ecoute.accept();
                isConnected = true;
                BufferedReader fromClient = new BufferedReader(new
                        InputStreamReader(client.getInputStream(),
                        Charset.forName("UTF-8")));

                PrintWriter toClient = new PrintWriter(new
                        OutputStreamWriter(client.getOutputStream(),
                        Charset.forName("UTF-8")), true);

                String ligne = readLine(fromClient);

                System.out.println("Ligne reçue:" + ligne);

                toClient.print("Bonjour, tu vas bien ?\r\n");

                toClient.flush(); client.close(); isConnected=false;
            } // end of while
        } catch(IOException ex) { ex.printStackTrace();
        } finally { try {
            if(isConnected) client.close();
            if(isStarted) ecoute.close();
        } catch(IOException e) { }
        }
    }
    private String readLine(BufferedReader b) throws IOException{String line = b.readLine();
        if(line!=null && line.length() >2 && line.startsWith("\uFEFF"))return line.substring("\uFEFF".length());
        return line;
    }

    public static void main(String[] args) {
        if(args.length == 0) new Server(PORT);
        else new Server(Integer.parseInt(args[0]));
    }

}
