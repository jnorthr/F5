package io.jnorthr.toolkit;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.*;
import java.awt.Dimension;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.*;

/**
 * This Swing program demonstrates function key usage from a keyboard and/or clickable Jbutton.
 *
 * https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html for more on keystrokes
 *
 * @author james.b.northrop@googlemail.com
 *
 */
public class F5GUI extends JFrame 
{
	/** string holding the title to be displayed as part of GUI panel top line
	*/
	String title = "";

	/** a handle to temp storage for an in-progress Function Key  */
	String tooltip = "";
    
	/** a handle to temp storage for just changed Function Key  */
	String updatekey = null;

	/** If we need to println audit log to work, this will be true */ 
    boolean audit = true;

	/** a handle to our IO module to do read/write stuff */
	IO io = new IO();

	/** a handle to our global variables to do read/write stuff */
	F5Data f5data;

	/** a handle to our logic to manage the on-screen location of our F5 dialog */
	PositionLogic pl;

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
     * A method to change the dialog title to this new value
     *
     * @param  is text to show user in dialog title line
     * @return void
     */
    public void setHeading(String text)
    {
    	say "... F5GUI.setHeading($text)";
        this.setTitle(text); 
    } // end of method


	// includes a button onto this JFrame's content pane
	public void add(JButton b)
	{
		super.add(b);
		say "... F5GUI.add"+b.toString();
	} // end of add

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
    	say "... getAvailableTooltips() starting --"
    	f5data.tooltips["ESC"] = "ESC key ends this app";
    	f5data.tooltips["PRINTSCREEN"] = "This key makes full screen copy in screenprint.png";

		(1..12).eachWithIndex{ num, ix ->
			String ky = "F${ix+1}";
			say "... getAvailableTooltips for ky=|F${ix+1}|"

	        /*
	        * takes only simple key name like F3 or F3.txt to find a filename like /Users/jim/copybooks/F3.txt
			*/
			io.setFunctionKey(ky);
	        String tx = io.getPayload(ky);

			// remember which function key has boilerplate file by setting it to true or false flag set in IO module
			f5data.hasPayload[ky] = io.pf.hasFunctionKeyFileName(ky);  //io.present;

	        // keep the text content of file just read in internal array
        	f5data.payloads[ky]=tx;

	    	f5data.tooltips[ky] = io.getToolTip(ky);
		} // end of eachWithIndex

		f5data.dump();

    	return f5data.tooltips;
    } // end of method


	/**
     * Utility method to reset a single JButton after it's title and/or payload were updated.
     *
     * @return void - no response
     */
	public void cleanup(String updatekey)
	{
		if (updatekey!=null)
		{
			tooltip = io.getToolTip(updatekey);
			if (tooltip.trim().size() > 0)
			{
			    f5data.tooltips[updatekey] = tooltip;
			    if (f5data.buttons[updatekey])
			    {
					say("F5 -> ${updatekey} function key built text for tooltip: ${tooltip}");
				} // end of if

				updatekey=null;
			} // end of if
		}  // end of if
	} // end of cleanup


	/**
	* Default constructor to build a tool to support function key usage to paste text onto the System clipboard
	*/
	public F5GUI(F5Data f5d) throws HeadlessException 
	{
		super("F5 Utility");
		f5data = f5d;
		setHeading("F5 Utility");
		getContentPane().setBackground(Color.BLACK);
		setSize(400,300);
		pl = new PositionLogic(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		revalidate();
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
		F5Data f5data = new F5Data();
		F5GUI f5d = new F5GUI(f5data);
		f5d.setVisible(true);
		f5d.getAvailableTooltips();
		//f5d.f5data.dump();
		
		println "... ------------";
		//f5data.dump();

		println "--- the end of F5GUI ---"
	} // end of main

} // end of class
