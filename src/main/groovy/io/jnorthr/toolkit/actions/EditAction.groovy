package io.jnorthr.toolkit.actions;

import io.jnorthr.toolkit.Copier;
import io.jnorthr.toolkit.TemplateMaker;
import io.jnorthr.toolkit.Mapper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Feature to create an Action 
 */

/**
*	Here we show dialog to allow user to revise text for this function key, then stored in
*	file named after function key plus .txt suffix. So function key four would store text in
*	file named F4.txt
*/
public class EditAction extends AbstractAction
{
	public String title = "";
	public String key = "F13";

	// constructor
	public EditAction(String name, ImageIcon icon, String shortDescription, Integer mnemonic)
	{
	    super(name, icon);
	    putValue(SHORT_DESCRIPTION, shortDescription);
	    putValue(MNEMONIC_KEY, mnemonic);
        key = name;
	} // end of method

	public void actionPerformed(ActionEvent evt) 
	{
		print("\nAction run when CTRL+VK_${key} function key WAS pressed ...");

        String tx = io.getPayload(key);
       	if (tx.length() > 0)
       	{
			title = "F5 -> ${key} function key has ${tx.length()} bytes for ${tooltips[key]}"; 
			SwingUtilities.invokeLater(new Runnable() 
			{
				public void run()
				{
      					println "... Action TemplateMaker"
					TemplateMaker obj = new TemplateMaker(key, tx);                    
					tooltip = io.getToolTip(key);
					if (tooltip.trim().size() > 0)
					{
					    	tooltips[key] = tooltip;
						title = "F5 -> ${key} function key built text for ${tooltip}"; 
						mybutton.setForeground(Color.BLUE);
						mybutton.setFont(new Font("Arial", Font.BOLD, 10)); 
					}  // end of if
				} // end of run
			}); // end of invoke					
       	} // end of if
      	else
       	{
        	if (key!="A")
        	{
				title = "F5 -> ${key} function key requires text to ";  
				SwingUtilities.invokeLater(new Runnable() 
    			{
      				public void run()
      				{
      					// write new payload for this key
	      				println "... Action TemplateMaker key!=A"
						TemplateMaker obj = new TemplateMaker(key, true); 
						updatekey = key;                   
						title = "F5 -> ${key} - you need to  text";  
      				} // end of run
	 			}); // end of invoke
    		} // end of if
    							
		} // end of else
	} // end of ActionPerformed

} // end of class