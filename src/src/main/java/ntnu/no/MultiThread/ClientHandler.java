package ntnu.no.MultiThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String input;
            while ((input = reader.readLine()) != null) {
                String[] parts = input.split(" ");
                int num1 = Integer.parseInt(parts[0]);
                int num2 = Integer.parseInt(parts[1]);
                char operation = parts[2].charAt(0);
                double result = calculate(num1, num2, operation);
                writer.println(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double calculate(double num1, double num2, char operation) {
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
