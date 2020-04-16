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
 * @author james.northrop@orange.fr
 *
 */
public class F5GUI extends JFrame implements KeyListener
{

	/** string holding the title to be displayed as part of GUI panel top line
	*/
	String title = "";

	/** a yes/no flag is true to build gui vertically or nofor horizontal layout;
	* toggles each time the Arrow button on F5gui is hit
	*/
	boolean ok = false;

	/** a handle to temp storage for an in-progress Function Key  */
	String tooltip = "";

	KeyStroke leftstroke = KeyStroke.getKeyStroke("VK_LEFT")
    
	/** a handle to temp storage for just changed Function Key  */
	String updatekey = null;

	/** a yes/no choice to put our gui at a left pixel location edge if true else right edge if false  */
	boolean right = false;

	/** a single row grid layout manager  */
	LayoutManager H = new GridLayout(1, 0, 0, 0);
    
	/** a single column grid layout manager  */
	LayoutManager V = new GridLayout(0, 1, 0, 0);
    
	/** a dimension of the current hardware screen size */
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	/** a dimension of the current F5 screen size */
    	Dimension windowSize = null;


	/** If we need to println audit log to work, this will be true */ 
    	boolean audit = false;

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


	public void add(JButton b)
	{
		super.add(b);
		say "... F5GUI.add"+b.toString();
	} // end of add


	/**
	 * KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_MASK);
	 * KeyStroke.getKeyStroke("RIGHT"); DOWN LEFT
	 * setLayout(new BoxLayout(BoxLayout.X_AXIS));  //this, BoxLayout.X_AXIS));
	 * setLayout(new FlowLayout(FlowLayout.CENTER));
	 * BoxLayout bl = new BoxLayout(getContentPane(), BoxLayout.X_AXIS);
	 * def bl = new GridLayout(1, 0, 0, 0);
	*/

	public void keyTyped(KeyEvent event) 
	{
		println "... keyTyped:"+event.getKeyCode();

		if (event.getKeyCode()== KeyEvent.VK_LEFT) { println "... just pressed KeyEvent.VK_LEFT"; }
		if (event.getKeyCode()== KeyEvent.VK_KP_LEFT) { println "... just pressed KeyEvent.VK_KP_LEFT"; }
		if (event.getKeyCode()== KeyEvent.VK_RIGHT) { println "... just pressed KeyEvent.VK_RIGHT"; }
		if (event.getKeyCode()== KeyEvent.VK_KP_RIGHT) { println "... just pressed KeyEvent.VK_KP_RIGHT"; }
	}

	public void keyReleased(KeyEvent kr) 
	{
	    println "... keyReleased(KeyEvent kr):"+kr.toString();
	}

	public void keyPressed(KeyEvent e) 
	{
	    	int keyCode = e.getKeyCode();
		println "... keyPressed:"+keyCode();

		say "... keyPressed(KeyEvent e) = "+keyCode;
		switch( keyCode ) 
		{ 
			case KeyEvent.VK_UP:
            					// handle up 
            					println "... KeyPressed ? VK_UP";
            					break;
        		case KeyEvent.VK_DOWN:
            					// handle down 
            					break;
		        case KeyEvent.VK_LEFT:
            					// handle left
            					println "... KeyPressed ? VK_LEFT";
            					break;
		        case KeyEvent.VK_KP_LEFT:
            					// handle left
            					println "... KeyPressed ? VK_KP_LEFT";
            					break;
        		case KeyEvent.VK_RIGHT :
            					// handle right
            					break;
     		} // end of switch
	} // end of keyPressed



	/**
	* Default constructor to build a tool to support function key usage to paste text onto the System clipboard
	*/
	public F5GUI() throws HeadlessException 
	{
		super("F5 Utility");
		setHeading("F5 Utility");
		getContentPane().setBackground(Color.BLACK);
		setLayout(H);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(760, 80);
		setPreferredSize(new Dimension(760, 80));

		windowSize = this.getSize();

		int windowX = Math.max(0, (screenSize.width  - windowSize.width ) / 2);
		int windowY = Math.max(0, (screenSize.height - windowSize.height)) - 80;
		//windowY = screenSize.height - (windowSize.height+40);

		say "... windowSize.width=${windowSize.width} and windowSize.height=${windowSize.height}"
		say "... screenSize.width=${screenSize.width} and screenSize.height=${screenSize.height}"
		
		this.setLocation(windowX, windowY);  
		say "... initial setLocation(${windowX}, ${windowY}) "

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
		F5GUI f5d = new F5GUI().setVisible(true);
		
		println "--- the end of F5GUI ---"
	} // end of main

} // end of class
