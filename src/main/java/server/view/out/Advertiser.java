package server.view.out;

import server.utils.AesTools;
import server.utils.Config;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.*;
import java.security.*;
import java.util.*;

public class Advertiser implements Runnable {

    private static String echo = "ECHO " + Config.UNICAST_RELAY_PORT + " " + Config.DOMAIN_NAME;
    private DatagramSocket socket;
    private String ip;
    private int port;
    private NetworkInterface netInt;

    public Advertiser(String ip, int port, NetworkInterface netInt){
        this.ip = ip;
        this.port = port;
        this.netInt = netInt;
        try {
            setup();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void setup() throws SocketException {
        Enumeration<InetAddress> netIntAdrr = netInt.getInetAddresses();
        InetSocketAddress inetAddr= new InetSocketAddress(netIntAdrr.nextElement(), port);

        socket = new DatagramSocket(inetAddr);
        socket.setBroadcast(true);
    }

    @Override
    public void run() {

        while(true){
            try {
                broadcast(echo, InetAddress.getByName(Config.BROADCAST_ADDRESS));
                System.out.println(echo);
                Thread.sleep(Config.BROADCAST_WAIT_TIME);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcast(String broadcastMessage, InetAddress address) throws IOException {
        try {
            socket.send(generatePacket(broadcastMessage, address));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DatagramPacket generatePacket(String msg, InetAddress address) {
        byte[] IV = AesTools.generateIV();
        byte[] msgEnc = encryptBroadcast(msg, IV, Config.PUBLIC_AES_KEY);
        byte[] total = new byte[IV.length + msgEnc.length];

        System.arraycopy(IV, 0, total, 0, IV.length);
        System.arraycopy(msgEnc, 0, total, IV.length, msgEnc.length);

        return new DatagramPacket(total, total.length, address, Config.BROADCAST_PORT);
    }

    private byte[] encryptBroadcast(String msg, byte[] IV, String key){

        // Get Cipher Instance
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");

            // Create SecretKeySpec
            SecretKeySpec keySpec = new SecretKeySpec(AesTools.computeKey(key).getEncoded(), "AES");

            // Create GCMParameterSpec
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(Config.GCM_TAG_LENGTH * 8, IV);

            // Initialize Cipher for ENCRYPT_MODE
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

            // Perform Encryption
            return cipher.doFinal(msg.getBytes());

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
