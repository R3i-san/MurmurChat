package relay.in;

import java.io.IOException;
import java.net.Socket;
import java.io.*;
import java.nio.charset.StandardCharsets;

import static relay.utils.AesTools.decrypt;

public class UnicastReceiver implements Runnable{

    private Socket socket;
    private ServerHandler sh;
    private BufferedReader fromClient;
    private Thread thread;

    private boolean running;

    public UnicastReceiver(Socket s, ServerHandler sh) {

        this.sh = sh;
        this.running = true;
        this.socket = s;
        try {
            fromClient = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(),
                            StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            
        }
    }

    public Socket getSocket(){
        return this.socket;
    }

    private void listenOnce(){
        try {
            String line = fromClient.readLine();
            sh.transfer(socket, decrypt(line, sh.getAesKeyOf(socket.getInetAddress().getHostName())));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            try {
                socket.close();
                stop();
                running = false;
            } catch (IOException ioe2) {
                ioe2.printStackTrace();
            }
        }
    }

    @Override
    public void run() {

        while(running){
            listenOnce();
        }
        System.out.printf("[UNICAST RECEIVER] closed for %s\n",
            socket.getInetAddress().getHostName());
        sh.disconnect(socket);
    }

    public void attachThread(Thread t){
        this.thread = t;
    }

    public void stop(){
        this.thread.interrupt();
    }

}
