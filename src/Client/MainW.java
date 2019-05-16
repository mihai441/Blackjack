/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.c
 */
package Client;

import Client.Design.DefaultTheme;
import Client.Design.DyslexiaTheme;
import Client.Design.ITheme;
import Client.Design.Slot;
import Common.Constants;
import Common.Player;
import Common.Table;
import Common.TableStructure;
import Server.Table1;
import main.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
/**
 *
 * @author User
 */
public class MainW extends javax.swing.JFrame {

    /**
     * Creates new form MainW
     * @param table
     * @param player
     */
    public MainW(Table table, Player player) {
        this.player = player;
        banca = 500;
        miza=30;
        this.table = table;
        this.themes = new Stack<>();
        this.themes.push(new DyslexiaTheme());
        this.themes.push(new DefaultTheme());
        initConnection();
        initComponents();
        initlabels();
        updateAfis();
        updateTable();
        gameloop();   
    }

    private void gameloop(){
        
    }
    
    private void initConnection(){
            new Thread() {
                @Override
                public void run() {
                    ObjectInputStream inFromServer = null;
                    try {
                        socket = new Socket(table.getAdress(), table.getPort());
                        outToServer = new ObjectOutputStream(socket.getOutputStream());
                        outToServer.writeObject(player);
                        outToServer.flush();
                        outToServer.reset();
                        inFromServer = new ObjectInputStream(socket.getInputStream());
                        
                    } catch (IOException ex) {
                        Logger.getLogger(MainW.class.getName()).log(Level.SEVERE, null, ex);
                    }
                            
                    while(!Thread.interrupted()){
                        try {
                            Object object = inFromServer.readObject();
                            
                            if(object instanceof TableStructure){
                                tableStructure = (TableStructure) object;
                            }
                           

                            else if(object instanceof String){
                                String request = (String) object;
                                handleRequest(request);
                            }
                        }
                        catch (IOException ex) {
                            Logger.getLogger(Table1.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(MainW.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }.start();
    }
   
          
    private void updateTable(){
        Runnable upateTableRunnable = new Runnable() {
            @Override
            public void run() {
                while(!Thread.interrupted()){
                    TableStructure tempTableStructure = tableStructure;
                    getPlayer();
                    if(tempTableStructure!=null && tempTableStructure.getPlayers().size() > 0){
                        for(int i=0;i<tempTableStructure.getPlayers().size();i++){
                            for(int j=0;j<tempTableStructure.getPlayers().get(i).getPack().size();j++){
                                setCardIcon(slots.get(i).getCards(), tempTableStructure.getPlayers().get(i).getPack().get(j), j);
                            }
                        slots.get(i).setName(tempTableStructure.getPlayers().get(i).getName());
                        }
                        for(int i=0;i<tempTableStructure.getDealerCards().size(); i++){
                            setCardIcon(dealerSlot.getCards(), tempTableStructure.getDealerCards().get(i), i);
                        }
                    }
                    setText();
                }
            }
        };
        Thread updateTableThread = new Thread(upateTableRunnable);
        updateTableThread.setName("UpdateTableThread");
        updateTableThread.start();
    }
    
    
    private void getPlayer(){
        if(tableStructure!= null){
        player = tableStructure.getCurrentPlayer();
        }
    }
    
    private void inactiveButtons(){
         jButtonHit.setEnabled(false);
         jButtonStop.setEnabled(false);
         jButtonSelectMiza.setEnabled(false);
         jButtonPlus.setEnabled(false);
         jButtonMinus.setEnabled(false);
     }

     private void startBetting(){
      jButtonSelectMiza.setEnabled(true);
      jButtonPlus.setEnabled(true);
      jButtonMinus.setEnabled(true);
    }
  
     private void stopBetting(){
      jButtonSelectMiza.setEnabled(false);
      jButtonPlus.setEnabled(false);
      jButtonMinus.setEnabled(false);
      this.repaint();
        try {
            String mizaString = Integer.toString(miza);
            if(mizaString!= null){
            outToServer.writeUTF(mizaString);
            outToServer.flush();
            outToServer.reset();
            }
        } catch (IOException ex) {
            Logger.getLogger(MainW.class.getName()).log(Level.SEVERE, null, ex);
        } catch(NumberFormatException ex){
          try {
            outToServer.writeUTF("0");
            outToServer.flush();
            outToServer.reset();
          } catch (IOException ex1) {
              Logger.getLogger(MainW.class.getName()).log(Level.SEVERE, null, ex1);
          }
        }
        
     }
  
     private void setText(){
        mizaTextAfis.setText(Integer.toString(miza));
        credit.setText(Integer.toString(player.getCredit()));
        if(tableStructure!=null && tableStructure.getPlayers().size() > 0){
            for(int i=0;i<tableStructure.getPlayers().size();i++){
                int handValue = dealer.calculareValoare(tableStructure.getPlayers().get(i).getPack());
                if(handValue > 0){
                    slots.get(i).setCardValue(handValue);
                }
                else{
                    slots.get(i).setCardValue("");
                }
            }
        }
        if(tableStructure != null){
            if(dealer.getValue(tableStructure.getDealerCards()) > 0){
                dealerSlot.setCardValue(dealer.calculareValoare(tableStructure.getDealerCards()));
            }
            else{
                dealerSlot.setCardValue("");
            }
        }
     }
  
     private void actionTime(){
            jButtonHit.setEnabled(true);
            jButtonStop.setEnabled(true);
            this.repaint();
    }

    private void handleRequest(String request){
        Runnable handleRequest = new Runnable() {
        @Override
         public void run() {
            if(request.equals(Constants.stakeRequest)){
                stopBetting();
            }

            else if(request.equals(Constants.BETTING_TIME)){
                startBetting();
            }           

            else if(request.equals(Constants.actionRequest)){
                actionTime();
            }

            else if(request.equals(Constants.STOP_ACTION)){
                stopAction();
            }
            
            else if(request.equals(Constants.WIN)){
                //playsounds
            }
            
            else if(request.equals(Constants.LOSE)){
                //playsounds
            }
            
            else if(request.equals(Constants.NEW_ROUND)){
                clearTable();
            }
            
            else if(request.equals(Constants.BLACKJACK)){
                //playsounds
            }
            
            else if(request.equals(Constants.DRAW)){
                //playsounds
            }
         }
        };
        Thread handleRequestThread = new Thread(handleRequest);
        handleRequestThread.setName("HandleRequestThread");
        handleRequestThread.run();
         
    }
    
  
    private void stopAction(){
        jButtonHit.setEnabled(false);
        jButtonStop.setEnabled(false);
    }
    
    private void clearTable(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainW.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i=0;i<slots.size();i++){
            for(int j=0;j<slots.get(i).getCards().size();j++){
                slots.get(i).getCards().get(j).setIcon(null);
                slots.get(i).getCards().get(j).revalidate();
            }
        }
        for(int i=0;i<dealerSlot.getCards().size();i++){
            dealerSlot.getCard(i).setIcon(null);
            dealerSlot.getCard(i).revalidate();
        }

    }
    //Initializare 10 label-uri pentru a le utiliza la afisarea sprite-urilor
   private void initlabels(){  
     //creare labels carti player
     
        this.setResizable(false);
        this.setTitle("Blackjack Multiplayer: Table " + table.getId());
        //Slot player 0
            slots.add(new Slot());
            for(int i=0;i<10;i++){
                slots.get(0).addCard(new JLabel());
                jLabel1.add(slots.get(0).getCard(i));
                slots.get(0).getCard(i).setLocation(50+60*i+50, 220);
                slots.get(0).getCard(i).setSize(60,80);
            }
            slots.get(0).getName().setLocation(130, 200);
            slots.get(0).getName().setSize(80,20);
            slots.get(0).getName().setForeground(Color.ORANGE);
            jLabel1.add(slots.get(0).getName());
            
            slots.get(0).getCardValue().setLocation(80, 260);
            slots.get(0).getCardValue().setSize(80,20);
            slots.get(0).getCardValue().setForeground(Color.ORANGE);
            jLabel1.add(slots.get(0).getCardValue());
            
            //Slot Player 1
             slots.add(new Slot());
            for(int i=0;i<10;i++){
                slots.get(1).addCard(new JLabel());
                jLabel1.add(slots.get(1).getCard(i));
                slots.get(1).getCard(i).setLocation(300+60*i+50, 290);
                slots.get(1).getCard(i).setSize(60,80);
            }
            
            slots.get(1).getName().setLocation(380, 270);
            slots.get(1).getName().setSize(80,20);
            slots.get(1).getName().setForeground(Color.ORANGE);
            jLabel1.add(slots.get(1).getName());
            
            slots.get(1).getCardValue().setLocation(330, 350);
            slots.get(1).getCardValue().setSize(80,20);
            slots.get(1).getCardValue().setForeground(Color.ORANGE);
            jLabel1.add(slots.get(1).getCardValue());
            
        //creare slot dealer
            for(int i=0;i<10;i++){
                dealerSlot.addCard(new JLabel());
                jLabel1.add(dealerSlot.getCard(i));
                dealerSlot.getCard(i).setLocation(350+60*i, 100);
                dealerSlot.getCard(i).setSize(60,80);
            }
            
            dealerSlot.getName().setLocation(325, 80);
            dealerSlot.getName().setSize(80,20);
            dealerSlot.getName().setForeground(Color.ORANGE);
            jLabel1.add(dealerSlot.getName());
            
            dealerSlot.getCardValue().setLocation(310, 140);
            dealerSlot.getCardValue().setSize(80,20);
            dealerSlot.getCardValue().setForeground(Color.ORANGE);
            jLabel1.add(dealerSlot.getCardValue());

         //creare label credit
         
         jLabel1.add(credittext);
         credittext.setLocation(360,468);
         credittext.setText("Credit : ");
         credittext.setSize(80,30);
         credittext.setForeground(Color.ORANGE);

         //buton hit
        jLabel1.add(jButtonHit);
        jButtonHit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Assets/add.png"))); // NOI18N
        jButtonHit.setText("Hit");
        jButtonHit.setEnabled(false);
            jButtonHit.setSize(115,40);
            jButtonHit.setLocation(650,440);
        jButtonHit.addActionListener((java.awt.event.ActionEvent evt) -> {
            jButtonHitActionPerformed(evt);
        });
        
        //buton stop
         jLabel1.add(jButtonStop);
         jButtonStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Assets/stop.png"))); // NOI18N
         jButtonStop.setText("Stop");
         jButtonStop.setSize(128,40);
         jButtonStop.setEnabled(false);
         jButtonStop.setLocation(80,440);
         jButtonStop.addActionListener((java.awt.event.ActionEvent evt) -> {
             jButtonStopActionPerformed(evt);
        });
         
         //Buton miza - 
         jLabel1.add(jButtonMinus);
         jButtonMinus.setText("");
         jButtonMinus.setSize(30,30);
         jButtonMinus.setLocation(650,100);
         jButtonMinus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Assets/minus.png"))); // NOI18N
         jButtonMinus.addActionListener((java.awt.event.ActionEvent evt) -> {
             jButtonMinusActionPerformed(evt);
        });
        
        //buton miza +
         jLabel1.add(jButtonPlus);
         jButtonPlus.setText("");
         jButtonPlus.setSize(30,30);
         jButtonPlus.setLocation(700,100);
         jButtonPlus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Assets/plus.png"))); // NOI18N
         jButtonPlus.addActionListener((java.awt.event.ActionEvent evt) -> {
             jButtonPlusActionPerformed(evt);
        });
         
          //Static text Miza:
         jLabel1.add(switchButtonText);
         switchButtonText.setText("Dyslexia Theme");
         switchButtonText.setSize(120,30);
         switchButtonText.setLocation(35,30);
         switchButtonText.setForeground(Color.ORANGE);
         
         jLabel1.add(switchButton);
         switchButton.setLocation(50,70);
         switchButton.setSize(72,39);
         switchButton.setBorderPainted(false);
         switchButton.setFocusPainted(false);
         switchButton.setContentAreaFilled(false);
         switchButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            jButtonSwitchActionPerformed(evt);
         });
         switchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Assets/switchButtonOff.png")));
         
         //Static text Miza:
         jLabel1.add(MizaText);
         MizaText.setText("Bet: ");
         MizaText.setSize(50,30);
         MizaText.setLocation(670,30);
         MizaText.setForeground(Color.ORANGE);
         
//                  //Static text Miza:
//         jLabel1.add(MizaText);
//         MizaText.setText("Miza: ");
//         MizaText.setSize(50,30);
//         MizaText.setLocation(670,30);
//         MizaText.setFont(new Font("Serif", Font.BOLD, 16));
//         MizaText.setForeground(Color.ORANGE);
         
             //miza valoare
         jLabel1.add(mizaTextAfis);
         mizaTextAfis.setLocation(676,60);
         mizaTextAfis.setSize(30,30);
         mizaTextAfis.setForeground(Color.ORANGE);
         
          //Credit
          jLabel1.add(credit);
         credit.setLocation(415,468);
         credit.setSize(80,30);
         credit.setForeground(Color.ORANGE);
         
         //Buton selectare miza
         
         jLabel1.add(jButtonSelectMiza);
         jButtonSelectMiza.setLocation(650,142);
         jButtonSelectMiza.setText("Accept");
         jButtonSelectMiza.setEnabled(true);
         jButtonSelectMiza.setSize(80,30);
         jButtonSelectMiza.addActionListener((java.awt.event.ActionEvent evt) -> {
             jButtonSelectMizaPerformed(evt);
        });
         //jlabel nume
         updateFonts(false);
         inactiveButtons();
 }
    
    private void setCardIcon(ArrayList<javax.swing.JLabel> cardLabel, Card card,int index){
        if (card.isFaceUp()){
            int valoareTip=-1;
            switch(card.getSuit()){
               case Trefla:
                   valoareTip=0;
                   break;
               case Romb:
                   valoareTip=1;
                   break;
               case Inima:
                   valoareTip=2;
                   break;
               case Frunza:
                   valoareTip=3;
                   break;
            }    
        String parameter;
        parameter = "../main/Assets/" + Integer.toString(card.getVal()) + Integer.toString(valoareTip) + ".png";
        javax.swing.ImageIcon icon = new javax.swing.ImageIcon(getClass().getResource(parameter));
        cardLabel.get(index).setIcon(icon);
        }
        else{
             cardLabel.get(index).setIcon(new javax.swing.ImageIcon(getClass().getResource("../main/Assets/fd.png")));
        }
     
    }

    
    private void update(){
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    }
   @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Assets/bg.png"))); // NOI18N
        jLabel1.setText("bg");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 794, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

      private void jButtonHitActionPerformed(java.awt.event.ActionEvent evt) {
          try{
            outToServer.writeUTF(Constants.ACTION_HIT);
            outToServer.flush();    
            outToServer.reset();
        } catch (IOException ex) {
            Logger.getLogger(MainW.class.getName()).log(Level.SEVERE, null, ex);
        }
        jButtonHit.setEnabled(false);
        jButtonStop.setEnabled(false);
      }
      
      
      private void jButtonSwitchActionPerformed(java.awt.event.ActionEvent evt) {
         if(themes.peek().getTheme() instanceof DefaultTheme){
             themes.push(new DyslexiaTheme());
             updateFonts(true);
         }
         else{
             themes.pop();
             updateFonts(false);
         }
      }
      
      private void updateFonts(boolean isActivated){
          
          if(isActivated){
            switchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Assets/switchButtonOn.png")));
            switchButton.setSize(72,39);
          }
          else{
            switchButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Assets/switchButtonOff.png")));
            switchButton.setSize(72,39);
          }
          
          slots.get(0).getName().setFont(themes.peek().getInfoFont());
          slots.get(0).getCardValue().setFont(themes.peek().getInfoFont());
          slots.get(1).getName().setFont(themes.peek().getInfoFont());
          slots.get(1).getCardValue().setFont(themes.peek().getInfoFont());
          dealerSlot.getName().setFont(themes.peek().getInfoFont());
          dealerSlot.getCardValue().setFont(themes.peek().getInfoFont());
          credittext.setFont(themes.peek().getInfoFont());
          jButtonHit.setFont(themes.peek().getButtonFont()); 
          jButtonStop.setFont(themes.peek().getButtonFont()); 
          MizaText.setFont(themes.peek().getInfoFont());
          mizaTextAfis.setFont(themes.peek().getInfoFont());
          credit.setFont(themes.peek().getInfoFont());
          jButtonSelectMiza.setFont(themes.peek().getInfoFont());
          switchButtonText.setFont(themes.peek().getInfoFont());
          jLabel1.setIcon(themes.peek().getBackground()); 
          
          this.repaint();
      }

      
     private void jButtonStopActionPerformed(java.awt.event.ActionEvent evt) {                                         
        try {
            outToServer.writeUTF(Constants.ACTION_STAY);
            outToServer.flush();    
            outToServer.reset();
        } catch (IOException ex) {
            Logger.getLogger(MainW.class.getName()).log(Level.SEVERE, null, ex);
        }
        jButtonHit.setEnabled(false);
        jButtonStop.setEnabled(false);
    }  
      private void jButtonSelectMizaPerformed(java.awt.event.ActionEvent evt) {                                         
        
          if(miza>banca)
              miza=banca;
        jButtonSelectMiza.setEnabled(false);
        jButtonPlus.setEnabled(false);
        jButtonMinus.setEnabled(false);
        updateAfis();
      }
   private void jButtonMinusActionPerformed(java.awt.event.ActionEvent evt) {                                         
       if (miza>0)
        if (miza>10)
           miza-=10;
       updateAfis();
    }           private void jButtonPlusActionPerformed(java.awt.event.ActionEvent evt) {                                         
        if (miza<banca)
            if(miza<200)
                miza+=10;
        updateAfis();
    }  
    
    private void updateAfis(){
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
    private final ArrayList<Slot> slots = new ArrayList<>();
    private final Slot dealerSlot = new Slot();
    private final ArrayList<javax.swing.JLabel> cardOponent = new ArrayList<>();// initializare labels carti pc
    private final javax.swing.JButton jButtonHit= new javax.swing.JButton();
    private final javax.swing.JButton jButtonStop= new javax.swing.JButton();
    private final javax.swing.JButton jButtonPlus= new javax.swing.JButton();
    private final javax.swing.JButton jButtonMinus= new javax.swing.JButton();
    private final javax.swing.JButton jButtonSelectMiza= new javax.swing.JButton();
    private final Dealer dealer = new Dealer();
    private final Border border = LineBorder.createGrayLineBorder();
    private final javax.swing.JLabel MizaText = new javax.swing.JLabel();
    private final javax.swing.JLabel mizaTextAfis = new javax.swing.JLabel();
    private final javax.swing.JLabel switchButtonText = new javax.swing.JLabel();
    private final javax.swing.JButton switchButton = new javax.swing.JButton();
    private int miza;
    private int banca;
    private final javax.swing.JLabel credittext = new javax.swing.JLabel();
    private final javax.swing.JLabel credit = new javax.swing.JLabel();
    private Table table;
    private volatile TableStructure tableStructure;
    private Player player;
    private Socket socket;
    private ObjectOutputStream outToServer;
    private Stack<ITheme> themes;
}


