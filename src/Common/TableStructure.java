/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.io.Serializable;
import java.util.ArrayList;
import main.Card;

/**
 *
 * @author AsX
 */
public class TableStructure implements  Serializable{
    private ArrayList<Player> players;
    private ArrayList<Card> dealerCards;
    private Player currentPlayer;
    
    public TableStructure(){
        players = new ArrayList<>();
        dealerCards = new ArrayList<>();
    }
    
    public ArrayList<Player> getPlayers(){
        return players;
    }
    
    public void clearPlayerCards(){
        for(int i=0;i<players.size();i++){
            players.get(i).getPack().clear();
        }
    }
    public void setPlayers(ArrayList <Player> newPlayers){
        players.clear();
        players = newPlayers;
    }
    

    /**
     * @return the dealerCards
     */
    public ArrayList<Card> getDealerCards() {
        return dealerCards;
    }

    /**
     * @param dealerCards the dealerCards to set
     */
    public void setDealerCards(ArrayList<Card> dealerCards) {
        this.dealerCards = dealerCards;
    }

    /**
     * @return the currentPlayer
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @param currentPlayer the currentPlayer to set
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
