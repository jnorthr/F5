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
	/** a handle to our global variables to do read/write stuff */
	F5Data f5data = new F5Data();

	/** a handle to our GUI module to track global GUI stuff - this is your JFrame ! */
	F5GUI f5gui = new F5GUI(f5data);

	/** If we need to println audit log to work, this will be true */
    boolean audit = true;

	/** a handle to generic JButton builder for one Function Key  */
//	ButtonMaker buttonMaker;

    /**
     * A method to print an audit log if audit flag is true
     *
     * @param  text to show user via println
     * @return void
     */
    public void say(String text)
    {
        if (audit) { println text; }
    } // end of method


	/**
	* Default constructor to build a tool to support function key usage to paste stuff onto the System clipboard
	*/
	public F5()
	{
//		buttonMaker = new ButtonMaker(f5data)
		say "\n\n... F5() constructor before makeQuitButton()";
//		buttonMaker.quitbutton = buttonMaker.makeQuitButton();
		f5data.tooltips = f5gui.getAvailableTooltips();
		
		JButton b;

		(1..12).eachWithIndex{ num, ix ->
			String tx = "F${num}";
			say "\n\n... F5() constructor before makeButton(${tx})";
			b = new JButton(tx);
			//b = buttonMaker.makeButton(tx);
			say "... F5() constructor after makeButton(${tx})\n\n"

			if (f5data.hasPayload[tx])
			{
				b.setToolTipText( f5data.tooltips[tx] );
			}
			else
			{
				b.setToolTipText("${tx} has no Payload");	
			}

			say "... F5 constructor adding button ${num}+${ix}="+f5data.tooltips[tx];
			f5gui.add(b);
			f5data.buttons[tx] = b;
			f5gui.cleanup(tx);
			say " ";
		} // end of each

/*
		// the Screen Print button
		b = buttonMaker.printbutton;
		f5gui.add(b);

		// build the Arrow button to move gui to bottom or left or right edge of hardware screen
		b = buttonMaker.arrowbutton;    //makeButtonA();
		f5gui.add(b);

		// the ESC button
		b = buttonMaker.quitbutton;
		f5gui.add(b);
*/

		f5gui.setVisible(true);
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
	public static void main(String[] args) {

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
