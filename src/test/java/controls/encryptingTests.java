//package controls;
//
//import server.presenter.Server;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class encryptingTests {
//
//    @Test
//    public void encryptTest(){
//        Server server = new Server(null, null, null);
//        String message = "marche";
//        assertEquals(message, server.decrypt(server.encrypt(message)));
//    }
//
//    @Test
//    public void encryptLongMessageTest(){
//        Server server = new Server(null, null, null);
//        String message = "je suis un jolie message qui va atre chiffrer puis dechiffrer, trop bien";
//        assertEquals(message, server.decrypt(server.encrypt(message)));
//    }
//
//    @Test
//    public void encryptCharSpecialTest(){
//        Server server = new Server(null, null, null);
//        String message = "çéêï c'est bon pour vous";
//        assertEquals(message, server.decrypt(server.encrypt(message)));
//    }
//
//}
