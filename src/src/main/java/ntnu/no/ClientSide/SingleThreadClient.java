package ntnu.no.ClientSide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

public class SingleThreadClient {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5555;
    private static final String[] OPERATIONS = {"10 75 A", "10 56 S", "10 15 M", "10 47 D"};
    private static final AtomicLong totalTime = new AtomicLong(0);

    public static void main(String[] args) {
        final CountDownLatch latch = new CountDownLatch(4); // Initialize CountDownLatch for 4 threads

        for (int i = 0; i < 4; i++) {
            new Thread(new ClientTask(latch)).start();
        }

        try {
            latch.await(); // Wait for all threads to finish
            System.out.println("All client operations completed.");
            System.out.println("Total combined time taken for all operations: " + totalTime.get() + "ms");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread interrupted.");
        }
    }

    private static class ClientTask implements Runnable {
        private final CountDownLatch latch;

        public ClientTask(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void run() {
            try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                System.out.println("Connected to server by " + Thread.currentThread().getName());

                for (String operation : OPERATIONS) {
                    long operationStartTime = System.currentTimeMillis();

                    writer.println(operation);
                    System.out.println("Sent: " + operation + " by " + Thread.currentThread().getName());

                    String response = reader.readLine();
                    System.out.println("Response from server: " + response + " to " + Thread.currentThread().getName());

                    long operationEndTime = System.currentTimeMillis();
                    totalTime.addAndGet(operationEndTime - operationStartTime);
                }
            } catch (IOException e) {
                System.err.println("An error occurred in " + Thread.currentThread().getName() + ": " + e.getMessage());
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        }
    }
}
