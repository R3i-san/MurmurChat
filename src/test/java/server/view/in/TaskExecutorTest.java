package server.view.in;

import server.domain.QueueTask;
import server.domain.Task;
import server.domain.User;
import server.domain.UserSocket;
import server.infra.parsing.Parser;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.presenter.TaskExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TaskExecutorTest {

    @Test
    public void testTaskExecutorOneTask() throws InterruptedException {
        Parser parser = Mockito.mock(Parser.class);
        BlockingQueue<Task> queue = new ArrayBlockingQueue<>(1);
        TaskExecutor taskExecutor = new TaskExecutor(new QueueTask(queue), parser, null, null);
        String cmd = "FOLLOW #tests@server1.godswila.guru";

        UserSocket userSocket = new UserSocket(new User(), null);
        Task task = new Task("1@s", userSocket, cmd);

        // WHEN
        Thread taskExecutorThread = new Thread(taskExecutor);
        taskExecutorThread.start();
        queue.put(task);

        // THEN
        // Attendre que la tâche soit traitée
        Thread.sleep(1000);
        // Vérifier que la méthode manageRequest() du parser a été appelée avec les bons arguments
        Mockito.verify(parser, Mockito.times(1)).parseTask(task);

        // Interrompre le thread du TaskExecutor
        taskExecutorThread.interrupt();
    }

//    @Test
//    public void testTaskExecutorWithException() throws InterruptedException, GrammarException {
//        // GIVEN
//        BlockingQueue<Task> queue = new ArrayBlockingQueue<>(1);
//        Parser parser = Mockito.mock(Parser.class);
//        String cmd = "FOLLOW #tests@server1.godswila.guru";
//        // Lancer une exception lors de l'appel de la méthode manageRequest() du parser
//        Mockito.doThrow(new GrammarException("Erreur de syntaxe")).when(parser).manageRequest(Mockito.any(Socket.class), Mockito.anyString());
//        TaskExecutor taskExecutor = new TaskExecutor(new QueueTask(queue), parser);
//        Socket client = new Socket();
//        Task task = new Task(client, cmd);
//        Thread taskExecutorThread = new Thread(taskExecutor);
//
//        // WHEN
//        taskExecutorThread.start();
//        queue.put(task);
//
//        // THEN
//        // Attendre que la tâche soit traitée
//        Thread.sleep(1000);
//
//        // Vérifier que la méthode manageRequest() du parser a été appelée avec les bons arguments
//        Mockito.verify(parser, Mockito.times(1)).manageRequest(client, cmd);
//
//        // Interrompre le thread du TaskExecutor
//        taskExecutorThread.interrupt();
//    }
//
//    @Test
//    public void testTaskExecutorManyTasks() throws InterruptedException {
//        // GIVEN
//        BlockingQueue<Task> queue = new ArrayBlockingQueue<>(5);
//        Parser parser = Mockito.mock(Parser.class);
//        String[] cmds = {
//                "FOLLOW #tests@server1.godswila.guru",
//                "MSG abcdef",
//                "CONFIRM 123456789",
//                "CONNECT 123456789",
//                "REGISTER 123456789",
//        };
//        Socket[] clients = {
//                new Socket(),
//                new Socket(),
//                new Socket(),
//        };
//        List<Task> tasks = List.of(
//                new Task(clients[0], cmds[0]),
//                new Task(clients[1], cmds[1]),
//                new Task(clients[2], cmds[2]),
//                new Task(clients[0], cmds[3]),
//                new Task(clients[1], cmds[4])
//        );
//        QueueTask queueTask = new QueueTask(queue);
//        for (Task task : tasks) {
//            queueTask.add(task);
//        }
//        TaskExecutor taskExecutor = new TaskExecutor(queueTask, parser);
//
//        // WHEN
//        Thread taskExecutorThread = new Thread(taskExecutor);
//        taskExecutorThread.start();
//        // Attendre que la tâche soit traitée
//        Thread.sleep(1000);
//
//        // THEN
//        for (Task task : tasks) {
//            try {
//                Mockito.verify(parser, Mockito.times(1)).manageRequest(task.getClient(), task.getCommande());
//            } catch (GrammarException ignored) {}
//        }
//
//        // Interrompre le thread du TaskExecutor
//        taskExecutorThread.interrupt();
//    }
//
//    @Test
//    public void testTaskExecutorConcurrent() throws InterruptedException {
//        // GIVEN
//        BlockingQueue<Task> queue = new ArrayBlockingQueue<>(5);
//        Parser parser = Mockito.mock(Parser.class);
//        String[] cmds = {
//                "FOLLOW #tests@server1.godswila.guru",
//                "MSG abcdef",
//                "CONFIRM 123456789",
//                "CONNECT 123456789",
//                "REGISTER 123456789",
//        };
//        Socket[] clients = {
//                new Socket(),
//                new Socket(),
//                new Socket(),
//        };
//        Task[] tasks = {
//                new Task(clients[0], cmds[0]),
//                new Task(clients[1], cmds[1]),
//                new Task(clients[2], cmds[2]),
//                new Task(clients[0], cmds[3]),
//                new Task(clients[1], cmds[4])
//        };
//        QueueTask queueTask = new QueueTask(queue);
//        // Thread Executor
//        TaskExecutor taskExecutor = new TaskExecutor(queueTask, parser);
//        Thread taskExecutorThread = new Thread(taskExecutor);
//        // Créer plusieurs threads qui ajoutent des tâches à la queue
//        Thread[] receivers = new Thread[5];
//        for (int i = 0; i < tasks.length; i++) {
//            int finalI = i;
//            receivers[i] = new Thread(() -> threadBody(tasks[finalI], queueTask));
//        }
//
//        // WHEN
//        receivers[0].start();
//        receivers[1].start();
//        Thread.sleep(300);
//        receivers[2].start();
//        taskExecutorThread.start();
//        receivers[3].start();
//        Thread.sleep(300);
//        receivers[4].start();
//
//        // Attendre que toutes les tâches soient traitées
//        Thread.sleep(1000);
//
//        // THEN
//        for (int i = 0; i < tasks.length; i++) {
//            try {
//                Mockito.verify(parser, Mockito.times(tasks.length)).manageRequest(Mockito.any(), Mockito.any());
//            } catch (GrammarException ignored) {}
//        }
//
//        // Interrompre les threads
//        taskExecutorThread.interrupt();
//        for (Thread receiver: receivers) {
//            receiver.interrupt();
//        }
//    }
//
//    private static void threadBody(Task task, QueueTask queue) {
//        try {
//            queue.add(task);
//        } catch (Exception e) {
//            fail(e);
//        }
//    }

//}
