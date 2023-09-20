package server.view.in;

import server.domain.UserSocket;
import server.infra.exceptions.GrammarException;
import server.infra.parsing.Filter;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ClientReceiver implements Runnable {

    private final UserSocket socket;
    private BufferedReader fromClient;
    //private final QueueTask queueTask;
    private final String r22;
    private final Filter parser;
    private Thread thread;

    public ClientReceiver(UserSocket socket, Filter parser, String r22) {
        this.socket = socket;
        this.parser = parser;
        try {
            fromClient = new BufferedReader(
                    new InputStreamReader(socket.getSocket().getInputStream(),
                            StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.r22 = r22;
    }

    public String getR22(){
        return this.r22;
    }

    private void listenOnce() throws GrammarException, IOException {

        String line = fromClient.readLine() /*== null ? "" : line*/;

        if(line == null){
            throw new IOException();
        }

        parser.parseRequest(socket, line);
        System.out.printf("[RECEIVER] %s\n", line);
    }

    @Override
    public void run() {
        System.out.println("[RECEIVER] started for " + socket);
        boolean running = true;
        while(!thread.isInterrupted() || !socket.getSocket().isClosed() || running == false){
            try {
                listenOnce();
            }
            catch(IOException ioe){
                try {
                    stop();
                } catch (IOException ioe2){
                    System.out.println(ioe2.getMessage());
                }
            } catch (GrammarException ge) {
                System.out.println(ge.getMessage());
            }
        }
        System.out.println("[RECEIVER] closed for " + socket);
    }

    public void attachThread(Thread t){
        this.thread = t;
    }

    public void stop() throws IOException {
        socket.getSocket().close();
        thread.interrupt();
    }

}
