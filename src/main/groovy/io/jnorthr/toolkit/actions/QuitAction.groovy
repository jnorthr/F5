package io.jnorthr.toolkit.actions;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Feature to create an Action to dispose of the current JOptionPane dialog 
 */
public class QuitAction extends AbstractAction
{
  JFrame f;

  public QuitAction(String name, ImageIcon icon, String shortDescription, Integer mnemonic)
  {
    super(name, icon);
    putValue(SHORT_DESCRIPTION, shortDescription);
    putValue(MNEMONIC_KEY, mnemonic);
  }

  public QuitAction(String shortDescription, Integer mnemonic)
  {
    putValue(SHORT_DESCRIPTION, shortDescription);
    putValue(MNEMONIC_KEY, mnemonic);
  }

  public void actionPerformed(ActionEvent e)
  { //println "... QuitAction clicked";
    f.dispose();
  }

  public void setFrame(JFrame e)
  {
    f = e;
  }
}
