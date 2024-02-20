package ntnu.no.ServerSide;

public class ServerRunner {

  public static void main(String[] args) {

    MultiThreadServer multithreadedServer = new MultiThreadServer();
    multithreadedServer.start();

    SingleThreadServer singlethreadedServer = new SingleThreadServer();
    singlethreadedServer.start();
  }


}

