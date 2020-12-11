package io.jnorthr.toolkit;
  
import io.jnorthr.toolkit.Copier;
import io.jnorthr.toolkit.F5Data;
import io.jnorthr.toolkit.F5GUI;
import io.jnorthr.toolkit.IO;
import io.jnorthr.toolkit.Mapper;
 
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

import java.io.*;

import javax.swing.border.LineBorder;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
//import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.*;
import javax.swing.JFrame;

/**
 * This Swing program demonstrates function key usage from a keyboard and/or clickable Jbutton.
 *
 * https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html for more on keystrokes
 *
 * @author james.b.northrop@googlemail.com
 *
 */
public class F5
{
	JFrame frame = new JFrame("F5 Utility - to copy function key text to System Clipboard");

	/** a handle to our global variables to do read/write stuff */
	F5Data f5data;

	/** a handle to our GUI module to track global GUI stuff - this is your JFrame ! */
	F5GUI f5gui;


	/**
	* Default constructor to build a tool to support function key usage to paste stuff onto the System clipboard
	*/
	public F5()
	{
		f5data = new F5Data();
		f5data.setFrame(frame);
		f5gui = new F5GUI(f5data);
	} // end of constructor


    // =============================================================================
    /**
      * The primary method to execute this class. Can be used to test and examine logic and performance issues.
      *
      * argument is a list of strings provided as command-line parameters.
      *
      * @param  args a list of possibly zero entries from the command line; first arg[0] if present, is
      *         taken as a simple file name of a groovy script-structured configuration file;
    */
	public static void main(String[] args) 
	{
		SwingUtilities.invokeAndWait(new Runnable()
		{
			@Override
			public void run()
			{
				F5 f5object = new F5();
			}
		}
		);

		println "--- the end of F5 ---"
	} // end of main

} // end of class
