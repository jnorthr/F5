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
import javax.swing.border.EmptyBorder;

/**
 * This Swing program demonstrates function key usage from a keyboard and/or clickable Jbutton.
 *
 * https://docs.oracle.com/javase/tutorial/uiswing/components/menu.html for more on keystrokes
 *
 * @author james.b.northrop@googlemail.com
 *
 */
public class F5GUI //extends JFrame 
{
	/** string holding the title to be displayed as part of GUI panel top line
	*/
	String title = "";

	/** a handle to temp storage for an in-progress Function Key  */
	String tooltip = "";
    
	/** a handle to temp storage for just changed Function Key  */
	String updatekey = null;

	/** If we need to println audit log to work, this will be true */ 
    boolean audit = false;

	/** a handle to our IO module to do read/write stuff */
	IO io = new IO();

	/** a handle to our global variables to do read/write stuff */
	F5Data f5data;

	/** a handle to our logic to manage the on-screen location of our F5 dialog */
	PositionLogic pl;

	JPanel cp = new JPanel();  //new FlowLayout());
	
	JToolBar tb = new JToolBar("F5 Utility - to copy function key text to System Clipboard");

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
	* A method to change the dialog title to this new value
    *
    * @param  is text to show user in dialog title line
    * @return void
    */
    public void setHeading(String text)
    {
        f5data.frame.setTitle(text); 
    } // end of method


	// includes a button onto this JFrame's content pane
	public void add(JButton b)
	{
		tb.add(b);
	} // end of add

	/**
	* Utility method to reset a single JButton after it's title and/or payload were updated.
    *
    * @return void - no response
    */
	public void cleanup(String updatekey)
	{
		tooltip = f5data.tooltip[updatekey];
	} // end of cleanup


	/**
	* Default constructor to build a tool to support function key usage to paste text onto the System clipboard
	*/
	public F5GUI(F5Data f5d) throws HeadlessException 
	{
		f5data = f5d;
		//dispose();
		setHeading("F5 Tool - to copy function key text to System Clipboard");
		f5data.frame.getContentPane().setBackground(Color.GRAY);
		f5data.frame.setBackground(Color.WHITE);
		f5data.frame.setSize(910,120);
		f5data.frame.setLocationRelativeTo(null);
		tb.setFloatable(true);
		tb.setRollover(true);
		tb.setBackground(Color.WHITE);

        // add toolbar to frame 
        f5data.frame.add(tb, BorderLayout.NORTH); 
		
		pl = new PositionLogic(f5data.frame);
		f5data.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		buttonMaker = new ButtonMaker(f5data);
		
		JButton b;
		(1..12).eachWithIndex{ num, ix ->
			String tx = "F${num}";
			tb.add(buttonMaker.makeButton(tx));
		} // end of each

		// the Screen Print button
		b = buttonMaker.printbutton;
		tb.add(b);

		// the ESC button
		b = buttonMaker.quitbutton;
		tb.add(b);

		f5data.frame.setVisible(true);
		
		f5data.frame.revalidate();
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
		JFrame frame = new JFrame("Test F5GUI main method");
		F5Data f5data = new F5Data();
		f5data.setFrame(frame);
		F5GUI f5gui = new F5GUI(f5data);
		JButton b1 = new JButton("B1");
		JButton b2 = new JButton("B2");
		f5gui.add(b1);
		f5gui.add(b2);
		f5gui.setVisible(true);
		//f5d.getAvailableTooltips();
		//f5d.f5data.dump();
		
		println "... ------------";
		f5data.dump();

		println "--- the end of F5GUI ---"
	} // end of main

} // end of class
