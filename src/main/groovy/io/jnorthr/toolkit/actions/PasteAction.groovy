package io.jnorthr.toolkit.actions;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Feature to create an Action 
 */
  public class PasteAction extends AbstractAction
  {
    public PasteAction(String name, ImageIcon icon, String shortDescription, Integer mnemonic)
    {
      super(name, icon);
      putValue(SHORT_DESCRIPTION, shortDescription);
      putValue(MNEMONIC_KEY, mnemonic);
    }

    public void actionPerformed(ActionEvent e)
    {
      JOptionPane.showMessageDialog(null, "Would have done the 'Paste' action.");
    }
  }

