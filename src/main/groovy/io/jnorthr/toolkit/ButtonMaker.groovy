package io.jnorthr.toolkit;

import io.jnorthr.toolkit.F5Data;
import io.jnorthr.toolkit.F5GUI;
import io.jnorthr.toolkit.Copier;
import io.jnorthr.toolkit.IO;
import io.jnorthr.toolkit.Mapper;

import io.jnorthr.toolkit.actions.EditAction;
import io.jnorthr.toolkit.actions.ToClipboardAction;
import io.jnorthr.toolkit.actions.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.*;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.*;
import javax.swing.*;

/**
 * This program demonstrates building a clickable Jbutton.
 *
 * @author james.b.northrop@googlemail.com
 *
 */
public class ButtonMaker
{
    /**
    * a key/value map storage for known boilerplate variables of 'payloads' below that can be copied to
    * system clipboard; key is like F12 or F2 while value is true if payload text exists
    */
    F5Data f5data;

    /**
    * a key/value map storage for known boilerplate variables used in F5 GUI
    */
    F5GUI f5gui;

    // a button to stop this app - usually tied to the ESC key on a keyboard
    JButton quitbutton; // = new JButton("Quit");

    // a button to do a full screen print as a .png to output file in current user's folder by some key on a keyboard*/
    JButton printbutton = new JButton("PrintScreen");
    JButton mybutton;

    /** a hook to use of the Escape key */
    KeyStroke escKeyStrokeHit = KeyStroke.getKeyStroke("ESCAPE");

    /** a hook to use of the Print Screen key */
    KeyStroke printKeyStrokeHit = KeyStroke.getKeyStroke("PAUSE");

    Color purple = new Color(255);

    /** If we need to println audit log to work, this will be true */
    boolean audit = false;
    
    Icon icon9 = new ImageIcon("images/PLUS.png");

    Action editAction = new EditAction("Edit", icon9, "Fix text within a file", new Integer(KeyEvent.VK_COPY));

    /**
    * Default constructor to build a JButton instance
    */
    public ButtonMaker(F5Data f5d)
    {
		f5data      = f5d;
		f5gui = new F5GUI(f5data);
		quitbutton  = makeQuitButton();
		printbutton = makePrintButton();
    } // end of constructor

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
     * Utility method to build a single JButton for each function key to paste text to the System clipboard
     * containing logic to read text from external file due to being clicked or pressing
     * it's associate Function Key.
     *
     * @param  text string of function key pressed, i.e. F1,F4
     * @return JButton containing logic to read text from external file
    */
    public JButton makeButton(String key)
    {
		mybutton = new JButton(); 
		mybutton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent mEvt)
			{   
				String butSrcTxt = mEvt.toString();
				Object source = mEvt.getSource();
				if (source instanceof JButton) {
					JButton btn = (JButton)source;
					butSrcTxt = btn.getText();
				} // end of if	
    		} // end of mouseClicked
		});

		Action myAction = new ToClipboardAction("${key}", icon9, "${key} tooltip text goes here", new Integer(KeyEvent."VK_${key}"), f5gui);
		String g = "${key}";
	
		/**
		* when function key pressed, this is done
		*/
		mybutton.setText(g);
		mybutton.setActionCommand(g);
		mybutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent."VK_${key}", 0), key);

		mybutton.getActionMap().put(key, myAction);

		/**
		* when function key pressed with SHIFT key down, this edit event is done to edit/revise payload
		*/
		mybutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent."VK_${key}", KeyEvent.SHIFT_MASK), "Edit");
		editAction.setKey(g);
		mybutton.getActionMap().put("Edit", editAction);

		mybutton.setPreferredSize(new Dimension(60, 48))
		mybutton.setToolTipText(f5data.tooltips["${key}"]);
		mybutton.setSize(new Dimension(60, 48))
		mybutton.setMargin(new Insets(0, 0, 0, 0));
		mybutton.setFont(new Font("Arial", Font.PLAIN, 10));
		mybutton.setBorderPainted(false);   // (new LineBorder(purple,2));   //Color.BLACK,1));
		mybutton.setVerticalTextPosition(SwingConstants.BOTTOM);
    	Icon icon = new ImageIcon("images/${key}.png");
    	mybutton.setIcon(icon); 
        mybutton.setContentAreaFilled(false);
        mybutton.setOpaque(true);


		if (f5data.hasPayload[key])	// true if this function key text was found/loaded
		{
			mybutton.setForeground(Color.BLUE);
			//mybutton.setBackground(Color.BLUE);
			mybutton.setFont(new Font("Arial", Font.BOLD, 10));
		} // end of if
		else
		{
			mybutton.setBackground(Color.BLACK);
		} // end of else

		say "... ButtonMaker makeButton(${key}) method ended\n"

		return mybutton;
	} // end of makeButton
	// ----------------------------------------------------------------------------


    /**
    *	Setup PRINT button handling
    */
    public JButton makePrintButton()
    {
    	ImageIcon printer = new ImageIcon("images/PLUS.png");

		// build an abstract shell of an action with known reaction when screen print key pressed on user keyboard
        Action printAction = new PrintAction("", printer, null, new Integer(KeyEvent.VK_P))

		//
		// tied to PRINT button; when you hover over this choice, this tooltip is shown
		//
		printbutton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent mEvt)
			{
				printAction();
    		}
		});

		printbutton.setFont(new Font("Arial", Font.BOLD, 10));
		printbutton.setBackground(Color.GREEN);
        printbutton.setContentAreaFilled(false);
        printbutton.setOpaque(true);
		printbutton.setBorder(new LineBorder(Color.BLACK,1));

    	printbutton.setHorizontalTextPosition(SwingConstants.CENTER);
		printbutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(printKeyStrokeHit, "PRINT"); 
		printbutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(printKeyStrokeHit, "PRINTSCREEN");

		// this marries the abstract quitAction to the ESC button 
		printbutton.getActionMap().put("PRINT", printAction);
		printbutton.setAction(printAction); // when button mouse clicked
		printbutton.setMnemonic(KeyEvent.VK_P);
		// myItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		//printbutton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		//printbutton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		printbutton.setToolTipText("This key writes full screen copy to screenprint.png");
		return printbutton;
    } // end of makePrintButton


    //	Setup ESCAPE button handling    
    public JButton makeQuitButton()
    {
    	ImageIcon quiticon = new ImageIcon("images/ESC.png");
		
		quitbutton = new JButton(quiticon);
		
		// build an abstract shell of an action with known reaction when screen print key pressed on user keyboard
        Action quitAction = new ExitAction("", quiticon, null, new Integer(KeyEvent.VK_ESCAPE))

		// tied to ESC button; when you hover over ESC choice, this tooltip text maybe shown
		quitbutton.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent mEvt)
            		{
				quitbutton.setToolTipText( f5data.tooltips["ESC"] );
				f5gui.setHeading(f5data.tooltips["ESC"]);
    			}
		});

		quitbutton.setFont(new Font("Arial", Font.BOLD, 10));
		quitbutton.setBackground(Color.GREEN);
        quitbutton.setContentAreaFilled(false);
       	quitbutton.setOpaque(true);
		//quitbutton.setBorder(new LineBorder(purple,2));   //Color.BLACK,1));
    	quitbutton.setVerticalTextPosition(SwingConstants.CENTER);
    	quitbutton.setHorizontalTextPosition(SwingConstants.CENTER);
		quitbutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escKeyStrokeHit, "Quit");
		quitbutton.setBorder(new LineBorder(Color.BLACK,1));

		// this marries the abstract quitAction to the ESC button 
		quitbutton.getActionMap().put("Quit", quitAction);
		quitbutton.setAction(quitAction); // when button mouse clicked

		return quitbutton;
	} // end of makeQuitButton


	// =============================================================================
    	/**
      	* The primary method to execute this class. Can be used to test and examine logic and performance issues.
      	*
      	* argument is a list of strings provided as command-line parameters.
      	*
      	* @param  args a list of possibly zero entries from the command line;
    	*/
	public static void main(String[] args)
	{
		println " "
		println "ButtonMaker starting"
		// setup empty Map
		Map myPayload = ["F12":true]
		Map tools = ["F12":"this is tooltip for F12"]

		// F5GUI f5gui = new F5GUI();
		println "ButtonMaker making F5Data"
		F5Data f5data = new F5Data();
		println "ButtonMaker ended making F5Data"

		println "... new constructor ButtonMaker(f5data)="+f5data;
		def bm = new ButtonMaker(f5data);
		println "... ButtonMaker made."

		println "... makeButton F12"
		JButton b = bm.makeButton("F12");
		println "... bm.makeButton(F12) method gave JButton=" + b.toString();

		println "... makeQuitButton()"
		b = bm.makeQuitButton();
		println "... bm.makeQuitButton() method gave JButton=" + b.toString();

		println "... makePrintButton()"
		b = bm.makePrintButton();
		println "... bm.makePrintButton() method gave JButton=" + b.toString();

		println "--- the end of ButtonMaker ---"
	} // end of main

} // end of class
