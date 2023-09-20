package server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class ClientRunnable implements Runnable {
    private Socket monClient;
    private BufferedReader in;
    private PrintWriter out;
    private boolean isConnected = false;

    private ThreadedServer controler;

    public ClientRunnable(Socket client, ThreadedServer controler) {
        this.monClient = client;
        try {
            this.controler = controler;
            in = new BufferedReader(new InputStreamReader(client.getInputStream(), Charset.forName("UTF-8")));
            out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), Charset.forName("UTF-8")), true);
            isConnected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String ligne = in.readLine();
            while(isConnected && ligne != null) {
                System.out.printf("Ligne reçue : %s\r\n", ligne);

                ligne = in.readLine();
                controler.broadcastToAllClients(this,"Hello");
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
        //System.relay.relay.repo.out.printf("Message envoyé: %s\n", message);
    }
}