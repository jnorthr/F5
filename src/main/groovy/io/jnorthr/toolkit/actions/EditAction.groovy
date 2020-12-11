package io.jnorthr.toolkit.actions;

import io.jnorthr.toolkit.Copier;
import io.jnorthr.toolkit.TemplateGUI;
import io.jnorthr.toolkit.Mapper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/*
 * Feature to create an Action to edit a payload of text for a function key
 */
public class EditAction extends AbstractAction
{
	public String title = "";
	public String key = "F13"; 
	public void setKey(String x3) { key = x3; }

	// constructor
	public EditAction(String name, ImageIcon icon, String shortDescription, Integer mnemonic)
	{
	    super(name, icon);
	    putValue(SHORT_DESCRIPTION, shortDescription);
	    putValue(MNEMONIC_KEY, mnemonic);
        key = name; 
	    //println "... EditAction (String ${name}, ImageIcon icon, String ${shortDescription}, Integer ${mnemonic})"
	} // end of method

	public void actionPerformed(ActionEvent evt) 
	{
		Object source = evt.getSource();
        JButton btn = (JButton)source;
        String tx = btn.getText();
		key = tx;
		//println("\nEditAction run when function key |${tx}| was pressed ...="+evt.toString());
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run()
			{
				/**
				*	Here we show dialog to allow user to revise text for this function key, then stored in
				*	file named after function key plus .txt suffix. So function key four would store text in
				*	file named F4.txt typicall in the /copybooks folder of ther user'e home directory.
				*/
				TemplateGUI obj = new TemplateGUI(tx);
			} // end of run
		}); // end of invoke					
	} // end of ActionPerformed

} // end of class
