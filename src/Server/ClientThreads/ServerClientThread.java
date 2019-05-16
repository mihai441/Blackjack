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
    private boolean isAlive = false;


    public ServerClientThread(Socket inSocket, Player player, ObjectInputStream inFromplayer) {
        serverClient = inSocket;
        this.inFromPlayer = inFromplayer;
        this.player = player;
        this.isAlive = true;
        try {   
            this.outToPlayer = new ObjectOutputStream(serverClient.getOutputStream());
        } 
        catch (IOException ex) {
               Logger.getLogger(ServerClientThread.class.getName()).log(Level.SEVERE, null, ex);
               isAlive = false;
        }
        try{
            serverClient.setSoTimeout(Constants.ALLOCATED_TIME);
        }
        catch (SocketException ex) {
            Logger.getLogger(ServerClientThread.class.getName()).log(Level.SEVERE, null, ex);
            isAlive = false;
        }
    }

    @Override
    public void run() {
    }

    public String getMessage(){
        if(isAlive){
            String message = null;
            try {
                if(inFromPlayer == null){
                    inFromPlayer = new ObjectInputStream(serverClient.getInputStream());
                }
                message = (String) inFromPlayer.readUTF();
            } catch (SocketTimeoutException ex) {
                isAlive = false;
                return null;
            } catch (IOException ex) {
                Logger.getLogger(ServerClientThread.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println(ex.getMessage());
                isAlive = false;
            }
            return message;
        }
        else{
            return null;
        }
    }
    
    public synchronized void setMessage(String message){
        if(isAlive){
            try {
                outToPlayer.writeObject(message);
                outToPlayer.flush();
                outToPlayer.reset();
            } catch (IOException ex) {
                Logger.getLogger(ServerClientThread.class.getName()).log(Level.SEVERE, null, ex);
                isAlive = false;
            }
        }
    }

    public synchronized void setTable(TableStructure tableStructure) {
        if(isAlive){
            try {
                outToPlayer.writeObject(tableStructure);
                outToPlayer.flush();
                outToPlayer.reset();
            } catch (IOException ex) {
                Logger.getLogger(ServerClientThread.class.getName()).log(Level.SEVERE, null, ex);
                isAlive = false;
            }
        }
    }
    
    public boolean isConnectionAlive(){
        return isAlive;
    }
    
    public Player getPlayer(){
        return player;
    }
    
}
