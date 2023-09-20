//package server.infra.parsing;
//
//import in.ClientHandler;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//
//import java.net.Socket;
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.params.provider.Arguments.arguments;
//import static org.mockito.Mockito.*;
//
//class MessageParserTest {
//    private final Socket client = new Socket();
//
//    @ParameterizedTest
//    @MethodSource
//    void correct_register_request(String request, String username, String size, String bcrypt) {
//        // GIVEN
//        ClientHandler server = mock(ClientHandler.class);
//        MessageParser parser = new MessageParser(server, queueTask);
//
//        // WHEN
//        try {
//            parser.manageRequest(client, request);
//        } catch (GrammarException e) {
//            Assertions.fail();
//        }
//
//        // THEN
//        verify(server).registerClient(client, username, size, bcrypt);
//    } static Stream<Arguments> correct_register_request() {
//        return Stream.of(
//                arguments("REGISTER Laurent 22 $2b$14$1zf4Zw4i3kfdg0KJNtGtfOQuXgZoQu0oFUOweIrBJz/vFIOdmYTx2",
//                        "Laurent", "22", "$2b$14$1zf4Zw4i3kfdg0KJNtGtfOQuXgZoQu0oFUOweIrBJz/vFIOdmYTx2"),
//                arguments("REGISTER Nasser 22 $2b$14$cYT83FwYmoxxrcYYn5HJ4.itrlJG6m3VG3nI1uPSIjbQ0c/cRBYuO",
//                        "Nasser", "22", "$2b$14$cYT83FwYmoxxrcYYn5HJ4.itrlJG6m3VG3nI1uPSIjbQ0c/cRBYuO"),
//                arguments("REGISTER xxxxx 10 $2b$14$/iCx1NLxw95kHxO/ULbYFOH02LpT6NKTQYFjai3e4mzT4IOYjO2qm",
//                        "xxxxx", "10", "$2b$14$/iCx1NLxw95kHxO/ULbYFOH02LpT6NKTQYFjai3e4mzT4IOYjO2qm")
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource
//    void incorrect_register_request(String request) {
//        // GIVEN
//        ClientHandler server = mock(ClientHandler.class);
//        MessageParser parser = new MessageParser(server, queueTask);
//
//        // EXPECTED
//        Assertions.assertThrows(GrammarException.class, () -> parser.manageRequest(client, request));
//    } static Stream<Arguments> incorrect_register_request() {
//        return Stream.of(
//                arguments("REGISTER Laurent 22 ..."),
//                arguments("REGISTER Nasser  $2b$14$cYT83FwYmoxxrcYYn5HJ4.itrlJG6m3VG3nI1uPSIjbQ0c/cRBYuO"),
//                arguments("REGISTE xxxxx 10 $2b$14$/iCx1NLxw95kHxO/ULbYFOH02LpT6NKTQYFjai3e4mzT4IOYjO2qm"),
//                arguments("")
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource
//    void correct_connection_request(String request, String username) {
//        // GIVEN
//        ClientHandler server = mock(ClientHandler.class);
//        MessageParser parser = new MessageParser(server, queueTask);
//
//        // WHEN
//        try {
//            parser.manageRequest(client, request);
//        } catch (GrammarException e) {
//            Assertions.fail();
//        }
//
//        // THEN
//        verify(server).sendParam(client, username);
//    } static Stream<Arguments> correct_connection_request() {
//        return Stream.of(
//                arguments("CONNECT Nasser", "Nasser"),
//                arguments("CONNECT XXXXX", "XXXXX"),
//                arguments("CONNECT YYYYY\r\n", "YYYYY")
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource
//    void incorrect_connection_request(String request) {
//        // GIVEN
//        ClientHandler server = mock(ClientHandler.class);
//        MessageParser parser = new MessageParser(server, queueTask);
//
//        // EXPECTED
//        Assertions.assertThrows(GrammarException.class, () -> parser.manageRequest(client, request));
//    } static Stream<Arguments> incorrect_connection_request() {
//        return Stream.of(
//                arguments("CONNECTE Nasser"),
//                arguments("CONNECT XXXX"),
//                arguments("REGISTER XXXXX"),
//                arguments("")
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource
//    void correct_confirm_request(String request, String hash) {
//        // GIVEN
//        ClientHandler server = mock(ClientHandler.class);
//        MessageParser parser = new MessageParser(server, queueTask);
//
//        // WHEN
//        try {
//            parser.manageRequest(client, request);
//        } catch (GrammarException e) {
//            Assertions.fail();
//        }
//
//        // THEN
//        verify(server).confirmConnection(client, hash);
//    } static Stream<Arguments> correct_confirm_request() {
//        return Stream.of(
//                arguments("CONFIRM 55ca3ecdfb1e5f10ed76c37a47b03055a2518318a2319588cd6971866cdf4d15",
//                        "55ca3ecdfb1e5f10ed76c37a47b03055a2518318a2319588cd6971866cdf4d15"),
//                arguments("CONFIRM a3fd5203505419acc7fe170bbe8544167921b5b6249462513765c0b2f2292cd0",
//                        "a3fd5203505419acc7fe170bbe8544167921b5b6249462513765c0b2f2292cd0")
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource
//    void incorrect_confirm_request(String request) {
//        // GIVEN
//        ClientHandler server = mock(ClientHandler.class);
//        MessageParser parser = new MessageParser(server, queueTask);
//
//        // EXPECTED
//        Assertions.assertThrows(GrammarException.class, () -> parser.manageRequest(client, request));
//    } static Stream<Arguments> incorrect_confirm_request() {
//        return Stream.of(
//                arguments("CONFIRME 55ca3ecdfb1e5f10ed76c37a47b03055a2518318a2319588cd6971866cdf4d15"),
//                arguments("CONFIRM a3fd5203505419acc7fe170bbe854")
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource
//    void correct_follow_user_request(String request, String username, String server.domain) {
//        // GIVEN
//        ClientHandler server = mock(ClientHandler.class);
//        MessageParser parser = new MessageParser(server, queueTask);
//
//        // WHEN
//        try {
//            parser.manageRequest(client, request);
//        } catch (GrammarException e) {
//            Assertions.fail();
//        }
//
//        // THEN
//        verify(server).followUser(client, username, server.domain);
//    } static Stream<Arguments> correct_follow_user_request() {
//        return Stream.of(
//                arguments("FOLLOW tests@server1.godswila.guru",
//                        "tests", "server1.godswila.guru"),
//                arguments("FOLLOW username123@s2.sw",
//                        "username123", "s2.sw"),
//                arguments("FOLLOW username123@s2.sw\r\n",
//                        "username123", "s2.sw"),
//                arguments("FOLLOW 12345@server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.god.server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.god",
//                        "12345", "server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.god.server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.god"),
//                arguments("FOLLOW 12345tendanceABCDEFG@server1.godswila.guru",
//                        "12345tendanceABCDEFG", "server1.godswila.guru")
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource
//    void correct_follow_trend_request(String request, String trend, String server.domain) {
//        // GIVEN
//        ClientHandler server = mock(ClientHandler.class);
//        MessageParser parser = new MessageParser(server, queueTask);
//
//        // WHEN
//        try {
//            parser.manageRequest(client, request);
//        } catch (GrammarException e) {
//            Assertions.fail();
//        }
//
//        // THEN
//        verify(server).followTrend(client, trend, server.domain);
//    } static Stream<Arguments> correct_follow_trend_request() {
//        return Stream.of(
//                arguments("FOLLOW #tests@server1.godswila.guru",
//                        "#tests", "server1.godswila.guru"),
//                arguments("FOLLOW #tendance123@s2.sw",
//                        "#tendance123", "s2.sw"),
//                arguments("FOLLOW #tendance123@s2.sw\r\n",
//                        "#tendance123", "s2.sw"),
//                arguments("FOLLOW #12345@server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.god.server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.god",
//                        "#12345", "server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.god.server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.god"),
//                arguments("FOLLOW #12345tendanceABCDEFG@server1.godswila.guru",
//                        "#12345tendanceABCDEFG", "server1.godswila.guru")
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource
//    void incorrect_follow_request(String request) {
//        // GIVEN
//        ClientHandler server = mock(ClientHandler.class);
//        MessageParser parser = new MessageParser(server, queueTask);
//
//        // EXPECTED
//        Assertions.assertThrows(GrammarException.class, () -> parser.manageRequest(client, request));
//    } static Stream<Arguments> incorrect_follow_request() {
//        return Stream.of(
//                arguments("FOLLOWE #tests@server1.godswila.guru"),
//                arguments("FOLLOW#tendance123@s2.sw"),
//                arguments("FOLLOW #1234@server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.god.server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.godswila.guru.server1.god"),
//                arguments("FOLLOW #12345tendanceABCDEFGH@server1.godswila.guru"),
//                arguments(" FOLLOW #tendance123@s2.sw"),
//                arguments("FOLLOW nomUser@s.sw"),
//                arguments("FOLLOW #tendance123"),
//                arguments("FOLLOW ^%yyr@s2.sw"),
//                arguments("FOLLOW nomUser@s2!sw")
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource
//    void correct_msg_request(String request, String message) {
//        // GIVEN
//        ClientHandler server = mock(ClientHandler.class);
//        MessageParser parser = new MessageParser(server, queueTask);
//
//        // WHEN
//        try {
//            parser.manageRequest(client, request);
//        } catch (GrammarException e) {
//            Assertions.fail();
//        }
//
//        // THEN
//        //verify(server).sendMsg(client, message);
//    } static Stream<Arguments> correct_msg_request() {
//        return Stream.of(
//                arguments("MSG éèáà",
//                        "éèáà"),
//                arguments("MSG TEST DU MESSAGE",
//                        "TEST DU MESSAGE"),
//                arguments("MSG a",
//                        "a"),
//                arguments("MSG abc123!£$%^&*()-=",
//                        "abc123!£$%^&*()-="),
//                arguments("MSG TEST DU MESSAGE\r\n",
//                        "TEST DU MESSAGE")
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource
//    void incorrect_msg_request(String request) {
//        // GIVEN
//        ClientHandler server = mock(ClientHandler.class);
//        MessageParser parser = new MessageParser(server, queueTask);
//
//        // EXPECTED
//        Assertions.assertThrows(GrammarException.class, () -> parser.manageRequest(client, request));
//    } static Stream<Arguments> incorrect_msg_request() {
//        return Stream.of(
//                arguments("MSG "),
//                arguments("MSGTEST DU MESSAGE"),
//                arguments(""),
//                arguments("MSGS TEST DU MESSAGE"),
//                arguments(" MSG TEST DU MESSAGE")
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource
//    void correct_disconnect_request(String request) {
//        // GIVEN
//        ClientHandler server = mock(ClientHandler.class);
//        MessageParser parser = new MessageParser(server, queueTask);
//
//        // WHEN
//        try {
//            parser.manageRequest(client, request);
//        } catch (GrammarException e) {
//            Assertions.fail();
//        }
//
//        // THEN
//        verify(server).onClientClosed(client);
//    } static Stream<Arguments> correct_disconnect_request() {
//        return Stream.of(
//                arguments("DISCONNECT"),
//                arguments("DISCONNECT\r\n")
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource
//    void incorrect_disconnect_request(String request) {
//        // GIVEN
//        ClientHandler server = mock(ClientHandler.class);
//        MessageParser parser = new MessageParser(server, queueTask);
//
//        // EXPECTED
//        Assertions.assertThrows(GrammarException.class, () -> parser.manageRequest(client, request));
//    } static Stream<Arguments> incorrect_disconnect_request() {
//        return Stream.of(
//                arguments(" DISCONNECT"),
//                arguments("DISCONNECTE"),
//                arguments("")
//        );
//    }
//}