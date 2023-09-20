import server.repo.GeneralRepo;
import server.repo.GRepo;
import server.utils.Config;
import server.domain.ReceiverFactory;
import server.presenter.Server;
import server.domain.ConnectedUsers;
import server.utils.ConfigChooser;
import server.utils.NetChooser;
import server.presenter.TaskExecutor;
import server.domain.QueueTask;
import server.view.in.ClientType;
import server.view.out.Advertiser;
import server.view.in.Accepter;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.security.*;
import java.security.cert.CertificateException;


public class ServerMain {

    private static final String[] protocole = new String[]{"TLSv1.3"};
    private static final String[] chiffrement = new String[]{"TLS_AES_256_GCM_SHA384"};

    public static void main(String[] args) throws IOException, KeyStoreException, NoSuchAlgorithmException,
            CertificateException, UnrecoverableKeyException, KeyManagementException {

        ConfigChooser cChooser = new ConfigChooser();
        NetChooser nChooser = new NetChooser();
        Config.NETWORK_INTERFACE = nChooser.getSelected();

        final int clientPort = Config.UNICAST_CLIENT_PORT;
        final int relayPort = Config.UNICAST_RELAY_PORT;
        System.out.println("Démarrage du serveur sur le port " + clientPort);

        SSLServerSocket webListening = initSSL(443);

        SSLServerSocket clientListening = initSSL(clientPort);

        ServerSocket relayListening = new ServerSocket();
        relayListening.bind(new InetSocketAddress(
                InetAddress.getByName(Config.DOMAIN_NAME), relayPort));

        /*ServerSocket webListening = new ServerSocket();
        webListening.bind(new InetSocketAddress(
                InetAddress.getByName(Config.DOMAIN_NAME), 80));

        ServerSocket clientListening = new ServerSocket();
        clientListening.bind(new InetSocketAddress(
                InetAddress.getByName(Config.DOMAIN_NAME), clientPort));*/

        ConnectedUsers users = new ConnectedUsers();
        QueueTask asyncTasks = new QueueTask();
        QueueTask syncTasks = new QueueTask();

        GRepo repo = new GeneralRepo();

        server.presenter.Server server = new server.presenter.Server(repo, users);
        Advertiser ad = new Advertiser(Config.BROADCAST_ADDRESS, Config.BROADCAST_PORT, nChooser.getSelected());

        TaskExecutor asyncExecutor = new TaskExecutor(asyncTasks, repo, users, server);
        TaskExecutor syncExecutor = new TaskExecutor(syncTasks, repo, users, server);

        ReceiverFactory factory = new ReceiverFactory(asyncTasks, syncTasks);
        server.setReceiverFactory(factory);

        Accepter webAccepter =  new Accepter(webListening, ClientType.WEB);
        webAccepter.setSubscriber(server);

        Accepter clientAccepter =  new Accepter(clientListening, ClientType.USER);
        clientAccepter.setSubscriber(server);

        Accepter relayAccepter = new Accepter(relayListening, ClientType.RELAY);
        relayAccepter.setSubscriber(server);

        (new Thread(webAccepter)).start();
        (new Thread(relayAccepter)).start();
        (new Thread(ad)).start();
        (new Thread(asyncExecutor)).start();
        (new Thread(syncExecutor)).start();
        (new Thread(clientAccepter)).start();
    }

    private static SSLServerSocket initSSL(int port) throws KeyStoreException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, KeyManagementException {
        char[] password = "labo2023".toCharArray();

        System.setProperty("javax.net.ssl.keyStore","MurmurCS\\src\\main\\resources\\star.godswila.guru.p12");
        System.setProperty("javax.net.ssl.keyStorePassword","labo2023");
        System.setProperty("javax.net.ssl.protocols", "TLSv1.3");

        KeyStore ks = KeyStore.getInstance("JKS");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");

        ks.load(new FileInputStream("src\\main\\resources\\star.godswila.guru.p12"), password);
        kmf.init(ks, password);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);
        SSLContext sc = SSLContext.getInstance("TLSv1.3");
        sc.init(kmf.getKeyManagers(),  tmf.getTrustManagers(), null);

        SSLServerSocketFactory SSLFactory = sc.getServerSocketFactory();

        SSLServerSocket listening = (SSLServerSocket) SSLFactory.createServerSocket(port);
        listening.setReceiveBufferSize((int) Math.pow(2, 32));
        listening.setEnabledProtocols(protocole);
        listening.setEnabledCipherSuites(chiffrement);

        return listening;

    }


}
