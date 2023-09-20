package server.view.in;

import server.domain.RelaySocket;
import server.domain.User;
import server.domain.UserSocket;
import server.infra.exceptions.GrammarException;
import server.infra.parsing.Filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static server.utils.AesTools.decrypt;

public class RelayReceiver implements Runnable {
    private final RelaySocket socket;
    private BufferedReader fromRelay;
    private final Filter parser;
    private Thread thread;

    public RelayReceiver(RelaySocket socket, Filter parser) {
        this.socket = socket;
        this.parser = parser;
        try {
            fromRelay = new BufferedReader(
                    new InputStreamReader(socket.getSocket().getInputStream(),
                            StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private UserSocket adaptSocket(RelaySocket s){
        return new UserSocket(new User("Relay"), s.getSocket());
    }

    private void listenOnce() throws GrammarException, IOException {
        String line = "";
        line = fromRelay.readLine() /*== null ? "" : line*/;
        line = decrypt(line);
        parser.parseRequest(adaptSocket(socket), line);
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
                    running = false;
                    socket.getSocket().close();
                    thread.interrupt();
                } catch (IOException e) {
                    e.printStackTrace();
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

    public void stop(){
        this.thread.interrupt();
    }

}
