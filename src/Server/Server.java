/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;


import Common.Constants;
import Server.ClientThreads.MainServerClient;
import Common.Player;
import Common.Table;
import Common.MainServerUser;
import Server.Helpers.DatabasePersistence;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author AsX
 */


class Server {
  public final int PLAYERPORT = 5678;
  public final int TABLEPORT = 5679;
  private DatabasePersistence db = new DatabasePersistence();
  private ArrayList<Player> players = new ArrayList<>();
  private ArrayList<Table> tables = new ArrayList<>();
  private ArrayList<MainServerClient> clients = new ArrayList<>();

    public void run() {
            new Thread() {
                @Override
                public void run() {
                    Thread.currentThread().setName("playerServers");
                    playerServers();
                }
            }.start();
            new Thread() {
                @Override
                public void run() {
                    Thread.currentThread().setName("tableServers");
                    tableServers();
                }
            }.start();
    }
    

    void playerServers() {
         Runnable serverTask = () -> {
             try {
                 ServerSocket server = new ServerSocket(PLAYERPORT);
                 System.out.println("Player server handler is now running!");
                 while (true) {
                          
                     final Socket client = server.accept();
                     
                     Runnable loginThread = () -> {
                         try {
                             ObjectOutputStream outToClient = new ObjectOutputStream(client.getOutputStream());
                             ObjectInputStream inFromClient = new ObjectInputStream(client.getInputStream());
                             
                             while(!Thread.interrupted()){
                                 String loginCredentials = inFromClient.readUTF();
                                 String[] separatedCredentials = loginCredentials.split("->");
                                 MainServerUser user = db.checkLoginData(separatedCredentials[0], separatedCredentials[1]);
                                 if(user != null){
                                     MainServerClient mainServerClient = new MainServerClient(client, outToClient, inFromClient);
                                     mainServerClient.setUser(user);
                                     mainServerClient.run();
                                     clients.add(mainServerClient);
                                     outToClient.writeUTF(Constants.LOGIN_SUCCESS);
                                     outToClient.flush();
                                     Thread.currentThread().interrupt();
                                 }
                                 else{
                                     outToClient.writeUTF(Constants.INVALID_LOGIN);
                                     outToClient.flush();
                                 }
                             }
                         } catch (IOException ex) {
                             Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                         }};
                         Thread userLoginThread = new Thread(loginThread);
                         loginThread.run();
                 }
             }
             catch(IOException e){
                 System.out.println(e);
             }};
          Thread serverThread = new Thread(serverTask);
          serverThread.start();
    }
    
    private int isUpdateThreadRunning(){
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for(Thread thread : threadSet){
            if(thread.getName().equals("UpdateLoopThread"))
                return 1;
        }
        startUpdateThread();
        return 0;
    }
    
    private void startUpdateThread(){
          new Thread() {
                @Override
                public void run() {
                Thread.currentThread().setName("UpdateLoopThread");
                    while(true){
                    updateTables();
                    updateInformations();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        System.out.println("thread closing");
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                }
          }.start();
    }
        
//    private void updatePlayerThreads(){
//                        for(MainServerClient client : clients){
//                            if(client.isUserConnected()){
//                                if(!client.isAlive())
//                                    client.start();
//                            }
//                            else{
//                                client.stop();
//                                clients.remove(client);                            
//                            }
//                        }
//    }
    
    private void updateTables(){
        clients.forEach((client) -> {
            client.UpdateTables(tables);
      });
    }

    private void updateInformations(){
         clients.forEach((client) -> {
            client.updateInfo();
      });
    }
    void tableServers() {
        try {
            ServerSocket server = new ServerSocket(TABLEPORT);
            System.out.println("Table Server handler is now running!");
            while (true) {
               final Socket client = server.accept();
               new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.currentThread().setName("tableServer");
                        ObjectOutputStream outToClient = new ObjectOutputStream(client.getOutputStream());
                        ObjectInputStream inFromClient = new ObjectInputStream(client.getInputStream());
                            try {
                                outToClient.writeUTF("Table");
                                outToClient.flush();
                                Table table = (Table) inFromClient.readObject();
                                if(!tables.contains(table))
                                    tables.add(table);
                                while(!Thread.interrupted()){
                                    String request = inFromClient.readUTF();
                                    String[] splitRequest = request.split(":");
                                    if(splitRequest[0].equals("getPlayer")){
                                        int playerId = Integer.parseInt(splitRequest[1]);
                                        boolean found = false;
                                        for(int i=0;i<clients.size();i++){
                                            if(clients.get(i).getUser().getId() == playerId){
                                                outToClient.writeObject(clients.get(i).getUser());
                                                outToClient.flush();
                                                found = true;
                                            }
                                        }
                                        if(!found){
                                            outToClient.writeObject(null);
                                            outToClient.flush();
                                        }
                                    }
                                    else if(splitRequest[0].equals("NEWCREDIT")){
                                        int newCredit = Integer.parseInt(splitRequest[1]);
                                        int playerId = Integer.parseInt(splitRequest[2]);
                                        db.updateUserCredit(newCredit, playerId);
                                    }
                                    
                                }
                                
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            for(int i=0;i<tables.size();i++){
                            System.out.println(tables.get(i).getId());
                            System.out.println(tables.get(i).getAdress());
                            System.out.println(tables.get(i).getPort());
                            System.out.println(tables.get(i).isAvailable());
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
      
    Server server = new Server();
    server.run();
    while(true){
        server.isUpdateThreadRunning();
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  }
  
}