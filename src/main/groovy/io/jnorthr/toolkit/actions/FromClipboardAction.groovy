package io.jnorthr.toolkit.actions;

import io.jnorthr.toolkit.Copier;
import io.jnorthr.toolkit.IO;
import io.jnorthr.toolkit.TemplateGUI;
import io.jnorthr.toolkit.Mapper;
import io.jnorthr.toolkit.F5GUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*
 * Feature to create an Action to copy System Clipboard content into the text file that belongs to this function key 
 */
public class FromClipboardAction extends AbstractAction
{
	String key = "";
	IO io = new IO();

	public FromClipboardAction(String name, ImageIcon icon, String shortDescription, Integer mnemonic)
	{
		super(name, icon);
		println ("... FromClipboardAction: name='${name}' shortDescription='${shortDescription}' mnemonic='${mnemonic}' ...");
		putValue(SHORT_DESCRIPTION, shortDescription);
		putValue(MNEMONIC_KEY, mnemonic);
		key = name;
	} // end of constructor

	/**
	* Here's logic to copy payload text from clipboard into payload file for one function key that's been presssed 
	*/
	public void actionPerformed(ActionEvent evt) 
	{
		/** a handle to our IO module to do read/write stuff */
		io.reset();
		io.setFunctionKey(key)
		String tx = "";

		// time to get text string from System Clipboard
		Copier ck = new Copier();
		tx = ck.copy();
		println ("\n${key} function key found ${tx.size()} bytes of text ...");
		io.write(tx);
	} // end of ActionPerformed

} // end of class
