/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import Common.Player;
import java.util.ArrayList;

/**
 *
 * @author AsX
 */
public class Dealer {
     
    public void getCarteDealer(Pack pachet, ArrayList<Card> cards, boolean faceUp){
        pachet.get(0).faceup = faceUp;
        cards.add(pachet.get(0));
        pachet.remove(0);
    }
    
    public void getCartePlayer(Pack pachet, Player player){
        player.addCardFaceUp(pachet.get(0));
        pachet.remove(0); 
    }
    
       public int getValue(ArrayList<Card> carti){
        int suma=0;
        for(int i=0;i<carti.size();i++){
           if(carti.get(i).isFaceUp()){
            switch(carti.get(i).getVal()){
                case 1:
                    suma+=1;
                    break;
                case 2:
                    suma+=2;
                    break;
                case 3:
                    suma+=3;
                    break;
                case 4:
                    suma+=4;
                    break;
                case 5:
                    suma+=5;
                    break;
                case 6:
                    suma+=6;
                    break;
                case 7:
                    suma+=7;
                    break;
                case 8:
                    suma+=8;
                    break;
                case 9:
                    suma+=9;
                    break;
                case 10:
                    suma+=10;
                    break;
                case 11:
                    suma+=10;
                    break;
                case 12:
                    suma+=10;
                    break; 
                case 13:
                    suma+=10;
                    break;
                case 14:
                    suma+=11;
                    break;    
            }
         }
        }
    return suma;
       }
       
    public int calculareValoare(ArrayList<Card> carti){
        if (getValue(carti)>21){
        for(int i=0;i<carti.size();i++)
            if (carti.get(i).getVal()==14){
                carti.get(i).setVal(1);
                if(getValue(carti)<21)
                    break;
            }
        }
    return getValue(carti);
    }
       
//     public void startingHands(){
//         getCartePlayer(pachet, cartiPlayer);
//         getCartePC(pachet,cartiPC,0,false);
//         getCartePlayer(pachet,cartiPlayer,1);
//         getCartePC(pachet,cartiPC,1,true);
//     }
     
         
     public boolean bjack(ArrayList<Card> carti){
         return calculareValoare(carti)==21;
}

}
    
