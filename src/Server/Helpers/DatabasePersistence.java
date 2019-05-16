/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server.Helpers;

import Common.MainServerUser;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AsX
 */
public class DatabasePersistence {
    
    private Connection connection;
    
    public DatabasePersistence(){
     connectToDatabase();
    }
    
    private void connectToDatabase(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/blackjack","root","");
        } catch (SQLException ex) {
            Logger.getLogger(DatabasePersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public MainServerUser checkLoginData(String username, String password){
            
        try {
            String hashedPassword = hashPassword(password,"SHA-256");
            
            PreparedStatement ps = connection.prepareStatement("select * from users where nume=? and parola=?");
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                MainServerUser user = new MainServerUser();
                user.setCredit(rs.getInt("credit"));
                user.setName(rs.getString("nume"));
                user.setId(rs.getInt("id"));
                return user;
            } 
        } catch (SQLException ex) {
            Logger.getLogger(DatabasePersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
        
    private String hashPassword(String password, String alg){
        try {
            MessageDigest messageDigester = MessageDigest.getInstance(alg);
            byte[] hash = messageDigester.digest(password.getBytes(StandardCharsets.UTF_8));
            String hashedPassword = Base64.getEncoder().encodeToString(hash);
            return hashedPassword;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DatabasePersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void updateUserCredit(int credit,int id){
        try {
            PreparedStatement ps = connection.prepareStatement("update users SET credit = ? where id=?");
            ps.setInt(1, credit);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabasePersistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}