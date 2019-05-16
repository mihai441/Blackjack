/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author User
 */
public class Pack implements Serializable{
    


 long seed = System.nanoTime();
 public ArrayList<Card> pachet;
 private final int nrpachete = 3;
 
 public Pack (){
    pachet = new ArrayList<>();
    for(int b=0;b<3;b++){
     for (int i=2;i<=14;i++){
       for(int j=0;j<4;j++){
           pachet.add(new Card(Card.Tip.values()[j], i));
       }
     }
    }
 Collections.shuffle(pachet, new Random(seed));
 } 
 
 public Pack(int numOfCards){
     pachet = new ArrayList<>();
 }
public void afisare(){
System.out.println(pachet.toString());
}
public void clear(){
    this.pachet.clear();
    }
public int size(){
    return this.pachet.size();
}

public Card get(int i){
    if(pachet.size() > i){
        return pachet.get(i);
    }
    return null;
}

public void remove(int i){
    if(pachet.size() > i){
        pachet.remove(i);
    }
}

}

