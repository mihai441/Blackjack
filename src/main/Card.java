/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.Serializable;

/**
 *
 * @author User
 */
public class Card implements Serializable{

    public enum Tip  {
    Trefla,
    Romb,
    Inima,
    Frunza;
    }
             
    private final Tip tip;
    private  int valoare;
    public boolean faceup;
       
    public Card(Tip tip, int valoare){
        if(valoare <1 ||  valoare >14){
            System.exit(1);
        }
        this.valoare=valoare;
        this.tip=tip;
        this.faceup=true;
    }

    public int getVal(){
        return this.valoare;
    }

    public void setVal(int i){
        this.valoare = i;
    }

    public boolean isFaceUp(){
        return this.faceup;
    }

    public Tip getSuit(){
        return this.tip;
    }

    @Override
   public String toString(){

       String mesaj = "valoare";

       switch(this.valoare){
           case(1):
              mesaj = "As(1)";
              break;
           case(2):
              mesaj = "Doi";
              break;
           case(3):
               mesaj = "Trei";
               break;
           case(4):
               mesaj = "Patru";
               break;
           case(5):
               mesaj = "Cinci";
               break;
           case(6):
               mesaj = "Sase";
               break;
           case(7):
               mesaj = "Sapte";
               break;    
           case(8):
               mesaj = "Opt";
               break;
           case(9):
               mesaj = "Noua";
               break;
           case(10):
               mesaj = "Zece";
               break;   
           case(11):
               mesaj = "Valet";
               break;
           case(12):
               mesaj = "Dama";
               break;
           case(13):
               mesaj = "Popa";
               break;
           case(14):
               mesaj = "As(11)";
               break;    
       }
       return mesaj + " de " + tip.toString();
   }

   public void flip(){
       faceup = !faceup;
   }
}
