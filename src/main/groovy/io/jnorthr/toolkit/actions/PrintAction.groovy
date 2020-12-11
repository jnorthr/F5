package io.jnorthr.toolkit.actions;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import io.jnorthr.toolkit.F5Data;

/*
 * Feature to do a full screen print as an Action 
 */
public class PrintAction extends AbstractAction
{
  F5Data f5data;

  public PrintAction(String name, ImageIcon icon, String shortDescription, Integer mnemonic, F5Data data)
  {
    super(name, icon);
	f5data = data;
    putValue(SHORT_DESCRIPTION, shortDescription);
    putValue(MNEMONIC_KEY, mnemonic);
  }

  public PrintAction(String shortDescription, Integer mnemonic, F5Data data)
  {
	f5data = data;
    putValue(SHORT_DESCRIPTION, shortDescription);
    putValue(MNEMONIC_KEY, mnemonic);
  }

  public void actionPerformed(ActionEvent e)
  {
    def fn = System.getProperty("output.file");
    String currentDirectory  = System.getProperty("user.dir") + File.separator;
    if ( fn == null ) { fn = "${currentDirectory}screenprint.png" }
    f5data.frame.setTitle("Screenprint sent to ${fn}.");

    def size = Toolkit.getDefaultToolkit().getScreenSize();
    java.awt.Rectangle pic = new Rectangle(size);
    
    ImageIO.write(new Robot().createScreenCapture(pic),"png", new File(fn));  
  } // end of actionPerfiormed method

} // end of class
