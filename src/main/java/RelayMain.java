import relay.repo.JsonRepo;
import relay.control.Config;
import relay.control.Relay;
import relay.utils.NetChooser;
import relay.in.BroadcastReceiver;

import java.io.IOException;
import java.net.*;

public class RelayMain {

    public static void main(String[] args) throws IOException {

        final int port = Config.BROADCAST_PORT;

        NetChooser chooser = new NetChooser();

        MulticastSocket socket = new MulticastSocket(port);
        socket.setInterface(chooser.getSelected().getInetAddresses().nextElement());
        socket.joinGroup(InetAddress.getByName(Config.BROADCAST_ADDRESS));

        Relay relay = new Relay(new JsonRepo(Config.REPO_PATH));

        BroadcastReceiver receiver = new BroadcastReceiver(socket, relay);

        (new Thread(receiver)).start();
    }
}
