package ntnu.no.MultiThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class MultiThreadClientTest {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 6666;
    private static final int NUM_CLIENTS = 100; // Number of clients per operation
    private static final String[] OPERATIONS = {"10 5 A", "10 5 S", "10 5 M", "10 5 D"}; // Define operations to test
    private static final Map<String, Long> totalTimeMap = new ConcurrentHashMap<>();
    private static final Map<String, Integer> operationCountMap = new ConcurrentHashMap<>();
    private static final CountDownLatch latch = new CountDownLatch(NUM_CLIENTS * OPERATIONS.length);

    public static void main(String[] args) {
        // Initialize maps
        for (String operation : OPERATIONS) {
            totalTimeMap.put(operation, 0L);
            operationCountMap.put(operation, 0);
        }

        // Run clients for each operation
        for (String operation : OPERATIONS) {
            for (int i = 0; i < NUM_CLIENTS; i++) {
                new Thread(new ClientTask(operation)).start();
            }
        }

        try {
            latch.await(); // Wait for all clients to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Calculate and output total average for each operation
        for (String operation : OPERATIONS) {
            long totalTime = totalTimeMap.get(operation);
            int operationCount = operationCountMap.get(operation);
            if (operationCount > 0) {
                long totalAverage = totalTime / operationCount;
                System.out.println("Total average for operation " + operation + ": " + totalAverage + "ms");
            }
        }
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
            }
            long endTime = System.currentTimeMillis();
            long operationTime = endTime - startTime;

            // Update total time and count for the operation
            totalTimeMap.compute(operation, (k, v) -> v + operationTime);
            operationCountMap.merge(operation, 1, Integer::sum);

            System.out.println("Time taken for operation " + operation + ": " + operationTime + "ms");
            latch.countDown(); // Signal completion
        }
    }
}
