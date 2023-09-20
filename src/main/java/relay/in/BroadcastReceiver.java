package relay.in;

import relay.control.Config;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static relay.utils.AesTools.computeKey;


public class BroadcastReceiver implements Runnable {

    private DatagramSocket socket;
    private ServerHandler sh;
    private boolean running = true;

    public BroadcastReceiver(DatagramSocket socket, ServerHandler sh) {
        this.socket = socket;
        this.sh = sh;
    }

    private void listenOnce() {
        DatagramPacket dp = new DatagramPacket(new byte[128], 128);
        try {
            socket.receive(dp);

            byte[] received = dp.getData();
            byte[] IV = Arrays.copyOfRange(received, 0, Config.GCM_IV_LENGTH);
            byte[] msg = Arrays.copyOfRange(received, Config.GCM_IV_LENGTH, dp.getLength());
            String rcv = decryptBroadCast(IV, msg);
            if(rcv != null){
                System.out.println(rcv);
                sh.connect(rcv);
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
            socket.close();
            running = false;
        }
    }

    @Override
    public void run() {

        System.out.println("[BROADCAST RECEIVER] started ");
        while (running) {
            listenOnce();
        }
        System.out.println("[BROADCAST RECEIVER] closed");
    }

    private String decryptBroadCast(byte[] IV, byte[] msg) {

        // Get Cipher Instance
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");


            // Create SecretKeySpec
            SecretKeySpec keySpec = new SecretKeySpec(computeKey(Config.PUBLIC_AES_KEY).getEncoded(), "AES");

            // Create GCMParameterSpec
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(Config.GCM_TAG_LENGTH * 8, IV);

            // Initialize Cipher for DECRYPT_MODE
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

            // Perform Decryption
            byte[] decryptedText = cipher.doFinal(msg);

            return new String(decryptedText);

        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | InvalidKeyException
                | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            System.out.println("[BROADCAST RECEIVER] Unknown server");
        }

        return null;
    }

}
