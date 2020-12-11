package io.jnorthr.toolkit.actions;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Feature to create an Action to dispose of the current JOptionPane dialog 
 */
public class ClearAction extends AbstractAction
{
  JFrame f;

  public ClearAction(String name, ImageIcon icon, String shortDescription, Integer mnemonic)
  {
    super(name, icon);
    putValue(SHORT_DESCRIPTION, shortDescription);
    putValue(MNEMONIC_KEY, mnemonic);
  }

  public ClearAction(String shortDescription, Integer mnemonic)
  {
    putValue(SHORT_DESCRIPTION, shortDescription);
    putValue(MNEMONIC_KEY, mnemonic);
  }

  public void actionPerformed(ActionEvent e)
  {
	functionField.setText("");
	tooltip.setText("");
	area.setText("");
	//println "--- ClearAction hit actionPerformed(ActionEvent ${e})"
  }

  public void setFrame(JFrame e)
  {
    f = e;
  }
}
