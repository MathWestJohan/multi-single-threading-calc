package ntnu.no.SingleThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SingleThreadClientTest {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 5555;
    private static final int NUM_CLIENTS = 100; // Number of clients per operation
    private static final String[] OPERATIONS = {"10 5 A", "10 5 S", "10 5 M", "10 5 D"}; // Define operations to test

    public static void main(String[] args) {
        for (String operation : OPERATIONS) {
            for (int i = 0; i < NUM_CLIENTS; i++) {
                simulateClient(operation);
            }
        }
    }

    private static void simulateClient(String operation) {
        long startTime = System.currentTimeMillis();
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            writer.println(operation);

            String response = reader.readLine();
            System.out.println("Response from server: " + response);

        } catch (IOException e) {
            System.err.println("Error occurred while communicating with the server: " + e.getMessage());
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        long operationTime = endTime - startTime;
        System.out.println("Time taken for operation " + operation + ": " + operationTime + "ms");
    }
}



