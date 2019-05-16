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
public interface ITheme {
    public ITheme getTheme();
    public Font getInfoFont();
    public Font getButtonFont();
    public String getName();
    public ImageIcon getBackground();
}
