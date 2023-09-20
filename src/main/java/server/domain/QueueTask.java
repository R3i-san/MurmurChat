package server.domain;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class QueueTask {
    private final BlockingQueue<Task> queue;


    public QueueTask() {
        this.queue = new LinkedBlockingQueue<>(1000);
    }

    public QueueTask(BlockingQueue<Task> queue) {
        this.queue = queue;
    }

    public void add(Task task) {
        try {
            boolean isAdded = queue.offer(task, 500, TimeUnit.MILLISECONDS);
            if (!isAdded) {
                System.out.println("[QueueTask] La tâche n'a pas pu être ajoutée à la queue dans le délai imparti car Collection pleine.");
            }
        } catch (InterruptedException e) {
            System.out.println("[QueueTask] L'ajout de la tâche a été interrompu.");
        }
    }

    public Task getNext() {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

}