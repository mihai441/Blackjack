package Server;

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
import main.Pack;

class MainServerClient extends Thread {

    private Socket serverClient;
    private ArrayList<Table> tables;
    private AtomicBoolean modifiedTables;
   

    MainServerClient(Socket inSocket) {
        serverClient = inSocket;
        modifiedTables.set(true);
    }

    @Override
    public void run() {
        try {
           Thread.currentThread().setName("playerServer");
           while(true){
                            ObjectInputStream inFromClient = new ObjectInputStream(serverClient.getInputStream());
                            ObjectOutputStream outToClient = new ObjectOutputStream(serverClient.getOutputStream());
                            if (modifiedTables.compareAndSet(true, false)) {
                            outToClient.writeObject(tables);
                            outToClient.flush();
                              }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
           }
                        } catch (IOException e) {
                            System.err.println(e);
                        }
        
        }

    public void UpdateTables(ArrayList<Table> tables){
        this.tables=tables;
        modifiedTables.set(true);
    }
    
}
