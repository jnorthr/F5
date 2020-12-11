package io.jnorthr.toolkit.actions;

import io.jnorthr.toolkit.Copier;
import io.jnorthr.toolkit.IO;
import io.jnorthr.toolkit.TemplateGUI;
import io.jnorthr.toolkit.Mapper;
import io.jnorthr.toolkit.F5Data;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Feature to create an Action to copy text to Syetem Clipboard 
 */
public class ToClipboardAction extends AbstractAction
{
	String key = "";
	IO io = new IO();
    F5Data f5data;
	
	public ToClipboardAction(String name, ImageIcon icon, String shortDescription, Integer mnemonic, F5Data data)
	{
		super(name, icon);
		f5data = data;

		//println ("... ToClipboardAction.constructor: name='${name}' shortDescription='${shortDescription}' mnemonic='${mnemonic}' ...");
		putValue(SHORT_DESCRIPTION, shortDescription);
		putValue(MNEMONIC_KEY, mnemonic);
		key = name;
	}


	/**
	* here's logic to copy payload text to clipboard for one function key that's been presssed 
	*/
	public void actionPerformed(ActionEvent evt) 
	{
		//println ("\n${key} myAction run when ${key} function key pressed ...");
		// ok, get xxx.txt text content, do any replacements using groovy template engine

		/** a handle to our IO module to do read/write stuff */
		io.reset(key);
		io.setFunctionKey(key)

		if (io.chkobj(key))
		{
			String tx = io.getPayload(key);
			// find the ${} parms
			Mapper ma = new Mapper();
			Map map = ma.getMap(tx);
			tx = ma.getTemplate(map);

			// time to put fully translated text string onto System Clipboard
			Copier ck = new Copier();
			ck.paste(tx);
			f5data.frame.setTitle("F5 Tool - copied function key ${key} text to System Clipboard of ${tx.length()} bytes"); 
		} // end of if

		/** when no Fxx.txt file found in user home folder or it's empty, logic comes here */
		else
		{
			f5data.frame.setTitle("F5 -> ${key} function key has no text for Clipboard"); 
			SwingUtilities.invokeLater(new Runnable() 
			{
				public void run()
				{
					//println "... ToClipboardAction calls TemplateGUI running to build text file for ${key} ..."
					TemplateGUI obj = new TemplateGUI(key); 
				} // end of run
			});
		} // end of else
	} // end of ActionPerformed

} // end of class
