package ntnu.no.ClientSide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicLong;

public class SingleThreadClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5555;
    private static final String[] OPERATIONS = {"10 756483 A", "10 564737 S", "10 157483 M", "10 474756 D"};
    private static final AtomicLong totalTime = new AtomicLong(0);
    private static final int NUM_OPERATIONS_PER_TYPE = 4;

    public static void main(String[] args) {
        long totalStartTime = System.currentTimeMillis();

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            System.out.println("Connected to server.");

            for (String operation : OPERATIONS) {
                for (int i = 0; i < NUM_OPERATIONS_PER_TYPE; i++) {
                    long operationStartTime = System.currentTimeMillis();

                    writer.println(operation);
                    System.out.println("Sent: " + operation);

                    String response = reader.readLine();
                    System.out.println("Response from server: " + response);

                    long operationEndTime = System.currentTimeMillis();
                    long operationDuration = operationEndTime - operationStartTime;
                    totalTime.addAndGet(operationDuration);
                    System.out.println("Time taken for operation: " + operationDuration + "ms");
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            long totalEndTime = System.currentTimeMillis();
            long totalDuration = totalEndTime - totalStartTime;
            System.out.println("Total time taken for all operations: " + totalDuration + "ms");
        }
    }
}
