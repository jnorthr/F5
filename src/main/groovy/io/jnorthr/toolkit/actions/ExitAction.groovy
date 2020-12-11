package io.jnorthr.toolkit.actions;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Feature to create an Action 
 */
public class ExitAction extends AbstractAction
{
  public ExitAction(String name, ImageIcon icon, String shortDescription, Integer mnemonic)
  {
    super(name, icon);
    putValue(SHORT_DESCRIPTION, shortDescription);
    putValue(MNEMONIC_KEY, mnemonic);
  }

  public ExitAction(String shortDescription, Integer mnemonic)
  {
    putValue(SHORT_DESCRIPTION, shortDescription);
    putValue(MNEMONIC_KEY, mnemonic);
  }

  public void actionPerformed(ActionEvent e)
  {
    System.exit(0);
  }
}
