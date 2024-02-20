package ntnu.no.ServerSide;

import java.io.*;
import java.net.*;

public class SingleThreadServer extends Thread {
    private int port = 5555;

    public SingleThreadServer() {
        // Empty constructor
    }
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Starting the server at port " + port);
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("Connected to a client");

                    BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    // Process 4 operations from the client
                    for (int i = 0; i < 4; i++) {
                        String clientMessage = socketReader.readLine();
                        if (clientMessage != null) {
                            String serverResponse = "Processed: " + clientMessage;
                            socketWriter.write(serverResponse);
                            socketWriter.newLine();
                            socketWriter.flush();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Server exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Server could not start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
