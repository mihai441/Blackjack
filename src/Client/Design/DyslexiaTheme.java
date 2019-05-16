/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Design;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


/**
 *
 * @author alexandruf
 */
public class DyslexiaTheme implements ITheme {
    
    public DyslexiaTheme(){
        this.setName("Dyslexia Theme");
        
        String s = new File(".").getAbsolutePath();
        try{
            Font customFont = Font.createFont(Font.TRUETYPE_FONT,new FileInputStream(new File("src/Client/Design/Fonts/OpenDyslexic.ttf"))).deriveFont(Font.PLAIN,13);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            this.setInfoFont(customFont);
        }
        catch(Exception ex){
             JOptionPane.showMessageDialog(null, "The font of this theme was not found. A default one will be used!");
             this.setInfoFont(new Font("Serif", Font.BOLD,16));
        };
        try{ 
            Font customFont = Font.createFont(Font.TRUETYPE_FONT,new FileInputStream(new File("src/Client/Design/Fonts/OpenDyslexic.ttf"))).deriveFont(20f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
            this.setButtonFont(customFont);
        }
        catch(Exception ex){
                this.setButtonFont(new java.awt.Font("Verdana", 1, 20));
        }
        try{
            setBackground(new javax.swing.ImageIcon(getClass().getResource("/main/Assets/bgDyslexicTheme.jpg")));
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, "The background of this theme was not found. A default one will be used!");
            setBackground(new javax.swing.ImageIcon(getClass().getResource("/main/Assets/bg.png")));
        }
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
        return name;
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
        return infoFont;
    }

    @Override
    public Font getButtonFont() {
        return buttonFont;
    }

    @Override
    public ImageIcon getBackground() {
        return this.background;
    }
    
    
}
