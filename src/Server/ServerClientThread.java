package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import main.Pack;

class ServerClientThread extends Thread {

    private Socket serverClient;
    private int clientNo;
    private int clientId;
    private AtomicBoolean newPackage;
    private volatile String sendMessage;
    private volatile String receivedMessage;
    private ArrayList<Pack> playersPack;
    private AtomicBoolean cardsChanged;

    ServerClientThread(Socket inSocket, int counter) {
        serverClient = inSocket;
        clientNo = counter;
        newPackage.set(true);
    }

    @Override
    public void run() {
        try {
            DataOutputStream outStream;
            try (DataInputStream inStream = new DataInputStream(serverClient.getInputStream())) {
                outStream = new DataOutputStream(serverClient.getOutputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);
                
                if (cardsChanged.compareAndSet(true, false)) {
                    objectOutputStream.writeObject(playersPack);
                    objectOutputStream.flush();
                }
                if (newPackage.compareAndSet(true, false)) {
                    outStream.writeChars(sendMessage);
                    outStream.flush();
                    
                    receivedMessage = inStream.readUTF();
                    
                }
            }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }

    public String getMessage(){
        return receivedMessage;
    }
    
    public void setMessage(String message){
        sendMessage = message;
        newPackage.set(true);
    }

    void setCards(ArrayList<Pack> playersPack) {
        this.playersPack = playersPack;
        cardsChanged.set(true);
    }
    
    }
