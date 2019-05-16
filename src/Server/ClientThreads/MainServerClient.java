package Server.ClientThreads;

import Common.MainServerUser;
import Common.Table;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainServerClient extends Thread{

    private final Socket serverClient;
    private ArrayList<Table> tables;
    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;
    private AtomicBoolean isUserConnected;
    private MainServerUser user;
   

    public MainServerClient(Socket inSocket,ObjectOutputStream outToClient, ObjectInputStream inFromClient ) {
        this.isUserConnected = new AtomicBoolean();
        this.tables = new ArrayList<>();
        serverClient = inSocket;
        isUserConnected.set(true);
        this.outToClient= outToClient;
        this.inFromClient = inFromClient;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("player"+user.getId());
    }

    public void UpdateTables(ArrayList<Table> tables){
        this.tables=tables;
        try {
            outToClient.writeObject(tables);
            outToClient.flush();
            outToClient.reset();
        } catch (IOException ex) {
            isUserConnected.set(false);
        }
    }
    
    public void updateInfo(){
        try {
            outToClient.writeObject(user);
            outToClient.flush();
            outToClient.reset();
        } catch (IOException ex) {
            isUserConnected.set(false);
        }
    }
    
    
    public boolean isUserConnected(){
        return isUserConnected.get();
    }

    /**
     * @return the user
     */
    public MainServerUser getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(MainServerUser user) {
        this.user = user;
    }
    
}
