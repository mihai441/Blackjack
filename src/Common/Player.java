/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Common;

import java.io.Serializable;
import java.util.ArrayList;
import main.Card;
import main.Pack;

/**
 *
 * @author AsX
 */
public class Player implements Serializable {
    
    private final int id;
    private final String name;
    private int tableSlot;
    private int stake;
    private int credit;
    private ArrayList<Card> pack;

    public Player(int id, String name, int credit) {
        stake = 0;
        this.id = id;
        this.credit = credit;
        this.name = name;
        this.pack = new ArrayList<>();
    }
    

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Card> getPack(){
        return pack;
    }
    
    public void addCard(Card card) {
        pack.add(card);
    }
       
    public void addCardFaceUp(Card card) {
        card.faceup = true;
        pack.add(card);
    }
    
        public void addCardFaceDown(Card card) {
        card.faceup = false;
        pack.add(card);
    }


    public void setPack(ArrayList<Card> pack) {
        this.pack = pack;
    }

    /**
     * @return the tableSlot
     */
    public int getTableSlot() {
        return tableSlot;
    }

    /**
     * @param tableSlot the tableSlot to set
     */
    public void setTableSlot(int tableSlot) {
        this.tableSlot = tableSlot;
    }

    /**
     * @return the stake
     */
    public int getStake() {
        return stake;
    }

    /**
     * @param stake the stake to set
     */
    public void setStake(int stake) {
        this.stake = stake;
    }

    /**
     * @return the credit
     */
    public int getCredit() {
        return credit;
    }

    /**
     * @param credit the credit to set
     */
    public void setCredit(int credit) {
        this.credit = credit;
    }
}
