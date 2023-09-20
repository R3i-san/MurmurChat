//package server.infra.parsing;
//
//import server.domain.Task;
//import server.domain.User;
//import server.domain.UserSocket;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mockito;
//import server.server.utils.utils.view.in.Executable;
//
//import java.net.Socket;
//import java.util.List;
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.params.provider.Arguments.arguments;
//
//class MessageExtractorTest {
//    private Executable executor;
//    private MessageExtractor extractor;
//    private ArgumentCaptor<Task> taskCaptor;
//
//    @BeforeEach
//    void setup() {
//        executor = Mockito.mock(Executable.class);
//        extractor = new MessageExtractor(executor);
//        taskCaptor = ArgumentCaptor.forClass(Task.class);
//    }
//
//    @ParameterizedTest
//    @MethodSource
//    void addFollowerToTag(Task task, Task expTask,String expTargetTrend, String expTargetDomain) {
//        // GIVEN
//
//        // WHEN
//        extractor.manageRequest(task);
//
//        // THEN
//        Mockito.verify(executor, Mockito.times(1))
//                .addFollowerToTag(taskCaptor.capture(), expTargetTrend, expTargetDomain);
//        assertEqualsTask(expTask, taskCaptor.getValue());
//    } static Stream<Arguments> addFollowerToTag() {
//        return Stream.of(
//                arguments(TASKS[12], TASKS[12], TRENDS[0], DOMAINS[0]),
//                arguments(TASKS[13], TASKS[13], TRENDS[1], DOMAINS[1]),
//                arguments(TASKS[14], TASKS[14], TRENDS[2], DOMAINS[2])
//        );
//    }
//
//    @ParameterizedTest
//    @MethodSource
//    void addRemoteFollowerToTag(Task task, Task expTask,String expTargetTrend, String expTargetDomain) {
//        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
//        extractor.manageRequest(task);
//        Mockito.verify(executor, Mockito.times(1))
//                .addRemoteFollowerToTag(expTask, expTargetTrend);
//    } static Stream<Arguments> addRemoteFollowerToTag() {
//        return Stream.of(
//                arguments(TASKS[21], new Task(IDS[0], USER_SOCKETS[0], FOLLOW_TREND_S[0]), TRENDS[0], DOMAINS[0]),
//                arguments(TASKS[22], new Task(IDS[1], USER_SOCKETS[1], FOLLOW_TREND_S[1]), TRENDS[1], DOMAINS[1]),
//                arguments(TASKS[23], new Task(IDS[2], USER_SOCKETS[1], FOLLOW_TREND_S[2]), TRENDS[2], DOMAINS[2])
//        );
//    }
//
//    private static final String[] DOMAINS = new String[] {
//            "server1.godswila.guru",
//            "server2.godswila.guru",
//            "server3.godswila.guru"
//    };
//
//    private static final String[] IDS = new String[] {
//            "00001@"+DOMAINS[0],
//            "00002@"+DOMAINS[0],
//            "00003@"+DOMAINS[1],
//            "00004@"+DOMAINS[1],
//            "00005@"+DOMAINS[2]
//    };
//
//    private static final String[] NAMES = new String[] {
//            "user1",
//            "user2",
//            "user3"
//    };
//
//    private static final String[] TRENDS = new String[] {
//            "#trend1",
//            "#trend2",
//            "#trend3"
//    };
//
//    private static final User[] USERS = {
//            new User(NAMES[0], DOMAINS[0]),
//            new User(NAMES[1], DOMAINS[0]),
//            new User(NAMES[2], DOMAINS[1]),
//    };
//
//    private static final Socket[] SOCKETS = {
//            new Socket(),
//            new Socket(),
//            new Socket(),
//    };
//
//    private static final UserSocket[] USER_SOCKETS = {
//            new UserSocket(USERS[0], SOCKETS[0]),
//            new UserSocket(USERS[1], SOCKETS[1]),
//            new UserSocket(USERS[2], SOCKETS[2])
//    };
//
//    private static final String[][] MSG_S = {
//            {"MSG hello",
//                    "hello"},
//            {"MSG aqwertyuiopasdfghjklzxcvbnm",
//                    "aqwertyuiopasdfghjklzxcvbnm"},
//            {"MSG fvew sg erdgc*£$(etg $evds(*y g%er()r £!)wfed",
//                    "fvew sg erdgc*£$(etg $evds(*y g%er()r £!)wfed"}
//    };
//
//    private static final String[][] MSG_TREND_S = {
//            {"MSG hello " + TRENDS[0],
//                    "hello " + TRENDS[0]},
//            {"MSG " + TRENDS[1] + " is my favourite hashTag",
//                    TRENDS[1] + " is my favourite hashTag"},
//            {"MSG what s up " + TRENDS[2] + " how are you",
//                    "what s up " + TRENDS[2] + " how are you"}
//    };
//
//    private static final String[][] MSG_TRENDS_S = {
//            {"MSG hello " + TRENDS[0] + " " + TRENDS[1] + " " + TRENDS[2],
//                    "hello " + TRENDS[0] + " " + TRENDS[1] + " " + TRENDS[2]},
//            {"MSG hello " + TRENDS[0] + " " + TRENDS[0] + " " + TRENDS[1],
//                    "hello " + TRENDS[0] + " " + TRENDS[0] + " " + TRENDS[1]},
//            {"MSG hello " + TRENDS[0] + " test" + TRENDS[1] + TRENDS[2],
//                    "hello " + TRENDS[0] + " test" + TRENDS[1] + TRENDS[2]},
//    };
//
//    private static final String[] FOLLOW_USER_S = {
//            "FOLLOW " + USERS[0],
//            "FOLLOW " + USERS[1],
//            "FOLLOW " + USERS[2]
//    };
//
//    private static final String[] FOLLOW_TREND_S = {
//            "FOLLOW " + TRENDS[0] + "@" + DOMAINS[0],
//            "FOLLOW " + TRENDS[1] + "@" + DOMAINS[1],
//            "FOLLOW " + TRENDS[2] + "@" + DOMAINS[2]
//    };
//
//    private static final String[] MSGS = {
//            "MSGS " + USERS[0] + " " + MSG_S[0][1],
//            "MSGS " + USERS[1] + " " + MSG_TREND_S[0][1],
//            "MSGS " + USERS[2] + " " + MSG_TRENDS_S[0][1]
//    };
//
//    private static final String[] SEND_MSGS_S = {
//            "SEND " + IDS[0] + " " + USERS[0] + " " + USERS[2] + " " + MSGS[0],
//            "SEND " + IDS[1] + " " + USERS[1] + " " + USERS[2] + " " + MSGS[1],
//            "SEND " + IDS[2] + " " + USERS[2] + " " + USERS[1] + " " + MSGS[2]
//    };
//
//    private static final String[] SEND_FOLLOW_USER_S = {
//            "SEND " + IDS[0] + " " + USERS[0] + " " + USERS[2] + " " + FOLLOW_USER_S[0],
//            "SEND " + IDS[1] + " " + USERS[1] + " " + USERS[2] + " " + FOLLOW_USER_S[1],
//            "SEND " + IDS[2] + " " + USERS[1] + " " + USERS[2] + " " + FOLLOW_USER_S[2]
//    };
//
//    private static final String[] SEND_FOLLOW_TREND_S = {
//            "SEND " + IDS[0] + " " + USERS[0] + " " + USERS[2] + " " + FOLLOW_TREND_S[0],
//            "SEND " + IDS[1] + " " + USERS[1] + " " + USERS[2] + " " + FOLLOW_TREND_S[1],
//            "SEND " + IDS[2] + " " + USERS[1] + " " + USERS[2] + " " + FOLLOW_TREND_S[2]
//    };
//
//    private static final Task[] TASKS = {
//            new Task(IDS[0],USER_SOCKETS[0], MSG_S[0][0]),
//            new Task(IDS[1],USER_SOCKETS[0], MSG_S[1][0]),
//            new Task(IDS[2],USER_SOCKETS[0], MSG_S[2][0]),
//            new Task(IDS[0],USER_SOCKETS[0], MSG_TREND_S[0][0]),
//            new Task(IDS[1],USER_SOCKETS[0], MSG_TREND_S[1][0]),
//            new Task(IDS[2],USER_SOCKETS[0], MSG_TREND_S[2][0]), // 5
//            new Task(IDS[0],USER_SOCKETS[0], MSG_TRENDS_S[0][0]),
//            new Task(IDS[1],USER_SOCKETS[0], MSG_TRENDS_S[1][0]),
//            new Task(IDS[2],USER_SOCKETS[0], MSG_TRENDS_S[2][0]),
//            new Task(IDS[0],USER_SOCKETS[1], FOLLOW_USER_S[0]),
//            new Task(IDS[1],USER_SOCKETS[2], FOLLOW_USER_S[1]), // 10
//            new Task(IDS[2],USER_SOCKETS[0], FOLLOW_USER_S[2]),
//            new Task(IDS[0],USER_SOCKETS[0], FOLLOW_TREND_S[0]),
//            new Task(IDS[1],USER_SOCKETS[0], FOLLOW_TREND_S[1]),
//            new Task(IDS[2],USER_SOCKETS[0], FOLLOW_TREND_S[2]),
//            new Task(IDS[0],USER_SOCKETS[0], SEND_MSGS_S[0]), // 15
//            new Task(IDS[1],USER_SOCKETS[1], SEND_MSGS_S[1]),
//            new Task(IDS[2],USER_SOCKETS[2], SEND_MSGS_S[2]),
//            new Task(IDS[0],USER_SOCKETS[0], SEND_FOLLOW_USER_S[0]),
//            new Task(IDS[1],USER_SOCKETS[1], SEND_FOLLOW_USER_S[1]),
//            new Task(IDS[2],USER_SOCKETS[1], SEND_FOLLOW_USER_S[2]), // 20
//            new Task(IDS[0],USER_SOCKETS[0], SEND_FOLLOW_TREND_S[0]),
//            new Task(IDS[1],USER_SOCKETS[1], SEND_FOLLOW_TREND_S[1]),
//            new Task(IDS[2],USER_SOCKETS[1], SEND_FOLLOW_TREND_S[2]),
//    };
//
//    private void assertEqualsTask(Task expected, Task actual) {
//        assertEquals(expected.getId(), actual.getId());
//        assertEquals(expected.getClient(), actual.getClient());
//        assertEquals(expected.getCommand(), actual.getCommand());
//        assertEquals(expected.getSource(), actual.getSource());
//    }
//}