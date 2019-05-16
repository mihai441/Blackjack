/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Common.Constants;
import Common.MainServerUser;
import Common.Player;
import Server.ClientThreads.ServerClientThread;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import Common.Table;
import Common.TableStructure;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import main.*;



/**
 *
 * @author AsX
 */
public class Table1 {
    
    private final int ID = 1;
    private final String ADRESS = "localhost";
    private final int PORT=6001;
    private final boolean AVAILABLE = true;
    private final boolean TAKEN = false;
    private final int MAX_PLAYERS=3;
    private final int TABLE_STAND_VALUE = 17;
    private String dataRequest;
    private int idRequested;
    private ArrayList<AtomicBoolean> positions;
    private volatile ArrayList<ServerClientThread>  playerThreads;
    private Pack pack;
    private Dealer dealer; 
    private volatile TableStructure tableStructure;
    private ArrayList<Card> dealerCards;
    private ArrayList<Integer> activePlayers;
    private ObjectOutputStream outToMainServer;
    private ObjectInputStream inFromMainServer;
    private Socket s;
    
    public Table1() {
        this.dealer = new Dealer();
        this.playerThreads = new ArrayList<>();
        this.positions = new ArrayList<>();
        this.dealerCards = new ArrayList<>();
        this.tableStructure = new TableStructure();
        this.activePlayers = new ArrayList<>();
    }
    
    public void update() {
          Runnable serverTask = () -> {
              boolean isRunning=false;
              boolean newRound=false;
              while (true) {
                  if(!isRunning){
                      if(playerThreads.size()>0){
                          isRunning=true;
                          newRound=true;
                      }
                  }
                  if(isRunning){
                      if(newRound){
//                        checkOnlinePlayers();
                          setActivePlayers();
                          pack = new Pack();
                          initCards();
                          newRound=false;
                          forceUpdateTables();
                          sendMessageEveryUser(Constants.BETTING_TIME);
                          try{
                              Thread.sleep(10000);
                          }
                          catch(InterruptedException e){}
                      }
                      getStakes();
                      dealcards(numOfPlayers());
                      forceUpdateTables();
                      try {
                          Thread.sleep(700);
                      } catch (InterruptedException ex) {
                          Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
                      }
                      checkInputs();
                      showDealer();
                      win();
                      clearCards();
                      forceUpdateTables();
                      sendMessageEveryUser(Constants.NEW_ROUND);

                  }
                  try {
                      Thread.sleep(2500);
                  } catch (InterruptedException ex) {
                      Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  isRunning=false;
                  newRound=false;
              } };
          Thread serverThread = new Thread(serverTask);
          serverThread.setName("GameLoopThread");
          serverThread.start();
    }


    private void setActivePlayers(){
        activePlayers = new ArrayList<>();
        for(ServerClientThread clientThread: playerThreads)
            if(clientThread.getPlayer().getTableSlot() != -1){
                activePlayers.add(clientThread.getPlayer().getId());
            }
    }
    
    private void clearCards(){
        for(int i=0;i<playerThreads.size();i++){
            playerThreads.get(i).getPlayer().getPack().clear();
        }
        dealerCards.clear();
    }
    public void getStakes(){
        for(int i=0;i<numOfPlayers();i++){
           playerThreads.get(i).setMessage(Constants.stakeRequest);
           String stake = playerThreads.get(i).getMessage();
           playerThreads.get(i).getPlayer().setStake(Integer.parseInt(stake));
           if(Integer.parseInt(stake) <= 0){
                for(Integer activePlayer : activePlayers){
                    if(activePlayer.equals(playerThreads.get(i).getPlayer().getId())){
                        activePlayers.remove(i);
                    }
                }    
            }
        }
    }
    
    public int numOfPlayers() {
        return activePlayers.size();
    }


    public void win() {
        
        if(dealer.calculareValoare(dealerCards) > 21){
            for(Integer activePlayer : activePlayers){
                for(int i=0;i<playerThreads.size();i++){
                     if(playerThreads.get(i).getPlayer().getId() == activePlayer && !isBusted(i)){
                         try {
                            int newCredit = playerThreads.get(i).getPlayer().getCredit()+playerThreads.get(i).getPlayer().getStake();
                            outToMainServer.writeUTF("NEWCREDIT" + ":" + newCredit + ":" + playerThreads.get(i).getPlayer().getId());
                            outToMainServer.flush();
                            playerThreads.get(i).setMessage(Constants.WIN);
                            playerThreads.get(i).getPlayer().setCredit(newCredit);
                            break;
                         } catch (IOException ex) {
                             Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
                         }
                     }
                }    
            }
        }
        else{
             for(Integer activePlayer : activePlayers){
                for(int i=0;i<playerThreads.size();i++){
                     if(playerThreads.get(i).getPlayer().getId() == activePlayer && !isBusted(i)){
                         if(dealer.calculareValoare(playerThreads.get(i).getPlayer().getPack()) > dealer.calculareValoare(dealerCards)){
                            try {
                                int newCredit = playerThreads.get(i).getPlayer().getCredit()+playerThreads.get(i).getPlayer().getStake();
                                outToMainServer.writeUTF("NEWCREDIT" + ":" + newCredit + ":" +playerThreads.get(i).getPlayer().getId());
                                outToMainServer.flush();
                                playerThreads.get(i).setMessage(Constants.WIN);
                                playerThreads.get(i).getPlayer().setCredit(newCredit);
                                playerThreads.get(i).getPlayer().getPack().clear();
                                Thread.sleep(100);
                            } 
                            catch (IOException ex) {
                                 Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            catch (InterruptedException ex) {
                                 Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
                            }
                         }
                         else if(dealer.calculareValoare(playerThreads.get(i).getPlayer().getPack()) == dealer.calculareValoare(dealerCards)){
                             playerThreads.get(i).setMessage(Constants.DRAW);
                             playerThreads.get(i).getPlayer().getPack().clear();
                         }
                         else{
                             userLost(i);
                         }
                    }
                    //This means the user went over 21
                    else{
                        userLost(i);
                    }
                }
             }
                    
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
        }
        dealerCards.clear();
        forceUpdateTables();
    }
    
    private void userLost(int i){
        try{
            int newCredit = playerThreads.get(i).getPlayer().getCredit()-playerThreads.get(i).getPlayer().getStake();
            outToMainServer.writeUTF("NEWCREDIT" + ":" + newCredit + ":" +playerThreads.get(i).getPlayer().getId());
            outToMainServer.flush();
            playerThreads.get(i).setMessage(Constants.LOSE);
            playerThreads.get(i).getPlayer().setCredit(newCredit);
            playerThreads.get(i).getPlayer().getPack().clear();
        } catch (IOException ex) {
            Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void checkInputs(){
        for(Integer activePlayer : activePlayers){
            for(int i=0;i<playerThreads.size();i++){
                if(playerThreads.get(i).getPlayer().getId() == activePlayer){
                    String playerAction = "";
                    boolean isBusted = false;
                    if(dealer.calculareValoare(playerThreads.get(i).getPlayer().getPack()) == 21){
                        playerAction = Constants.ACTION_STAY;
                        playerThreads.get(i).setMessage(Constants.BLACKJACK);
                    }
                    while(!playerAction.equals(Constants.ACTION_STAY) && !isBusted){
                        playerThreads.get(i).setMessage(Constants.actionRequest);
                        playerAction = playerThreads.get(i).getMessage();
                        if(playerAction == null){
                            playerAction = Constants.ACTION_STAY;
                        }
                        if(playerAction.equals(Constants.ACTION_HIT)){
                            getCard(playerThreads.get(i).getPlayer());
                            forceUpdateTables();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                            }
                        }
                        //Check if busted 
                        isBusted = isBusted(i);
                        if(isBusted){
                            playerAction = Constants.ACTION_STAY;
                        }           
                    }
                    if(playerAction.equals(Constants.ACTION_STAY)){
                        playerThreads.get(i).setMessage(Constants.STOP_ACTION);
                    }
                }  
            }
        }

    }
    
    private boolean isBusted(int i){
        if(dealer.calculareValoare(playerThreads.get(i).getPlayer().getPack()) > 21){
            return true;
        }
        return false;
    }
 
    private void sendMessageEveryUser(String message){
        playerThreads.forEach((playerThread) -> {
            playerThread.setMessage(message);
        });
    }
    
//    private void updateUsersTable(){
//        //send player array to every player
//         Runnable serverTask = new Runnable() {
//           @Override
//            public void run() {
//                while(!Thread.interrupted()){
//                    ArrayList<Player> currentPlayers = new ArrayList<>();
//                    
//                    playerThreads.forEach((playerThread) -> {
//                        currentPlayers.add(playerThread.getPlayer());
//                    });
//                    
//                    tableStructure.setPlayers(currentPlayers);
//                    tableStructure.setDealerCards(dealerCards);

//                    playerThreads.forEach((playerThread) -> {
//                        playerThread.setTable(tableStructure);
//                    });
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//         };
//         Thread serverThread = new Thread(serverTask);
//         serverThread.setName("PlayerTableUpdate");
//         serverThread.start();
//    }

    public void showDealer(){
        dealerCards.get(1).faceup = true;
        forceUpdateTables();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(dealer.calculareValoare(dealerCards) < TABLE_STAND_VALUE){
            dealer.getCarteDealer(pack, dealerCards, true);
            forceUpdateTables();
            try {
                Thread.sleep(800);
            } catch (InterruptedException ex) {
                Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
     
    private void forceUpdateTables(){
        ArrayList<Player> currentPlayers = new ArrayList<>();
                    
        playerThreads.forEach((playerThread) -> {
            currentPlayers.add(playerThread.getPlayer());
        });

        tableStructure.setPlayers(currentPlayers);

//        if(dealerCards.size() > 0 && !dealerCards.get(1).isFaceUp()){
//            ArrayList<Card> tempDealerCards = new ArrayList<>();
//            tempDealerCards.add(dealerCards.get(0));
//            Card card = new Card(Card.Tip.values()[0], 0);
//            card.flip();
//            tempDealerCards.add(card);
//            tableStructure.setDealerCards(tempDealerCards);
//        }
//        else{
        tableStructure.setDealerCards(dealerCards);


        playerThreads.forEach((playerThread) -> {
            tableStructure.setCurrentPlayer(playerThread.getPlayer());
            playerThread.setTable(tableStructure);
         });
    }
    
    public void dealcards(int numOfPlayers){
        for(int i=0;i<numOfPlayers;i++){
            dealer.getCartePlayer(pack, playerThreads.get(i).getPlayer());  
            dealer.getCartePlayer(pack, playerThreads.get(i).getPlayer());
        }
        dealer.getCarteDealer(pack, dealerCards,true);
        dealer.getCarteDealer(pack, dealerCards,false);
    }
     
     public void getCard(Player player){
        dealer.getCartePlayer(pack, player);
     }
     
    public void run() {
                initTablePositions();
                connectToMainServer();
                startServer();
                update();
    }
    
    private void initTablePositions(){
        for(int i=0;i<MAX_PLAYERS;i++){
            positions.add(new AtomicBoolean());
            positions.get(i).set(AVAILABLE);
        }
    }
   
    
    private Table getTable(){
        return new Table(ID, numOfPlayers(), ADRESS,PORT,AVAILABLE);
    }
    
    void connectToMainServer(){
            new Thread() {
                  @Override
            public void run() {
            Thread.currentThread().setName("UpdateLoopThread");
                 String server = "localhost";
            try {
                s = new Socket(server, 5679);
            } catch (IOException ex) {
                Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Conexiunea cu serverul nu s-a putut realiza!");
            }
            System.out.println("Conexiunea cu serverul principal s-a realizat cu succes!");
                try {
                    outToMainServer = new ObjectOutputStream(s.getOutputStream());
                    if(inFromMainServer == null){
                        inFromMainServer = new ObjectInputStream(s.getInputStream());
                    }
                    String request = inFromMainServer.readUTF();
                    switch (request) {
                        case "Table":
                            outToMainServer.writeObject(getTable());
                            break;
                            //TODO
                    }
                    outToMainServer.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
                }
            };}.start();
    }
    
    String retriveFromPlayer(){
        return dataRequest;
    }
    
    int getNextAvailableSlot(){
     
        for(int i=0;i<MAX_PLAYERS;i++)
            if(positions.get(i).get()){
                positions.get(i).set(false);
                return i;
            }
        return -1;
    }
    
    String getIdRequested(){
        return Integer.toString(idRequested);
    }
    
    void startServer(){
        
         Runnable serverTask = () -> {
             try {
                 ServerSocket server = new ServerSocket(PORT);
                 ObjectInputStream inFromPlayer = null;
                 Player player = null;
                 ServerClientThread clientThread = null;
                 while (!Thread.currentThread().isInterrupted()) {
                     Socket client = server.accept();
                     inFromPlayer = new ObjectInputStream(client.getInputStream());
                     try {
                         player = (Player)inFromPlayer.readObject();
                         Player serverPlayer = serverGetPlayer(player);
                         if(serverPlayer != null){
                            int availableSlot = getNextAvailableSlot();
                            if(availableSlot != -1){
                                serverPlayer.setTableSlot(availableSlot);
                                clientThread = new ServerClientThread(client,serverPlayer, inFromPlayer);
                                clientThread.run();
                                playerThreads.add(clientThread);
                            }
                         }
                     } catch (ClassNotFoundException ex) {
                         Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 }
                 if(player.getTableSlot() != -1){
                     positions.get(player.getTableSlot()).set(TAKEN);
                 }
                 playerThreads.remove(clientThread);
             }
             catch(IOException e){
                 System.out.println(e);
             }};
          Thread serverThread = new Thread(serverTask);
          serverThread.start();  
    }
    
    private Player serverGetPlayer(Player player){
        try {
            outToMainServer.writeUTF("getPlayer"+":"+player.getId());
            outToMainServer.flush();
            if(inFromMainServer == null){
                inFromMainServer = new ObjectInputStream(s.getInputStream());
            }
            MainServerUser receivedPlayer = (MainServerUser)inFromMainServer.readObject();
            Player newPlayer = new Player(receivedPlayer.getId(),receivedPlayer.getName(),receivedPlayer.getCredit());
            return newPlayer;
        } catch (IOException ex) {
            Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

        
  public static void main(String[] args) throws IOException{
    Table1 table = new Table1();
    table.run();
    System.out.println("Masa a pornit");
    }

    private void initCards() {
        //Golesete pachetul jucatorilor de carti
        for(int i=0;i<numOfPlayers();i++){
        playerThreads.get(i).getPlayer().getPack().clear();
        }
        dealerCards.clear();
    }
}
