package ntnu.no.ServerSide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread{
    private final Socket socket;
    private BufferedReader socketReader;
    private PrintWriter socketWriter;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        initializeStreams();
    }

    private boolean initializeStreams() {
        boolean success = true;
        try {
            this.socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.socketWriter = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println("Could not initialize streams");
            success = false;
        }
        return success;
    }
    public void run() {
        if (initializeStreams()) {
            System.out.println("Handling new client on " + Thread.currentThread().getName());
            String response = null;
            do {
                try {
                    response = socketReader.readLine();
                    if (response != null) {
                        System.out.println("Received message: " + response);

                        Double result = handleMessage(response);

                        if (result != null) {
                            sendMessage(result.toString());
                        } else {
                            sendMessage("Could not parse message correctly");
                        }
                    }
                } catch (IOException e) {
                    System.err.println("IOException " + e.getMessage());
                    break;
                }
            } while (response != null);
        }
    }

    private Double handleMessage(String message) {
        String[] tokens = message.split(" ");
        Double result;
        switch (tokens[2]) {
            case "A":
                result = Double.parseDouble(tokens[0]) + Double.parseDouble(tokens[1]);
                break;
            case "S":
                result = Double.parseDouble(tokens[0]) - Double.parseDouble(tokens[1]);
                break;
            case "M":
                result = Double.parseDouble(tokens[0]) * Double.parseDouble(tokens[1]);
                break;
            case "D":
                result = Double.parseDouble(tokens[0]) / Double.parseDouble(tokens[1]);
                break;
            default:
                // Could not parse message correctly
                System.out.println("Could not parse" + tokens[2]);
                result = null;
        }
        System.out.println("Result: " + result);
        return result;
    }

    public void sendMessage(String message) {
        socketWriter.println(message);
    }
}

