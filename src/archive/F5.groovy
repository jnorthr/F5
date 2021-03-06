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
import java.awt.event.KeyListener;
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
public class F5 extends JFrame implements KeyListener{

    /** a hook to use of the Escape key */
	KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
	KeyStroke leftstroke = KeyStroke.getKeyStroke("VK_LEFT")
    
    /** a button to stop this app - usually tied to the ESC key on a keyboard*/
	JButton quitbutton = new JButton("Quit");

    /** a handle to our IO module to do read/write stuff */
	IO io = new IO();

    /** If we need to println audit log to work, this will be true */ 
    boolean audit = false;
	
    /** a handle to temp storage for an in-progress Function Key  */
	String tooltip = "";
    
    /** a key/value map storage for know Function Keys; key is like F12,F2,F14 while value of it's tooltip text  */
    Map tooltips = [:]

    /**
    * a key/value map storage for known Function Keys with a boilerplate 'payloads' below that can be copied to 
    * system clipboard; key is like F12 or F2 while value is true if payload text exists  
    */
    Map hasPayload = [:]

    /**
    * a key/value map storage for known Function Keys with a boilerplate payload that can be copied to 
    * system clipboard; key is like F12 or F2 while value is it's text payload if same hasPayload flag is true  
    */
    Map payloads = [:]


    /**
    * a key/value map storage for button Function Keys with a boilerplate payload that can be copied to 
    */
    Map buttons = [:]

    /** a handle to temp storage for just changed Function Key  */
	String updatekey = null;

    /** a yes/no flag is true to build gui vertically or nofor horizontal layout;
     * toggles each time the Arrow button onF5 gui is hit
     */
	boolean ok = false;

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


    public JButton makeButtonA()
    {
		JButton b = makeButton("A");  
		b.setText("\u21E7"); // ^ arrow
        b.setOpaque(true);
		b.setBackground(Color.WHITE);
		add(b);

		b.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
  			{ 
  				println "... b.actionPerformed e="+e.toString();
    			ok = !ok;
    			/* do vertical layout, either left edge or right edge */
    			if (ok) 
    			{ 
    				print "... vertical layout: "
    				setLayout(V); 
    				b.setText("\u21E6"); // 21E8
    				setSize(20, 500); 
					setPreferredSize(new Dimension(20, 500));
					windowSize = this.getSize();
    				right = !right;
    				if (right) { b.setText("\u21E8"); }
    				int j = (right) ? 0 : screenSize.width - (windowSize.width + 56);
					int thisWindowY = Math.max(0, ((screenSize.height - windowSize.height) / 2) );
    				this.setLocation(j, thisWindowY); 
    				say "setLocation(${j}, ${thisWindowY}) "
    			} // end of if

    			/**
    			 * do horizontal layout
    			 */
    			if (!ok) 
    			{ 
    				setLayout(H); 
    				b.setText("\u21E7"); 
    				setSize(600, 38); 
					setPreferredSize(new Dimension(600, 38));
					windowSize = this.getSize();
					int windowX = Math.max(0, (screenSize.width  - windowSize.width ) / 2);
					int windowY = Math.max(0, (screenSize.height - windowSize.height))  - 20;
    				this.setLocation(windowX, windowY);
    				say "... horizontal setLocation(${windowX}, ${windowY}) "
    			}
    			validate();
  			}
		});

		return b;
    } // end of makeButtonA


    /**
    *	Setup ESCAPE button handling
    */
    public JButton makeQuitButton()
    {
		Action quitAction = new AbstractAction("ESC") {
			@Override
			public void actionPerformed(ActionEvent evt) {
				say("\nQuit quitAction run when ESCAPE function key WAS pressed ...");
				System.exit(0);
			} // end of ActionPerformed
		};

		/**
		* tied to ESC button; when you hover over ESC choice, this tooltip is shown
		*/
		quitbutton.addMouseListener(new MouseAdapter() 
		{
            public void mouseEntered(MouseEvent mEvt) 
            {
				quitbutton.setToolTipText( tooltips["ESC"] );
				setTitle(tooltips["ESC"]);
    		}
		});

		quitbutton.setFont(new Font("Arial", Font.BOLD, 8));
		quitbutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, "Quit");		

		/** this marries the quitAction to the ESC button */
		quitbutton.getActionMap().put("Quit", quitAction);
		quitbutton.setAction(quitAction); // when button mouse clicked

		/** this marries the quitAction event to the keyboard ESC button */
		quitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_ESCAPE);

		return quitbutton;    	
    } // end of makeQuitButton


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
    	switch( keyCode ) { 
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
     	}
	} 

    /**
     * A method to get known tooltip text from external file named after a function key; so 
     * tooltip text for help in F2.txt is for the F2 function key and also holds payload; or
     * F11.txt holds tip for F11 function key while it may/maynot have the text we copy to 
     * clipboard if/when F11 key is pressed (or clicked).
     *
     * Note that these simple .txt files are made in the TemplateMaker class in user home directory
     * plus folder named 'copybooks' plus F1 plus .txt giving /Users/jim/copybooks/F1.txt file name
     *
     * @return map of known tooltips per function key
     */
    public Map getAvailableTooltips()
    {
    	tooltips = [:]
    	tooltips["ESC"] = "ESC key ends this app";

		(1..12).eachWithIndex{ num, ix -> 
			String ky = "F${ix+1}";
			//println "... ky=|F${ix+1}|" 

	        /*
	        * takes only simple key name like F3 or F3.txt to find a filename like /Users/jim/copybooks/F3.txt
			*/
	        String tx = io.getPayload(ky); 

			// remember which function key has boilerplate file by setting it to true or false flag in IO module
			hasPayload[ky]=io.present;

	        // keep the text content of file just read in internal array
        	payloads[ky]=tx;

        	// load function key io.tooltip value loaded in io.getPayload
			tooltip = io.tooltip;  //io.getToolTip(tx);
	        
	    	tooltips[ky] = tooltip;		
		} // end of each

    	return tooltips;
    } // end of method


		/**
		 * KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, KeyEvent.CTRL_MASK);
		 * KeyStroke.getKeyStroke("RIGHT"); DOWN LEFT
		 * setLayout(new BoxLayout(BoxLayout.X_AXIS));  //this, BoxLayout.X_AXIS));
		 * setLayout(new FlowLayout(FlowLayout.CENTER));
		 * BoxLayout bl = new BoxLayout(getContentPane(), BoxLayout.X_AXIS);
		 * def bl = new GridLayout(1, 0, 0, 0);
		*/

    /**
     * Default constructor to build a tool to support function key usage to paste text onto the System clipboard
     */
	public F5() throws HeadlessException {
		super("F5 Utility");
		getContentPane().setBackground(Color.BLACK);

		tooltips = getAvailableTooltips();
		//this.addKeyListener(listener);

		quitbutton = makeQuitButton();

		setLayout(H);

		(1..12).eachWithIndex{ num, ix -> 
			String tx = "F${num}"; 
			JButton b = makeButton(tx);
			if (hasPayload[tx]) 
			{
				b.setToolTipText( tooltips[tx] );
			}
			
			buttons[tx] = b;
			add(b);
		} // end of each


		// build the Arrow button to move gui to bottom orleftor right edge of hardware screen
		makeButtonA();

		add(quitbutton);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 38);
		setPreferredSize(new Dimension(600, 38));

		windowSize = this.getSize();

		int windowX = Math.max(0, (screenSize.width  - windowSize.width ) / 2);
		int windowY = Math.max(0, (screenSize.height - windowSize.height)) - 20;
		//windowY = screenSize.height - (windowSize.height+40);

		say "... windowSize.width=${windowSize.width} and windowSize.height=${windowSize.height}"
		say "... screenSize.width=${screenSize.width} and screenSize.height=${screenSize.height}"
		
		this.setLocation(windowX, windowY);  
		say "... initial setLocation(${windowX}, ${windowY}) "

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
		mybutton.setFont(new Font("Arial", Font.PLAIN, 12));
		mybutton.setBorder(new LineBorder(Color.BLACK,1));

		if (hasPayload[key])
		{
			mybutton.setForeground(Color.BLACK);
			mybutton.setBackground(Color.WHITE);
			mybutton.setFont(new Font("Arial", Font.BOLD, 10));
            mybutton.setContentAreaFilled(false);
            mybutton.setOpaque(true);
		} // end of if
		else
		{
			mybutton.setBackground(Color.WHITE)
			if (key=="A")
			{
				mybutton.setFont(new Font("Arial", Font.BOLD, 10));
				mybutton.setForeground(Color.MAGENTA);
			}
			else
			{
				mybutton.setForeground(Color.RED);
				mybutton.setBackground(Color.WHITE);
			} // end of else			
		} // end of else


		/**
		 * logic to provide a tool tip for the function key with focus
		 */
		mybutton.addMouseListener(new MouseAdapter() 
		{
            public void mouseEntered(MouseEvent mEvt) 
            {
            	cleanup();
            	if (key!="A")
            	{
					if (hasPayload[key]  && key!="ESC" && tooltips[key].size() > 0) 
					{
						setTitle("${key} copies text to System Clipboard for : "+tooltips[key]);
					}
					else
					{
						if (hasPayload[key])
						{	
							setTitle("${key} copies text to System Clipboard");
						}
						else
						{
							setTitle("F5 Utility");
						}
					}
				} // end of if 
				else
				{
	            	if (key!="ESC")
	            	{
						String s = (ok) ? "horizontally" : "vertically" ;
						mybutton.setToolTipText( "Stack these buttons " + s );
						setTitle("F5 -> click this to stack buttons up or across");	            		
	            	}
	            	else
	            	{
	            		setTitle("F5 Utility");
	            	}
				}
    		} // end of mouse
		});


		/**
		*	Here we show dialog to allow user to revise text for this function key, then stored in
		*	file named after function key plus .txt suffix. So function key four would store text in
		*	file named F4.txt
		*/
		Action editAction = new AbstractAction("${key}") {
			@Override
			public void actionPerformed(ActionEvent evt) {
				say("\nEDITAction run when CTRL+VK_${key} function key WAS pressed ...");
				cleanup();

        		String tx = io.getPayload(key);
        		if (tx.length() > 0)
        		{
					setTitle("F5 -> ${key} function key has ${tx.length()} bytes for ${tooltips[key]}"); 
   					SwingUtilities.invokeLater(new Runnable() 
    				{
      					public void run()
      					{
      						println "... editAction TemplateMaker"
							TemplateMaker obj = new TemplateMaker(key, tx);                    
							tooltip = io.getToolTip(key);
							if (tooltip.trim().size() > 0)
							{
						    	tooltips[key] = tooltip;
								setTitle("F5 -> ${key} function key built text for ${tooltip}"); 
								mybutton.setForeground(Color.BLUE);
								mybutton.setFont(new Font("Arial", Font.BOLD, 10)); 
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
	      						println "... editAction TemplateMaker key!=A"
								TemplateMaker obj = new TemplateMaker(key, true); 
								updatekey = key;                   
								setTitle("F5 -> ${key} - you need to edit text");  
      						} // end of run
	    				}); // end of invoke

    				} // end of if					
				}
			} // end of ActionPerformed
		};


		/**
		 * here's logic to copy payload text to clipboard for one function key that's been presssed 
		 */
		Action myAction = new AbstractAction("${key}") {
			@Override
			public void actionPerformed(ActionEvent evt) {
				say("\n${key} myAction run when ${key} function key pressed ...");
				cleanup();

				// ok, get xxx.txt text content, do any replacements using groovy template engine
        		String tx = io.getPayload(key);
        		if (tx.length() > 0)
        		{
        			// find the ${} parms
        			Mapper ma = new Mapper();
        			Map map = ma.getMap(tx);
        			tx = ma.getTemplate(map);

        			// time to put fully translated text string onto System Clipboard
        			Copier ck = new Copier();
        			ck.paste(tx);
					setTitle("F5 -> ${key} copied ${tx.length()} bytes to Clipboard for ${tooltips[key]}"); 
				} // end of if


        		/** when no Fxx.txt file found in user home folder or it's empty, logic comes here */
        		else
        		{
        			if (key!="A") 
        			{ 
        				setTitle("F5 -> ${key} function key has no text for Clipboard"); 
						SwingUtilities.invokeLater(new Runnable() 
    					{
      						public void run()
      						{
      						    println "... myAction TemplateMaker running..."
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
								println "... myAction TemplateMaker 523"							                   
      						} // end of run
    					});
    					println "... myAction TemplateMaker 526"
    				} // end of if					
    				else
    				{
						setTitle("F5 Utility"); 
    				} // end of else
        		} // end of else

        		/**
        		 * these don't change color
        		 * ((JButton) evt.getSource()).setBackground(Color.BLUE);
 				 * ((JButton) evt.getSource()).setContentAreaFilled(false);
                 * ((JButton) evt.getSource()).setOpaque(true);
        		 * JButton button = (JButton) evt.getSource()
        		 * button.setBackground(Color.CYAN);
				 * getContentPane().setBackground(Color.MAGENTA);
				 */
			} // end of ActionPerformed
		};

		mybutton.setAction(myAction); // when button mouse clicked
		myAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_ESCAPE);
		
		/**
		 * when function key pressed, this is done
		 */
		mybutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent."VK_${key}", 0), key);
		mybutton.getActionMap().put(key, myAction);

		/**
		 * when function key pressed with CTRL key down, this edit event is done to edit/revise payload
		 */
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
					say("F5 -> ${updatekey} function key built text for tooltip: ${tooltip}"); 
				}

				buttons[updatekey].setForeground(Color.BLUE);
				buttons[updatekey].setFont(new Font("Arial", Font.BOLD, 10)); 
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
		SwingUtilities.invokeAndWait(new Runnable() 
		{
			@Override
			public void run() 
			{
				new F5().setVisible(true);
			}
		}
		);

		//new F5().setVisible(true);
		println "--- the end of F5 ---"
	} // end of main

} // end of class
