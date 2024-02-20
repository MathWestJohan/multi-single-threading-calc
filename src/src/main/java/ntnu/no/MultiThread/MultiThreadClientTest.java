package ntnu.no.MultiThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class MultiThreadClientTest {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 6666;
    private static final int NUM_CLIENTS = 10;
    private static final String[] OPERATIONS = {"10 5 A", "10 5 S", "10 5 M", "10 5 D"};
    private static final AtomicLong totalTime = new AtomicLong(0);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_CLIENTS);
        long totalStartTime = System.currentTimeMillis();

        for (String operation : OPERATIONS) {
            for (int i = 0; i < NUM_CLIENTS; i++) {
                executorService.execute(new ClientTask(operation));
            }
        }

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        long totalEndTime = System.currentTimeMillis();
        long totalDuration = totalEndTime - totalStartTime;
        System.out.println("Total time taken for all operations: " + totalDuration + "ms");
    }

    static class ClientTask implements Runnable {
        private final String operation;

        public ClientTask(String operation) {
            this.operation = operation;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                writer.println(operation);
                String response = reader.readLine();
                System.out.println("Response from server: " + response);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                long endTime = System.currentTimeMillis();
                long operationTime = endTime - startTime;
                totalTime.addAndGet(operationTime);
                System.out.println("Time taken for operation " + operation + ": " + operationTime + "ms");
            }
        }
    }
}
