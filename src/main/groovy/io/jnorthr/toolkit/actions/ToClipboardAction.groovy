package io.jnorthr.toolkit.actions;

import io.jnorthr.toolkit.Copier;
import io.jnorthr.toolkit.IO;
import io.jnorthr.toolkit.TemplateMaker;
import io.jnorthr.toolkit.Mapper;
import io.jnorthr.toolkit.F5GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Feature to create an Action to copy text to Syetem Clipboard 
 */
public class ToClipboardAction extends AbstractAction
{
	String key = "";
	IO io = new IO(true);

	public ToClipboardAction(String name, ImageIcon icon, String shortDescription, Integer mnemonic)
	{
		super(name, icon);

		println ("... ToClipboardAction: name='${name}' shortDescription='${shortDescription}' mnemonic='${mnemonic}' ...");
		putValue(SHORT_DESCRIPTION, shortDescription);
		putValue(MNEMONIC_KEY, mnemonic);
		key = name;
	}


	/**
	* here's logic to copy payload text to clipboard for one function key that's been presssed 
	*/
	public void actionPerformed(ActionEvent evt) 
	{
		println ("\n${key} myAction run when ${key} function key pressed ...");
		// ok, get xxx.txt text content, do any replacements using groovy template engine

		/** a handle to our IO module to do read/write stuff */
		io.reset();
		io.setFunctionKey(key)

		/** a handle to our IO module to do read/write stuff */
		F5GUI f5gui = new F5GUI();

		String tx = io.getPayload();
		if (tx.length() > 0)
		{
			// find the ${} parms
			Mapper ma = new Mapper();
			Map map = ma.getMap(tx);
			tx = ma.getTemplate(map);

			// time to put fully translated text string onto System Clipboard
			Copier ck = new Copier();
			ck.paste(tx);
			f5gui.title = "F5 -> ${key} copied ${tx.length()} bytes to Clipboard"; 
		} // end of if

		/** when no Fxx.txt file found in user home folder or it's empty, logic comes here */
		else
		{
			if (key!="A") 
			{ 
				f5gui.title = "F5 -> ${key} function key has no text for Clipboard"; 
				SwingUtilities.invokeLater(new Runnable() 
				{
					public void run()
					{
						println "... myAction TemplateMaker running..."
						TemplateMaker obj = new TemplateMaker(key); 
						f5gui.tooltip = io.getToolTip();
						println "... ${key} function key has no text for Clipboard; tooltip=|${f5gui.tooltip}|"
						if (f5gui.tooltip.trim().size() > 0)
						{
							map[key] = tooltip;
							f5gui.title = "F5 -> ${key} function key built text for ${f5gui.tooltip}";  
							mybutton.setForeground(Color.BLUE);
							mybutton.setFont(new Font("Arial", Font.BOLD, 12)); 
						}  // end of if
					} // end of run
				});
			} // end of if					
			else
			{
				f5gui.title = "F5 Utility"; 
			} // end of else
		} // end of else
	} // end of ActionPerformed

} // end of class