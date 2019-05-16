/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Helpers;

import com.sun.javafx.binding.SelectBinding;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author AsX
 */
public class SoundManager {
    
    public SoundManager(){
        loadSounds();
    }
    
    private void loadSounds(){
        loadButtonClip();
    }
    private volatile Clip buttonClip;
    
    private void loadButtonClip(){
            try { 
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("/Client/Sound/ButtonSound.wav"));
                buttonClip = AudioSystem.getClip();
                buttonClip.open(audioInputStream);
            } catch (UnsupportedAudioFileException ex) {
            Logger.getLogger(SoundManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SoundManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(SoundManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void playButtonClip(){
        if(buttonClip.isRunning()){
            buttonClip.stop();
        }
        Thread play = new Thread(){
            @Override
              public void run() {
                    buttonClip.start();
                    buttonClip.setMicrosecondPosition(0);
                }
        };
        play.start();
    }
}
