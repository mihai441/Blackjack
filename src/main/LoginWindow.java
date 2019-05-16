/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import Client.MainWindow;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

 
public class LoginWindow extends JFrame implements ActionListener
{
 
 
    LoginWindow()
    {
    initWindow();
    }
    
    private void initWindow(){
                setTitle("Fereastra login");
        setVisible(true);
        setSize(500, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        l1 = new JLabel("Formular Login:");
        l1.setForeground(Color.blue);
        l1.setFont(new Font("Serif", Font.BOLD, 20));
 
        l2 = new JLabel("Nume:");
        l3 = new JLabel("Parola:");
        tf1 = new JTextField();
        p1 = new JPasswordField();
        btn1 = new JButton("Submit");
 
        l1.setBounds(100, 30, 400, 30);
        l2.setBounds(80, 70, 200, 30);
        l3.setBounds(80, 110, 200, 30);
        tf1.setBounds(300, 70, 200, 30);
        p1.setBounds(300, 110, 200, 30);
        btn1.setBounds(150, 160, 100, 30);
 
        add(l1);
        add(l2);
        add(tf1);
        add(l3);
        add(p1);
        add(btn1);
        btn1.addActionListener(this);
    }
 
    @Override
    public void actionPerformed(ActionEvent e)
    {
        showData();
    }
 
    public void showData(){
 
        String str1 = tf1.getText();
        char[] p = p1.getPassword();
        String str2 = new String(p);
        try
        {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/blackjack","root","");
            PreparedStatement ps = con.prepareStatement("select * from users where nume=? and parola=?");
            ps.setString(1, str1);
            ps.setString(2, str2);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                 banca=rs.getInt("credit");
                 nume=rs.getString("nume");
//                f1.pack();
                  dispose();
//                f1.setSize(600, 600);
//                f1.setLayout(null);
//                Main_Window b = new Main_Window();
//                b.setVisible(true);
                  MainWindow m = new MainWindow();
                  m.setVisible(true);
                  this.dispose();
            } else
            {
                JOptionPane.showMessageDialog(null,
                   "Nume sau parola gresita");
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }
 
    public static void updatebanca(int miza,String nume){
     
               try
        {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/blackjack","root","");
            PreparedStatement ps = con.prepareStatement("update users  set credit=? where nume=?");
            ps.setInt(1, miza);
            ps.setString(2, nume);
            ps.executeUpdate();
    }
                catch (Exception ex)
        {
            System.out.println(ex);
        }
    }
 
    public static void main(String arr[])
    {
        new LoginWindow();
    }

    //variabilele
    private JLabel l1;
    private JLabel l2;
    private JLabel l3;
    private JTextField tf1;
    private JButton btn1;
    private JPasswordField p1;
    static int banca;
    static String nume;
    
}