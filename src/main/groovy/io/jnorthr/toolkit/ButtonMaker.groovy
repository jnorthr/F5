package io.jnorthr.toolkit;

import io.jnorthr.toolkit.F5Data;
import io.jnorthr.toolkit.F5GUI;
import io.jnorthr.toolkit.Copier;
import io.jnorthr.toolkit.IO;
import io.jnorthr.toolkit.Mapper;

import io.jnorthr.toolkit.actions.EditAction;
import io.jnorthr.toolkit.actions.ToClipboardAction;
import io.jnorthr.toolkit.TemplateMaker;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.*;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.border.LineBorder;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * This program demonstrates building a clickable Jbutton.
 *
 * @author james.northrop@orange.fr
 *
 */
public class ButtonMaker
{
	/**
	* a key/value map storage for known boilerplate variables of 'payloads' below that can be copied to 
	* system clipboard; key is like F12 or F2 while value is true if payload text exists  
    	*/
    	F5Data f5 = new F5Data();


	/**
	* a key/value map storage for known boilerplate variables used in F5 GUI 
    	*/
    	F5GUI f5gui = new F5GUI();
	
	/** a button to stop this app - usually tied to the ESC key on a keyboard*/
	JButton quitbutton = new JButton("Quit");

    	/** a hook to use of the Escape key */
	KeyStroke escKeyStrokeHit = KeyStroke.getKeyStroke("ESCAPE");


	/**
     	* Default constructor to build a JButton instance
     	*/
	public ButtonMaker(F5GUI f5g, F5Data f5d)
	{
		f5 = f5d;
		f5gui = f5g;
	} // end of constructor
	
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
		JButton mybutton = new JButton();
		mybutton.setFont(new Font("Arial", Font.PLAIN, 12));
		mybutton.setBorder(new LineBorder(Color.BLACK,1));

		if (f5.hasPayload[key])
		{
			mybutton.setForeground(Color.BLACK);
			mybutton.setBackground(Color.YELLOW);
			mybutton.setFont(new Font("Arial", Font.BOLD, 12));
            		mybutton.setContentAreaFilled(false);
            		mybutton.setOpaque(true);
		} // end of if
		else
		{
			mybutton.setBackground(Color.WHITE)
			if (key=="A")
			{
				mybutton.setFont(new Font("Arial", Font.BOLD, 12));
				mybutton.setForeground(Color.MAGENTA);
			}
			else
			{
				mybutton.setFont(new Font("Arial", Font.BOLD, 14));
				mybutton.setForeground(Color.RED);
				mybutton.setBackground(Color.BLACK);
			} // end of else			
		} // end of else


		/**
		 * logic to provide a tool tip for the function key with focus
		 */
		mybutton.addMouseListener(new MouseAdapter() 
		{
			public void mouseEntered(MouseEvent mEvt) 
            		{
		            	//cleanup();
            			if (key!="A")
            			{
					if (f5.hasPayload[key]  && key!="ESC" && f5.tooltips[key].size() > 0) 
					{
						f5gui.title = "${key} copies text to System Clipboard for : "+ f5.tooltips[key];
					}
					else
					{
						if (f5.hasPayload[key])
						{	
							f5gui.title = "${key} copies text to System Clipboard";
							def sz = f5.payloads[key].size()
							f5.buttons[key].setToolTipText( "has ${sz} bytes of text" );
						}
						else
						{
							f5gui.title = "F5 Utility";
						} // end of else
					} // end of else
				} // end of if 
				else
				{
	            			if (key!="ESC")
	            			{
						String s = (f5gui.ok) ? "horizontally" : "vertically" ;
						mybutton.setToolTipText( "Stack these buttons " + s );
						f5gui.title = "F5 -> click this to stack buttons up or across";	            		
	            			}
	            			else
	            			{
			            		f5gui.title = "F5 Utility";
	            			} // end of lelse
				} // end of else
    			} // end of mouseEntered
		}); // mybutton.addMouseListener
		
		//ImageIcon cutIcon = new ImageIcon(JavaAbstractActionExample.class.getResource("Cut-32.png"));
		Action myAction = new ToClipboardAction("Cut", null, "Cut stuff onto the clipboard", new Integer(KeyEvent.VK_CUT));
		
		mybutton.setAction(myAction); 	// when button mouse clicked
		myAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_ESCAPE);
		
		/**
		 * when function key pressed, this is done
		 */
		mybutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent."VK_${key}", 0), key);
		mybutton.getActionMap().put(key, myAction);

		/**
		 * when function key pressed with SHIFT key down, this edit event is done to edit/revise payload
		 */
		Action editAction = new EditAction("Edit", null, "Fix text within a file", new Integer(KeyEvent.VK_COPY));
		mybutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent."VK_${key}", KeyEvent.SHIFT_MASK), "Edit");
		mybutton.getActionMap().put("Edit", editAction);
		
		return mybutton;
	} // end of makeButton


	/**
	* A method to create a button to allow the whole GUI to be shifted to different side of display panel by inventing a button named 'A' rather than F3,F11 etc
     	*
     	* @return JButton ready for GUI to allow user to shift F5 display from side-to-side
     	*/
    	public JButton makeButtonA()
    	{
		JButton b = makeButton("A");  
		b.setFont(new Font("Arial", Font.BOLD, 14))
		b.setText("<=>"); // ^ arrow
        	b.setOpaque(true);
		b.setForeground(Color.BLACK);
		b.setBackground(Color.WHITE);
		//add(b);

		// add logic when 'A' button is actioned
		b.addActionListener(new ActionListener()
		{
			// do this when actioned
			public void actionPerformed(ActionEvent e)
  			{ 
  				println "... b.actionPerformed e="+e.toString();
    				f5gui.ok = !f5gui.ok;

    				/* when f5gui.ok is true then do vertical layout, either left edge or right edge */
    				if (f5gui.ok) 
    				{ 
	    				print "... vertical layout: "
    					f5gui.setLayout(f5gui.V); 
    					b.setText("<="); // 21E8
    					f5gui.setSize(60, 760); // 60px wide & 760px tall
					f5gui.setPreferredSize(new Dimension(60, 760)); // 60px wide & 760px tall
					f5gui.windowSize = f5gui.getSize(); // remember preferred dimension

	    				f5gui.right = !f5gui.right; // reverse left or right edge of screen when doing vertical layout
    					if (f5gui.right) { b.setText("=>"); }

    					int j = (f5gui.right) ? 0 : f5gui.screenSize.width - (f5gui.windowSize.width + 56);
					int thisWindowY = Math.max(0, ((f5gui.screenSize.height - f5gui.windowSize.height) / 2) );
    					f5gui.setLocation(j, thisWindowY); 
    					println "setLocation(${j}, ${thisWindowY}) "
    				} // end of if

	    			/**
    				 * do horizontal layout when f5gui.ok boolean is false
    				 */
    				if (!f5gui.ok) 
    				{ 
	    				f5gui.setLayout(f5gui.H); 
    					b.setText("<=>"); 
    					f5gui.setSize(760, 80); // 760px wide & 80px tall window panel
					f5gui.setPreferredSize(new Dimension(760, 80)); // make it so ....
					f5gui.windowSize = f5gui.getSize();  // ask java vm for dimensions
					int windowX = Math.max(0, (f5gui.screenSize.width  - f5gui.windowSize.width ) / 2); // divide by 2 to center between left&right edge of hardware
					int windowY = Math.max(0, (f5gui.screenSize.height - f5gui.windowSize.height))  - 80; // this puts our window 80px from hardware screen bottom
    					f5gui.setLocation(windowX, windowY); // this does it
    					println "... horizontal setLocation(${windowX}, ${windowY}) "
    				} // end of if

	    			f5gui.validate(); // tell jvm to do changes
  			} // end of actionPerformed
		}); // end of addActionListener

		return b;
	} // end of makeButtonA


	/**
	*	Setup ESCAPE button handling
    	*/
    	public JButton makeQuitButton()
    	{
		// build an abstract shell of an action with known reaction when ESC key pressed on user keyboard
		Action quitAction = new AbstractAction("ESC") {
			@Override
			public void actionPerformed(ActionEvent evt) {
				println "\nQuit quitAction run when ESCAPE function key WAS pressed ...";
				System.exit(0);
			} // end of ActionPerformed
		}; // end of quitAction


		/**
		* tied to ESC button; when you hover over ESC choice, this tooltip is shown
		*/
		quitbutton.addMouseListener(new MouseAdapter() 
		{
			public void mouseEntered(MouseEvent mEvt) 
            		{
				quitbutton.setToolTipText( f5.tooltips["ESC"] );
				f5gui.title = f5.tooltips["ESC"];
    			}
		});



		quitbutton.setFont(new Font("Arial", Font.BOLD, 8));
		quitbutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escKeyStrokeHit, "Quit");		

		/** this marries the abstract quitAction to the ESC button */
		quitbutton.getActionMap().put("Quit", quitAction);
		quitbutton.setAction(quitAction); // when button mouse clicked

		/** this marries the quitAction event to the keyboard ESC button */
		quitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_ESCAPE);

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
		// setup empty Map
		Map myPayload = ["F1":true]
		Map tools = ["F1":"this is tooltip for F1"]
		F5GUI f5gui = new F5GUI();
		F5Data f5 = new F5Data();
		
		def bm = new ButtonMaker(f5gui, f5);
		assert bm != null;
		 
		JButton b = bm.makeButton("F1");  
		println "... ButtonMaker.makeButton method gave JButton=" + b.toString();
		println "--- the end of ButtonMaker ---"
	} // end of main

} // end of class
