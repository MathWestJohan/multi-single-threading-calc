package ntnu.no.ServerSide;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer extends Thread {

    public static final int PORT = 10025;

    public MultiThreadServer() {
        // Empty constructor
    }
    public void run() {
        startServer(PORT);
    }

    public void startServer(int port) {
        ServerSocket listeningSocket = openListeningSocket(port);
        System.out.println("Server listening on port " + port);

        if (listeningSocket != null) {
            while (true) {
                Socket clientSocket = acceptNextClientConnection(listeningSocket);
                if (clientSocket != null) {
                    System.out.println("New client connected from " + clientSocket.getRemoteSocketAddress());

                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    clientHandler.start();
                }
            }
        }
    }

    private ServerSocket openListeningSocket(int port) {
        ServerSocket listeningSocket = null;
        try {
            listeningSocket = new ServerSocket(port);
        } catch (Exception e) {
            System.err.println("Could not listen on port " + port);
        }
        return listeningSocket;
    }
    private Socket acceptNextClientConnection(ServerSocket listeningSocket) {
        Socket clientSocket = null;
        try {
            clientSocket = listeningSocket.accept();
        } catch (Exception e) {
            System.err.println("Accept failed on port " + PORT);
        }
        return clientSocket;
    }
}
