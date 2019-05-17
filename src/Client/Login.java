/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Common.Constants;
import Common.TableStructure;
import Server.TableServer;
import main.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class Login extends javax.swing.JFrame implements ActionListener {

    /**
     * Creates new form Login
     */
    public Login() {
        this.setResizable(false);
        initComponents();
        initLabels();
        initConnection();
    }
    
    private void initLabels(){
        
        this.setTitle("Login Window");
        
        Password = new javax.swing.JPasswordField();
        Username = new javax.swing.JTextField();
        UsernameText = new javax.swing.JLabel();
        PasswordText = new javax.swing.JLabel();
        Submit = new javax.swing.JButton();
        
       jLabel1.add(Password);
       jLabel1.add(Username);
       jLabel1.add(Submit);
       jLabel1.add(PasswordText);
       jLabel1.add(UsernameText);
       
       Password.setSize(130,25);
       Username.setSize(130,25);
       UsernameText.setText("Username: ");
       PasswordText.setText("Password: ");
       Submit.setSize(80,30);
       Submit.setText("Submit");
       
       Username.setLocation(360,256);
       Password.setLocation(360,297);
       Submit.setLocation(335,340);
       Submit.addActionListener(this);
       
        
    }
    
    public void actionPerformed(ActionEvent e)
    {
        try {
            String password = String.valueOf(Password.getPassword());
            outToServer.writeUTF(Username.getText()+"->"+password);
            outToServer.flush();
            if(inFromServer == null){
            inFromServer = new ObjectInputStream(socket.getInputStream());
            }
            String response = inFromServer.readUTF();
            if(response.equals(Constants.INVALID_LOGIN)){
                JOptionPane.showMessageDialog(null,
                   "Wrong username or password");
            }
            else if(response.equals(Constants.LOGIN_SUCCESS)){
                dispose();
                MainWindow m = new MainWindow(socket,outToServer,inFromServer);
                m.setVisible(true);
            }
            else{
                JOptionPane.showMessageDialog(null,
                   "An error has occured");
            }
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    public void initConnection(){
        new Thread() {
        @Override
            public void run() {
                ObjectInputStream inFromServer = null;
                try {
                    socket = new Socket(SERVER_ADRESS, SERVER_PORT);
                    outToServer = new ObjectOutputStream(socket.getOutputStream());

                } catch (IOException ex) {
                    Logger.getLogger(MainW.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/main/Assets/background.jpg"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
        );

        jLabel1.getAccessibleContext().setAccessibleName("background");

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JLabel UsernameText;
    private javax.swing.JTextField Username;
    private javax.swing.JLabel PasswordText;
    private javax.swing.JPasswordField Password;
    private javax.swing.JButton Submit;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
    private Socket socket;
    private final String SERVER_ADRESS = "localhost";
    private final int SERVER_PORT = 5678;
}
