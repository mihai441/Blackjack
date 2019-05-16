/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.Player;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import Common.Table;


/**
 *
 * @author AsX
 */
public class Table2 implements Runnable {
    
    private final int ID = 1;
    private final String ADRESS = "localhost";
    private final int PORT=6002;
    private final boolean AVAILABLE = false;
    private final int nrMaxPlayers=5;
    private int numOfPlayers=0;
    private final int allocatedTime = 10000;
    private ArrayList<Player> players = new ArrayList<>();
    

    
    public void update() {
        boolean isRunning=false;
        boolean newRound=false;
        numOfPlayers=players.size();
        while (true) {
            
            if(!isRunning){
                if(numOfPlayers()>0){
                    isRunning=true;
                    newRound=true;
                }
            }
            if(isRunning){
                if(newRound){
                    asezareLaMasa();
                    dealcards(numOfPlayers);
                    newRound=false;
                    try{
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException e){}
              }
              checkInputs(numOfPlayers);
              showDealer();
              win();
            }
        }
    }


    public int numOfPlayers() {
        //verifica nr de jucatori
        return numOfPlayers;
    }


    public void win() {
    }

    public void checkInputs(int numOfPlayers){
        
        
        for(int i=0;i<numOfPlayers;i++){
            long timenow = System.nanoTime();
            long time=0;
            while(time<allocatedTime){
                //wait input from player i
                //send timeleft to to clients from table
                time+=System.nanoTime()-timenow;
                timenow=System.nanoTime();
            }
        }
        
    }
    

    public void showDealer(){
    }

     public void asezareLaMasa(){
     
     }
     
     public void dealcards(int numOfPlayers){
     }

    @Override
    public void run() {
                connectToMainServer();
                createTableServer();
            }
   
    
    Table returnTable(){
        return new Table(ID, numOfPlayers(), ADRESS,PORT,AVAILABLE);
    }
    
    void serverRequest(String request){

    }
    
    void connectToMainServer(){
        String server = "localhost";
                Socket s = null;
                try {
                    s = new Socket(server, 5679);
                } catch (IOException ex) {
                    Logger.getLogger(Table2.class.getName()).log(Level.SEVERE, null, ex);
                }
            try {
                ObjectOutputStream outToServer = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream inFromServer = new ObjectInputStream(s.getInputStream());
                            while (true) {
                String request = inFromServer.readUTF();
                switch (request) {
                    case "Table":
                        outToServer.writeObject(returnTable());
                        break;
                        //TODO
                }
                
                outToServer.flush();
                            }
                
            } catch (IOException ex) {
                Logger.getLogger(Table2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    
    void createTableServer(){
        
        try {
            ServerSocket server = new ServerSocket(PORT);
            while (true) {
               final Socket client = server.accept();
               new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.currentThread().setName("Table1");
                        ObjectInputStream inFromClient = new ObjectInputStream(client.getInputStream());
                        ObjectOutputStream outToClient = new ObjectOutputStream(client.getOutputStream());
                            try {
                                Player player = (Player) inFromClient.readObject();
                                if(!players.contains(player))
                                    if(numOfPlayers<nrMaxPlayers)
                                        players.add(player);
                               System.out.println(player.getId());
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        

                    } catch (IOException e) {
                        System.err.println(e);
                    }

                }
            }.start();
        }
        }
        catch (IOException e) {
            System.err.println(e);
        }
        

        
    }
        
  public static void main(String[] args) throws IOException{
    Table2 table = new Table2();
    new Thread(table).start();
    System.out.println("Masa a pornit");
}
}
