package ntnu.no.SingleThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SingleThreadClient {
    public static void main(String[] args) {
        long totalStartTime = System.nanoTime();

        try (Socket socket = new Socket("localhost", 5555)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Connected to server.");
            System.out.println("Automatically sending 10 requests to the server for each operation.");

            char[] operations = {'A', 'S', 'M', 'D'};
            String[] operationNames = {"Addition", "Subtraction", "Multiplication", "Division"};

            for (int opIndex = 0; opIndex < operations.length; opIndex++) {
                char operation = operations[opIndex];
                System.out.println("Operation: " + operationNames[opIndex]);
                for (int i = 1; i <= 10; i++) {
                    String request = i + " " + (i + 10) + " " + operation;
                    System.out.println("Sending request: " + request);

                    writer.println(request);

                    BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String result = serverReader.readLine();
                    System.out.println("Result from server: " + result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            long totalEndTime = System.nanoTime();
            long totalDuration = (totalEndTime - totalStartTime) / 1000000;
            System.out.println("Total time taken for all operations: " + totalDuration + "ms");
        }
    }
}