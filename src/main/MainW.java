/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
/**
 *
 * @author User
 */
public class MainW extends javax.swing.JFrame {

    /**
     * Creates new form MainW
     */
    public MainW() {
        banca=Login.banca;
        miza=30;
        this.setResizable(false);
//      initWorker();
        initComponents();
        initlabels();
        gameloop();
          
        
    }

    private void gameloop(){

          initCards();
          inactiveButtons();
          updateAfis();
        //  worker.execute();
         // worker2.execute();
        
    }
   
          
  private void inactiveButtons(){
      
      jButtonHit.setEnabled(false);
      jButtonStop.setEnabled(false);
      jButtonSelectMiza.setEnabled(true);
      jButtonPlus.setEnabled(true);
      jButtonMinus.setEnabled(true);
      
      
      
  }
          //initiare worker
//   private void initWorker(){ 
//       
//   worker = new SwingWorker<Void, Void>() {
//   @Override
//   protected Void doInBackground() throws Exception {
//    return null;
//   }
//   };
//   
//   worker2 = new SwingWorker<Void, Void>() {
//   @Override
//   protected Void doInBackground() throws Exception {
//     update();
//     updateAfis();
//     
//    return null;
//   }
//  };
//        
//   }
    
    private void initCards(){
    cartiPlayer= new ArrayList<>();     
    cartiPC =  new ArrayList<>();
    pachet = new Pack();
    
    //goleste arraylists
    if(cartiPC.size()>0){
      cartiPlayer.clear();
      cartiPC.clear();
      pachet.clear();
    }
    
    //carti din pachet in pachetul jucatorilor
    dealer.getCartePlayer(pachet,cartiPlayer,false);
    dealer.getCartePC(pachet,cartiPC, false);
    dealer.getCartePlayer(pachet,cartiPlayer,false);
    dealer.getCartePC(pachet,cartiPC, false);
    isOver21();
    
        
}  
    //Initializare 10 label-uri pentru a le utiliza la afisarea sprite-urilor
   private void initlabels(){  
     //creare labels carti player
        for(int i=0;i<10;i++){
        card.add(new javax.swing.JLabel());
        jLabel1.add(card.get(i));
        card.get(i).setLocation(350+60*i, 300);
        card.get(i).setSize(60,82);
    }
        //creare label carti pc
         for(int i=0;i<10;i++){
        cardOponent.add(new javax.swing.JLabel());
        jLabel1.add(cardOponent.get(i));
        cardOponent.get(i).setLocation(350+61*i, 100);
        cardOponent.get(i).setSize(60,82);
   }

         //creare label credit
         
         jLabel1.add(credittext);
         credittext.setLocation(360,468);
         credittext.setText("Credit : ");
         credittext.setSize(80,30);
         credittext.setFont(new Font("Serif", Font.BOLD, 16));
         credittext.setForeground(Color.ORANGE);

         //buton hit
        jLabel1.add(jButtonHit);
        jButtonHit.setFont(new java.awt.Font("Verdana", 1, 20)); 
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
         jButtonStop.setFont(new java.awt.Font("Verdana", 1, 20)); 
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
         jLabel1.add(MizaText);
         MizaText.setText("Miza: ");
         MizaText.setSize(50,30);
         MizaText.setLocation(670,30);
         MizaText.setFont(new Font("Serif", Font.BOLD, 16));
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
         mizaTextAfis.setFont(new Font("Serif", Font.BOLD, 20));
         mizaTextAfis.setForeground(Color.ORANGE);
         
          //Credit
          jLabel1.add(credit);
         credit.setLocation(415,468);
         credit.setSize(80,30);
         credit.setFont(new Font("Serif", Font.BOLD, 19));
         credit.setForeground(Color.ORANGE);
         
         //Buton selectare miza
         
         jLabel1.add(jButtonSelectMiza);
         jButtonSelectMiza.setLocation(650,142);
         jButtonSelectMiza.setText("Accept");
         jButtonSelectMiza.setEnabled(true);
         jButtonSelectMiza.setSize(80,30);
         jButtonSelectMiza.setFont(new Font("Serif", Font.BOLD,10));
         jButtonSelectMiza.addActionListener((java.awt.event.ActionEvent evt) -> {
             jButtonSelectMizaPerformed(evt);
        });
         //jlabel nume
         
         jLabel1.add(nume);
         nume.setSize(100,30);
         nume.setFont(new Font("Serif", Font.BOLD,16));
         nume.setLocation(100,100);
         nume.setText(Login.user);
         nume.setForeground(Color.ORANGE);
         numeUser=Login.user;
         
 }
   private void update2(){
       updateAfis();
       isOver21();
   }
   
   private void isOver21(){
       if (dealer.calculareValoarePlayer(cartiPlayer)>21){
           for(int i=0;i<cartiPlayer.size();i++)
               if (cartiPlayer.get(i).getVal()==14){
                   cartiPlayer.get(i).setVal(1);
                   if(dealer.calculareValoarePlayer(cartiPlayer)<21)
                       break;
               }
           updateAfis();
       }
          if (dealer.calculareValoarePlayer(cartiPlayer)>21){
           JOptionPane.showMessageDialog(this,"Ai depasit 21, dealerul castiga!");
           //this.setVisible(false); //revino ulterior!!!
//           this.dispose();
//                new MainW().setVisible(true);
            banca -=miza;
            Login.updatebanca(banca,numeUser);
            updateAfis();
            input();
                
       }
   }
    private void updateAfis(){
       //afisare icon carti player
       
        for(int i=0;i<cartiPlayer.size();i++){
            setCardIcon(cartiPlayer.get(i).isFaceUp(), cartiPlayer.get(i).getVal(),card,cartiPlayer.get(i).getSuit(),i);
            card.get(i).setBorder(border);
    }
        //afisare icon carti pc
         for(int i=0;i<cartiPC.size();i++){
            setCardIcon(cartiPC.get(i).isFaceUp(), cartiPC.get(i).getVal(),cardOponent,cartiPC.get(i).getSuit(),i);
            cardOponent.get(i).setBorder(border);
         }
         
         for(int i=9; i>=cartiPlayer.size();i--){
                 card.get(i).setIcon(null); 
                 card.get(i).setBorder(null);
         }
         for(int i=9; i>=cartiPC.size();i--){
             cardOponent.get(i).setIcon(null);
             cardOponent.get(i).setBorder(null);
         }
         afisareValoare();
         
         
          setText();

        
    }
    
    private void checkWhoWon(){
            if ((dealer.calculareValoarePlayer(cartiPlayer)< dealer.calculareValoarePC(cartiPC)) && (dealer.calculareValoarePC(cartiPC)<=21)){
               JOptionPane.showMessageDialog(this,"Ai pierdut!");
//               this.setVisible(false); //revino ulterior!!!
//               this.dispose();
//                new MainW().setVisible(true);
                banca-=miza;
                Login.updatebanca(banca,numeUser);
                
            }
            else if (dealer.calculareValoarePlayer(cartiPlayer)== dealer.calculareValoarePC(cartiPC)){
               JOptionPane.showMessageDialog(this,"Este egalitate!");
//               this.setVisible(false); //revino ulterior!!!
//               this.dispose();
//                new MainW().setVisible(true);
                     
            }
            
            else{
                JOptionPane.showMessageDialog(this,"Felicitari, ai castigat!");
//                this.setVisible(false); //revino ulterior!!!
//                this.dispose();
//                new MainW().setVisible(true);
                banca+=miza;
                Login.updatebanca(banca,numeUser);
              
            }
 }
    private void afisareValoare(){
    valoarePC.setLocation(320, 130);
    valoarePC.setSize(20,20);
    valoarePC.setFont(new Font("Serif", Font.BOLD, 20));
    valoarePC.setForeground(Color.ORANGE);
//    valoarePC.setBorder(new LineBorder(Color.black));

    valoarePlayer.setForeground(Color.ORANGE);
    //valoarePlayer.setBorder(new LineBorder(Color.black));


    valoarePlayer.setFont(new Font("Serif", Font.BOLD, 20));
    valoarePlayer.setLocation(320, 330);
    valoarePlayer.setSize(20,20);
    jLabel1.add(valoarePlayer);
    jLabel1.add(valoarePC);
    //credit

         credit.setText(Integer.toString(banca));

}
    
    private void input(){
    gameloop();
    }
    private void setText(){
        valoarePC.setText(Integer.toString(dealer.calculareValoarePC(cartiPC)));
        valoarePlayer.setText(Integer.toString(dealer.calculareValoarePlayer(cartiPlayer)));
        mizaTextAfis.setText(Integer.toString(miza));
    }
    
    private void setCardIcon(boolean faceup, int val,ArrayList<javax.swing.JLabel> card, Card.Tip tip,int index){
        if (faceup){
       int valoareTip=-1;
       switch(tip){
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
         parameter = "/main/Assets/" + Integer.toString(val) + Integer.toString(valoareTip) + ".png";
        card.get(index).setIcon(new javax.swing.ImageIcon(getClass().getResource(parameter)));
    }
        else{
             card.get(index).setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Assets/fd.png")));
        }
     
    }
    private void AIdealer(){
       updateAfis();
        while(dealer.calculareValoarePC(cartiPC)<16){
            dealer.getCartePC(pachet, cartiPC, true);
            pcOver21();
            updateAfis();
            
        }
  }
    
    private void pcOver21(){
          if (dealer.calculareValoarePC(cartiPC)>21){
           for(int i=0;i<cartiPC.size();i++)
               if (cartiPC.get(i).getVal()==14){
                   cartiPC.get(i).setVal(1);
                   if(dealer.calculareValoarePC(cartiPC)<21)
                       break;
               }
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setIcon(themes.peek().getBackground()); // NOI18N
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
    }// </editor-fold>//

      private void jButtonHitActionPerformed(java.awt.event.ActionEvent evt) {                                         
        dealer.getCartePlayer(pachet,cartiPlayer,true);
        update2();
    }    
      
     private void jButtonStopActionPerformed(java.awt.event.ActionEvent evt) {                                         
        cartiPC.get(1).faceup=true;
        updateAfis();
         if (dealer.calculareValoarePC(cartiPC)==21){
        JOptionPane.showMessageDialog(this,"Oponentul are Blackjack, ati pierdut!");
        banca-=miza;
        Login.updatebanca(banca,numeUser);
        input();
         }
         else{
        AIdealer();
        checkWhoWon();   
        updateAfis();
        input();
         }
    }  
      private void jButtonSelectMizaPerformed(java.awt.event.ActionEvent evt) {                                         
        
          if(miza>banca)
              miza=banca;
        jButtonSelectMiza.setEnabled(false);
        jButtonPlus.setEnabled(false);
        jButtonMinus.setEnabled(false);
        jButtonHit.setEnabled(true);
        jButtonStop.setEnabled(true);
        cartiPlayer.get(0).faceup=true;
        cartiPlayer.get(1).faceup=true;
        cartiPC.get(0).faceup=true;
        
        updateAfis();
        
        
        //verificare blackjack
         if (dealer.calculareValoarePlayer(cartiPlayer)==21){
        JOptionPane.showMessageDialog(this,"Felicitari, ai Blackjack!");
        banca+=2*miza;
        Login.updatebanca(banca,numeUser);
        updateAfis();
        input();
         }
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
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainW.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
         new MainW().setVisible(true);
         

        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
    private final ArrayList<javax.swing.JLabel> card = new ArrayList<>();//Initializare labels carti player
    private final ArrayList<javax.swing.JLabel> cardOponent = new ArrayList<>();// initializare labels carti pc
    private final javax.swing.JButton jButtonHit= new javax.swing.JButton();
    private final javax.swing.JButton jButtonStop= new javax.swing.JButton();
    private final javax.swing.JButton jButtonPlus= new javax.swing.JButton();
    private final javax.swing.JButton jButtonMinus= new javax.swing.JButton();
    private final javax.swing.JButton jButtonSelectMiza= new javax.swing.JButton();
    private Pack pachet;
    private ArrayList<Card> cartiPlayer;
    private ArrayList<Card> cartiPC;
    private final Dealer dealer = new Dealer();
    private final Border border = LineBorder.createGrayLineBorder();
    private final javax.swing.JLabel valoarePlayer = new javax.swing.JLabel();
    private final javax.swing.JLabel MizaText = new javax.swing.JLabel();
    private final javax.swing.JLabel mizaTextAfis = new javax.swing.JLabel();
    private final javax.swing.JLabel valoarePC = new javax.swing.JLabel();
    private final javax.swing.JLabel nume = new javax.swing.JLabel();
    private int miza;
    private int banca;
    private final javax.swing.JLabel credittext = new javax.swing.JLabel();
    private final javax.swing.JLabel credit = new javax.swing.JLabel();
    private SwingWorker<Void, Void> worker;
    private SwingWorker<Void, Void> worker2;
    private int input;
    private String numeUser;
}


