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
import javax.swing.JFrame;
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
 * @author james.northrop@orange.fr
 *
 */
public class F5
{
	/** a handle to our IO module to do read/write stuff */
	IO io;

	/** a handle to our global variables to do read/write stuff */
	F5Data f5data = new F5Data();

	/** a handle to our GUI module to track global GUI stuff */
	F5GUI f5gui = new F5GUI();

	/** If we need to println audit log to work, this will be true */ 
    	boolean audit = true;
    
	/** a handle to generic JButton builder for one Function Key  */
	ButtonMaker buttonMaker;

    /**
     * A method to print an audit log if audit flag is true
     *
     * @param  is text to show user via println
     * @return void
     */
    public void say(String text)
    {
        if (audit) { println text; }
    } // end of method


    /**
     * A method to get known tooltip text from external file named after a function key; so 
     * tooltip text for help in F2.txt is for the F2 function key and also holds payload; or
     * F11.txt holds tip for F11 function key while it may/maynot have the text we copy to 
     * clipboard if/when F11 key is pressed (or clicked).
     *
     * Note that these simple .txt files are made in the TemplateMaker class that are found in user home directory
     * plus folder picked in the Chooser dialog plus F1 plus .txt giving /Users/jim/myfolder/F1.txt file name
     *
     * @return map of known tooltips per function key
     */
    public Map getAvailableTooltips()
    {
    	//tooltips = [:]
    	f5data.tooltips["ESC"] = "ESC key ends this app";
	io = new IO(true);

	(1..12).eachWithIndex{ num, ix -> 
		String ky = "F${ix+1}";
		println "... getAvailableTooltips for ky=|F${ix+1}|" 

	        /*
	        * takes only simple key name like F3 or F3.txt to find a filename like /Users/jim/copybooks/F3.txt
		*/
		io.setFunctionKey(ky);
	        String tx = io.getPayload(); 

		// remember which function key has boilerplate file by setting it to true or false flag set in IO module
		f5data.hasPayload[ky]=io.present;

	        // keep the text content of file just read in internal array
        	f5data.payloads[ky]=tx;
		println "... getAvailableTooltips for f5data.payloads=|${tx}|" 

        	// load function key io.tooltip value loaded in io.getPayload
		//f5gui.tooltip =   //io.getToolTip(tx);
	        
	    	f5data.tooltips[ky] = io.getToolTip();		
	} // end of each

    	return f5data.tooltips;
    } // end of method


	/**
     	* Utility method to reset a single JButton after it's title and payload were updated.
     	*
     	* @return void - no response 
     	*/
	public void cleanup(String updatekey) 
	{
		if (updatekey!=null)
		{
			f5gui.tooltip = io.getToolTip();
			if (f5gui.tooltip.trim().size() > 0)
			{
			    	f5data.tooltips[updatekey] = f5gui.tooltip;
			    	if (f5data.buttons[updatekey])
			    	{
					say("F5 -> ${updatekey} function key built text for tooltip: ${f5gui.tooltip}"); 
				}

				f5data.buttons[updatekey].setForeground(Color.BLUE);
				f5data.buttons[updatekey].setFont(new Font("Arial", Font.BOLD, 10)); 
				updatekey=null;
			} // end of if
		}  // end of if
	} // end of cleanup


	/**
	* Default constructor to build a tool to support function key usage to paste text onto the System clipboard
	*/
	public F5() 
	{
		f5data.tooltips = getAvailableTooltips();
		buttonMaker = new ButtonMaker(f5data)
println "\n\n... F5() constructor before makeQuitButton()"			 
		buttonMaker.quitbutton = buttonMaker.makeQuitButton();
		JButton b;
		
		(1..2).eachWithIndex{ num, ix -> 
			String tx = "F${num}";
println "\n\n... F5() constructor before makeButton(${tx})"			 
			b = buttonMaker.makeButton(tx);
println "... F5() constructor before makeButton(${tx})\n\n"			 

			if (f5data.hasPayload[tx]) 
			{
				b.setToolTipText( f5data.tooltips[tx] );
			}
			
			say "... F5 constructor adding button ${num}+${ix}="+f5data.tooltips[tx];
			f5gui.add(b);
			f5data.buttons[tx] = b;
			cleanup(tx);
		} // end of each


		// build the Arrow button to move gui to bottom orleftor right edge of hardware screen
		b = buttonMaker.makeButtonA();
		f5gui.add(b);
		def q = buttonMaker.quitbutton; 
		f5gui.add(q);
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
				F5 f5data = new F5();
			}
		}
		);

		println "--- the end of F5 ---"
	} // end of main

} // end of class
