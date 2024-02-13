package ntnu.no;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5555);
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connected to server.");
            System.out.println("Enter operations in the format 'Number Number Operation' Operations: A S M D , type 'exit' to quit:");

            String input;
            while (!(input = reader.readLine()).equalsIgnoreCase("exit")) {
                writer.println(input);

                BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String result = serverReader.readLine();
                System.out.println("Result from server: " + result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}