package Server.ClientThreads;

import Common.Constants;
import Common.Player;
import Common.TableStructure;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerClientThread extends Thread {

    private Socket serverClient;
    private Player player;
    private ObjectInputStream inFromPlayer;
    private ObjectOutputStream outToPlayer;


    public ServerClientThread(Socket inSocket, Player player, ObjectInputStream inFromplayer) {
        serverClient = inSocket;
        this.inFromPlayer = inFromplayer;
        this.player = player;
        try {   
            outToPlayer = new ObjectOutputStream(serverClient.getOutputStream());
        } catch (IOException ex) {
               Logger.getLogger(ServerClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
        serverClient.setSoTimeout(Constants.ALLOCATED_TIME);
        } catch (SocketException ex) {
            Logger.getLogger(ServerClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
    }

    public String getMessage(){
        String message = null;
        try {
            if(inFromPlayer == null){
                inFromPlayer = new ObjectInputStream(serverClient.getInputStream());
            }
            message = (String) inFromPlayer.readUTF();
        } catch (SocketTimeoutException ex) {
            return null;
        } catch (IOException ex) {
            Logger.getLogger(ServerClientThread.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }
        return message;
    }
    
    public synchronized void setMessage(String message){
        try {
            outToPlayer.writeObject(message);
            outToPlayer.flush();
            outToPlayer.reset();
        } catch (IOException ex) {
            Logger.getLogger(ServerClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public synchronized void setTable(TableStructure tableStructure) {
        try {
            outToPlayer.writeObject(tableStructure);
            outToPlayer.flush();
            outToPlayer.reset();
        } catch (IOException ex) {
            Logger.getLogger(ServerClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public Player getPlayer(){
        return player;
    }
    
}
