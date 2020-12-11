package io.jnorthr.toolkit;

import io.jnorthr.toolkit.F5Data;
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

import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

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
import javax.imageio.ImageIO;

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

    // a button to stop this app - usually tied to the ESC key on a keyboard
    JButton quitbutton; // = new JButton("Quit");

    // a button to do a full screen print as a .png to output file in current user's folder by some key on a keyboard*/
    JButton printbutton = new JButton("PrintScreen");
    JButton mybutton;

    /** a hook to use of the Escape key */
    KeyStroke escKeyStrokeHit = KeyStroke.getKeyStroke("ESCAPE");

    /** a hook to use of the Print Screen key */
    KeyStroke printKeyStrokeHit = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0); 

    Color purple = new Color(255);

    /** If we need to println audit log to work, this will be true */
    boolean audit = false;
    
    Icon icon9 = new ImageIcon("images/PLUS.png");

//	Action fromAction = new FromClipboardAction("From", icon9, "copy text from clipboard into function key's file", new Integer(printKeyStrokeHit));
	
    Action editAction = new EditAction("Edit", icon9, "Fix text within a file", new Integer(KeyEvent.VK_COPY));

	IO io = new IO();
	
    /**
    * Default constructor to build a JButton instance
    */
    public ButtonMaker(F5Data f5d)
    {
		f5data      = f5d;
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
		String g = "${key}";
    	Icon icon = new ImageIcon("images/${key}.png");
		if (icon==null) { mybutton.setText(g); }
		Action myAction = new ToClipboardAction("${key}", icon, "${key} tooltip text goes here", new Integer(KeyEvent."VK_${key}"), f5data);

		mybutton = new JButton(); 
		mybutton.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		mybutton.setBorderPainted(false);

		mybutton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent mEvt)
			{
				Object source = mEvt.getSource();
				String s = "";

				if (source instanceof JButton) {
					JButton btn = (JButton)source;
					s = btn.getText();
					io.read(s);
					s = io.getPayload();
				} // end of if
				
				//println "... Copier paste() received mouse event of "+s;
				ClipboardOwner owner = null;
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				Transferable transferable = new StringSelection(s);
				clipboard.setContents(transferable, owner);
    		} // endof method
		}); // end of addMou...

	
		/**
		* when function key pressed, this is done
		*/
		
		mybutton.setActionCommand(g);
		mybutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent."VK_${key}", 0), key);
		mybutton.getActionMap().put(key, myAction);

		/**
		* when function key pressed with SHIFT key down, this edit event is done to edit/revise payload
		*/
		mybutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent."VK_${key}", KeyEvent.SHIFT_MASK), "Edit");
		mybutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent."VK_${key}", KeyEvent.CTRL_MASK), "Edit");
		editAction.setKey(g);
		mybutton.getActionMap().put("Edit", editAction);

		mybutton.setPreferredSize(new Dimension(60, 48))
		mybutton.setToolTipText(f5data.tooltips["${key}"]);
		mybutton.setSize(new Dimension(60, 48))
		mybutton.setMargin(new Insets(0, 0, 0, 0));
		mybutton.setFont(new Font("Arial", Font.PLAIN, 10));
		mybutton.setBorderPainted(false);   // (new LineBorder(purple,2));   //Color.BLACK,1));
		mybutton.setVerticalTextPosition(SwingConstants.BOTTOM);
    	mybutton.setIcon(icon); 
        mybutton.setContentAreaFilled(false);
        mybutton.setOpaque(true);

		if (f5data.hasPayload[key])	// true if this function key text was found/loaded
		{
			mybutton.setForeground(Color.BLUE);
			mybutton.setBackground(Color.WHITE);
			mybutton.setFont(new Font("Arial", Font.BOLD, 10));
			mybutton.setToolTipText(f5data.tooltips[key]);
		} // end of if
		else
		{
			mybutton.setBackground(Color.BLACK);
			mybutton.setToolTipText("No text found for this key");
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
    	ImageIcon printer = new ImageIcon("images/screenprint.png");

		// build an abstract shell of an action with known reaction when screen print key pressed on user keyboard
        Action printAction = new PrintAction("", printer, null, new Integer(KeyEvent.VK_ENTER), f5data)

		//
		// tied to PRINT button; when you hover over this choice, this tooltip is shown
		//
		printbutton.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent mEvt)
			{
				def fn = System.getProperty("output.file");
				String currentDirectory  = System.getProperty("user.dir") + File.separator;
				if ( fn == null ) { fn = "${currentDirectory}screenprint.png" }
				f5data.frame.setTitle("Screenprint sent to ${fn}.");

				def size = Toolkit.getDefaultToolkit().getScreenSize();
				java.awt.Rectangle pic = new Rectangle(size);
    
				ImageIO.write(new Robot().createScreenCapture(pic),"png", new File(fn));  
    		}
		});

		printbutton.setSize(new Dimension(60, 48))
		printbutton.setMargin(new Insets(0, 0, 0, 0));
		printbutton.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		printbutton.setBorderPainted(false);

		printbutton.setFont(new Font("Arial", Font.BOLD, 10));
		printbutton.setBackground(Color.WHITE);
        printbutton.setContentAreaFilled(false);
        printbutton.setOpaque(true);
		//printbutton.setBorder(new LineBorder(Color.BLACK,1));

    	printbutton.setHorizontalTextPosition(SwingConstants.CENTER);
		printbutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(printKeyStrokeHit, "Print"); 

		printbutton.getActionMap().put("Print", printAction);
		printbutton.setAction(printAction); // when button mouse clicked
		printbutton.setToolTipText("Your ENTER key writes full screen copy to screenprint.png");
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
			public void mouseClicked(MouseEvent mEvt)
			{
				quitAction();
    		}
			public void mouseEntered(MouseEvent mEvt)
            {
				quitbutton.setToolTipText( f5data.tooltips["ESC"] );
    		}
		});

		quitbutton.setSize(new Dimension(60, 48))
		quitbutton.setMargin(new Insets(0, 0, 0, 0));
		quitbutton.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		quitbutton.setBorderPainted(false);
		quitbutton.setFocusPainted(false);

		quitbutton.setFont(new Font("Arial", Font.BOLD, 10));
		quitbutton.setBackground(Color.WHITE);
        quitbutton.setContentAreaFilled(false);
       	quitbutton.setOpaque(true);

		//quitbutton.setBorder(new LineBorder(purple,2));   //Color.BLACK,1));
    	quitbutton.setVerticalTextPosition(SwingConstants.CENTER);
    	quitbutton.setHorizontalTextPosition(SwingConstants.CENTER);
		quitbutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escKeyStrokeHit, "Quit");

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