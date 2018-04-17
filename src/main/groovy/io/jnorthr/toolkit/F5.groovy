//package net.codejava.swing;
package io.jnorthr.toolkit;

import io.jnorthr.toolkit.Copier;
import io.jnorthr.toolkit.IO;
import io.jnorthr.toolkit.Mapper;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import java.awt.Dimension;
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
public class F5 extends JFrame {

    /** a hook to use of the Escape key */
	KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");

    /** a button to stop this app - usually tied to the ESC key on a keyboard*/
	JButton quitbutton = new JButton("Quit");

    /** a handle to our IO module to do read/write stuff */
	IO io = new IO();

    /** If we need to println audit log to work, this will be true */ 
    boolean audit = false;
	
    /** a handle to temp storage for an in-progress Function Key  */
	String tooltip = "";
    
    /** a key/value map storage for known Function Keys; key is like F12,F2 while value of it's tooltip text  */
    Map tooltips = [:]

    /** a key/value map storage for known Function Keys with a boilerplate payload that can be copied to 
    * system clipboard; key is like F12,F2 while value is it's tooltip text  
    */
    Map payloads = [:]


    /** a key/value map storage for buttoknown Function Keys with a boilerplate payload that can be copied to 
    */
    Map buttons = [:]


    /** a handle to temp storage for just changed Function Key  */
	String updatekey = null;

	int windowX = 0;
	int windowY = 0;
	boolean ok = false;
	LayoutManager H = new GridLayout(1, 0, 0, 0);
	LayoutManager V = new GridLayout(0, 1, 0, 0);
    
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
     * tooltip text is help in F2.txt for the F2 function key; F11.txt for F11 function key.
     *
     * Note that these .txt files are made in the TemplateMaker class
     *
     * @return map of known tooltips per function key
     */
    public Map getAvailableTooltips()
    {
    	def map = [:]
    	map["ESC"] = "ESC key ends this app";

		(1..12).eachWithIndex{ num, ix -> 
			String ky = "F${num}";
			say "... ky=|F${num}|" 

			// remember which function key has boilerplate file
			payloads[ky]=false;
	        String tx = io.getPayload(ky); // takes only simple key name like F3 or F11
    	    if (tx.length() > 0)
        	{
        		payloads[ky]=true
        	} // end of if

        	// load function key  tool tips
			tooltip = io.getToolTip(ky);
			if (tooltip.trim().size() < 1)
			{
    	    	if (payloads[ky])
        		{
					tooltip = "Press ${ky} function key to paste ${tx.length()} bytes onto Clipboard";
				}
			}  // end of if

	    	map[ky] = tooltip;
				
		} // end of each

    	return map;
    } // end of method



    /**
     * Default constructor to build a tool to support function key usage to paste text onto the System clipboard
     */
	public F5() throws HeadlessException {
		super("F5");
		getContentPane().setBackground(Color.CYAN);

		tooltips = getAvailableTooltips();

		Action quitAction = new AbstractAction("ESC") {
			@Override
			public void actionPerformed(ActionEvent evt) {
				say("\nQuit quitAction run when ESCAPE function key WAS pressed ...");
				System.exit(0);
			} // end of ActionPerformed
		};

		quitbutton.addMouseListener(new MouseAdapter() 
		{
            public void mouseEntered(MouseEvent mEvt) 
            {
				quitbutton.setToolTipText( tooltips["ESC"] );
    		}
		});

		//quitbutton.setPreferredSize(new Dimension(20, 16));
		quitbutton.setFont(new Font("Arial", Font.BOLD, 10));
		quitbutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, "Quit");
		quitbutton.getActionMap().put("Quit", quitAction);
		quitbutton.setAction(quitAction); // when button mouse clicked
		quitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_ESCAPE);
		add(quitbutton);

		//KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.SHIFT_MASK);

		//setLayout(new BoxLayout(BoxLayout.X_AXIS));  //this, BoxLayout.X_AXIS));
		//setLayout(new FlowLayout(FlowLayout.CENTER));
		//BoxLayout bl = new BoxLayout(getContentPane(), BoxLayout.X_AXIS);
		//def bl = new GridLayout(1, 0, 0, 0);
		setLayout(H);

		(1..12).eachWithIndex{ num, ix -> 
			//say "... ix=$ix num=$num  F${num}" 
			JButton b = makeButton("F${num}");
			buttons["F${num}"] = b;
			add(b);
		} // end of each

		JButton b = makeButton("A");  
		//buttons["A"] = b;
		b.setText("\u21E7");
		add(b);
		b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
  			{
    			ok = !ok;
    			if (ok) { setLayout(V); b.setText("\u21E8"); setSize(36, 400); this.setLocation(0, 60); }
    			if (!ok) { setLayout(H); b.setText("\u21E7"); setSize(700, 46); this.setLocation(windowX, windowY);}
    			validate();
  			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 46);
		//validate();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = this.getSize();

		windowX = Math.max(0, (screenSize.width  - windowSize.width ) / 2);
		windowY = Math.max(0, (screenSize.height - windowSize.height) / 2);
		windowY = screenSize.height - (windowSize.height+10);
		//say "... windowSize.width=${windowSize.width} and windowSize.height=${windowSize.height}"
		//say "... screenSize.width=${screenSize.width} and screenSize.height=${screenSize.height}"
		this.setLocation(windowX, windowY);  // Don't use "f." inside constructor.
		validate();
	} // end of constructor

	
    /**
     * Utility method to build a single JButton for each function key to paste text to the System clipboard
	 * containing logic to read text from external file due to being clicked or pressing
	 * it's associate Function Key.
     *
     * @param  text string of function key pressed, i.e. F1,F4
     * @return JButton containing logic to read text from external file 
     */
	public JButton makeButton(String key) {
		JButton mybutton = new JButton();
		mybutton.setFont(new Font("Arial", Font.PLAIN, 10));
		mybutton.setMargin(new Insets(0,-1,-3,0));
		//mybutton.setPreferredSize(new Dimension(22, 15));

		//mybutton.setBorder(new LineBorder(Color.BLACK,2));
		mybutton.setBackground(Color.CYAN)

		if (payloads[key])
		{
			mybutton.setForeground(Color.BLUE);
			mybutton.setFont(new Font("Arial", Font.BOLD, 12));
		} // end of if
		else
		{
			if (key=="A")
			{
				mybutton.setFont(new Font("Arial", Font.BOLD, 14));
				mybutton.setForeground(Color.MAGENTA);
			}
			else
			{
				mybutton.setForeground(Color.RED);
			} // end of else			
		} // end of else


		mybutton.addMouseListener(new MouseAdapter() 
		{
            public void mouseEntered(MouseEvent mEvt) 
            {
            	cleanup();
            	if (key!="A")
            	{
					mybutton.setToolTipText( tooltips[key] );
					setTitle("F5 -> Press ${key} function key to put ${tooltips[key]} on System Clipboard");
				} // end of if 
				else
				{
					String s = (ok) ? "horizontally" : "vertically" ;
					mybutton.setToolTipText( "Stack these buttons " + s );
					setTitle("F5 -> click this to stack buttons up or across");
				}
    		} // end of mouse
		});


		Action editAction = new AbstractAction("${key}") {
			@Override
			public void actionPerformed(ActionEvent evt) {
				say("\nEDITAction run when SHIFT+VK_${key} function key WAS pressed ...");
				cleanup();

        		String tx = io.getPayload(key);
        		if (tx.length() > 0)
        		{
					setTitle("F5 -> ${key} function key has ${tx.length()} bytes for ${tooltips[key]}"); 
   					SwingUtilities.invokeLater(new Runnable() 
    				{
      					public void run()
      					{
							TemplateMaker obj = new TemplateMaker(key, tx);                    
							tooltip = io.getToolTip(key);
							if (tooltip.trim().size() > 0)
							{
						    	tooltips[key] = tooltip;
								setTitle("F5 -> ${key} function key built text for ${tooltip}"); 
								mybutton.setForeground(Color.BLUE);
								mybutton.setFont(new Font("Arial", Font.BOLD, 12)); 
							}  // end of if
      					} // end of run
    				}); // end of invoke					
        		} // end of if
        		else
        		{
        			if (key!="A")
        			{
						setTitle("F5 -> ${key} function key requires text to edit");  
   						SwingUtilities.invokeLater(new Runnable() 
    					{
      						public void run()
      						{
      							// write new payload for this key
								TemplateMaker obj = new TemplateMaker(key, true); 
								updatekey = key;                   
								setTitle("F5 -> ${key} - you need to edit text");  
      						} // end of run
	    				}); // end of invoke

    				} // end of if					
				}
			} // end of ActionPerformed
		};


		Action myAction = new AbstractAction("${key}") {
			@Override
			public void actionPerformed(ActionEvent evt) {
				say("\n${key} myAction run when ${key} function key pressed ...");
				cleanup();

        		String tx = io.getPayload(key);
        		if (tx.length() > 0)
        		{
        			// find the ${} parms
        			Mapper ma = new Mapper();
        			Map map = ma.getMap(tx);
        			tx = ma.getTemplate(map);

        			Copier ck = new Copier();
        			ck.paste(tx);
					setTitle("F5 -> ${key} copied ${tx.length()} bytes to Clipboard for ${tooltips[key]}"); 
					//setTitle("F5 -> ${key} function key copied ${tx.length()} bytes to Clipboard");  
        		} // end of if
        		else
        		{
					setTitle("F5 -> ${key} function key has no text for Clipboard");  
					SwingUtilities.invokeLater(new Runnable() 
    				{
      					public void run()
      					{
							TemplateMaker obj = new TemplateMaker(key); 
							tooltip = io.getToolTip(key);
							say "... ${key} function key has no text for Clipboard; tooltip=|${tooltip}|"
							if (tooltip.trim().size() > 0)
							{
						    	map[key] = tooltip;
								setTitle("F5 -> ${key} function key built text for ${tooltip}");  
								mybutton.setForeground(Color.BLUE);
								mybutton.setFont(new Font("Arial", Font.BOLD, 12)); 
							}  // end of if							                   
      					} // end of run
    				});					
        		} // end of else

        		// these don't change color
        		//((JButton) evt.getSource()).setBackground(Color.BLUE);
 				//((JButton) evt.getSource()).setContentAreaFilled(false);
                //((JButton) evt.getSource()).setOpaque(true);

        		//JButton button = (JButton) evt.getSource()
        		//button.setBackground(Color.CYAN);

				//getContentPane().setBackground(Color.MAGENTA);

			} // end of ActionPerformed
		};

		//String key = "F5";
		mybutton.setAction(myAction); // when button mouse clicked
		myAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_ESCAPE);
		
		// when function key pressed, this is done
		mybutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent."VK_${key}", 0), key);
		mybutton.getActionMap().put(key, myAction);

		// when function key pressed with SHIFT key down, this edit event is done
		mybutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent."VK_${key}", KeyEvent.SHIFT_MASK), "Edit");
		mybutton.getActionMap().put("Edit", editAction);
		
		return mybutton;
	} // end of makeButton


    /**
     * Utility method to reset a single JButton after it's title and payload were updated.
     *
     * @return void - no response 
     */
	public void cleanup() 
	{
		if (updatekey!=null)
		{
			tooltip = io.getToolTip(updatekey);
			if (tooltip.trim().size() > 0)
			{
		    	tooltips[updatekey] = tooltip;
		    	if (buttons[updatekey])
		    	{
					println("F5 -> ${updatekey} function key built text for tooltip: ${tooltip}"); 
				}

				buttons[updatekey].setForeground(Color.BLUE);
				buttons[updatekey].setFont(new Font("Arial", Font.BOLD, 12)); 
				updatekey=null;
			} // end of if
		}  // end of if
		
	} // end of cleanup



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
/*		SwingUtilities.invokeAndWait(new Runnable() 
		{
			@Override
			public void run() 
			{
				new F5().setVisible(true);
			}
		}
		);
*/
		new F5().setVisible(true);
		println "--- the end of F5 ---"
	} // end of main

} // end of class
