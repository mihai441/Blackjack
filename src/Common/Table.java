/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

/**
 *
 * @author AsX
 */
public class Table implements java.io.Serializable {
    private int id;
    private int numOfPlayers;
    private String adress;

    public Table(int id, int numOfPlayers, String adress, int port, boolean available) {
        this.id = id;
        this.numOfPlayers = numOfPlayers;
        this.adress = adress;
        this.port = port;
        this.available = available;
    }
    
    
    public String getAdress() {
        return adress;
    }

    public int getPort() {
        return port;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    private int port;
    private boolean available;

    public int getId() {
        return id;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }
}
