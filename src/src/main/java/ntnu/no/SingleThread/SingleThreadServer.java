package ntnu.no.SingleThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5555)) {
            System.out.println("Serverstarted. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected:" + clientSocket);

                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String input;
            while ((input = reader.readLine()) != null) {
                String[] parts = input.split(" ");
                if (parts.length == 3) {
                    int num1 = Integer.parseInt(parts[0]);
                    int num2 = Integer.parseInt(parts[1]);
                    char operation = parts[2].charAt(0);
                    double result = calculate(num1, num2, operation);
                    writer.println(result);
                } else {
                    writer.println("Invalid input format. Please provide input in the format 'Number Number Operation'.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static double calculate(double num1, double num2, char operation) {
        double result = 0;
        switch (operation) {
            case 'A':
                result = num1 + num2;
                break;
            case 'S':
                result = num1 - num2;
                break;
            case 'M':
                result = num1 * num2;
                break;
            case 'D':
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    System.out.println("Cannot divide by zero");
                    result = Double.NaN;
                }
                break;
            default:
                System.out.println("Invalid operation");
        }
        return result;
    }
}
