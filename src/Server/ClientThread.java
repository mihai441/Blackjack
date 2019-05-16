import Server.ITable;
import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.Socket;




class clientThread extends Thread {
    private String clientName = null;
    private DataInputStream in;
    private PrintWriter out;
    private Socket clientSocket = null;
    private final clientThread[] threads;
    private int currentClientsConnected;
    private ITable serverprotocol;

    public clientThread(Socket clientSocket, clientThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        currentClientsConnected = threads.length;
    }

    public void run() {
    //stuff 
    }
}