/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Design;

import java.awt.Font;
import javax.swing.ImageIcon;

/**
 *
 * @author alexandruf
 */
public class DefaultTheme implements ITheme{

    public DefaultTheme(){
        this.setName("Default Theme");
        this.setInfoFont(new Font("Serif", Font.BOLD,16));
        this.setButtonFont((new java.awt.Font("Verdana", 1, 20)));
        setBackground(new javax.swing.ImageIcon(getClass().getResource("/main/Assets/bg.png")));
    }
    
    private String name;
    private Font infoFont;
    private Font buttonFont;
    private ImageIcon background;
    
    
    @Override
    public ITheme getTheme() {
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }
    
        private void setName(String name){
        this.name = name;
    }
    
    private  void setInfoFont(Font font){
        this.infoFont = font;
    }
    
    private  void setButtonFont(Font font){
        this.buttonFont = font;
    }
    
    private void setBackground(ImageIcon background){
        this.background = background;
    }

    @Override
    public Font getInfoFont() {
        return this.infoFont;
    }

    @Override
    public Font getButtonFont() {
        return this.buttonFont;
    }

    @Override
    public ImageIcon getBackground() {
        return this.background;
    }
    
}
