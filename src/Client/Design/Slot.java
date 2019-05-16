/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Design;

import java.util.ArrayList;
import javax.swing.JLabel;
import main.Card;

/**
 *
 * @author AsX
 */
public class Slot {
    
    private JLabel name;
    private JLabel cardValue;
    private ArrayList<JLabel> cards = new ArrayList<>();

        
    public Slot(){
        cards = new ArrayList<>();
        name = new JLabel();
        cardValue = new JLabel();
    }


    public void addCard(JLabel card) {
        this.cards.add(card);
    }
    
    public ArrayList<JLabel> getCards(){
        return cards;
    }
    
    public JLabel getCard(int i){
        return cards.get(i);
    }

    public void setName(String text) {
        name.setText(text);
    }
    
    public JLabel getName(){
        return name;
    }


    public void setCardValue(int value) {
        cardValue.setText(Integer.toString(value));
    }
    
    
    public void setCardValue(String value) {
        cardValue.setText(value);
    }
    
    public JLabel getCardValue(){
        return cardValue;
    }

}
