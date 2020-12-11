package io.jnorthr.toolkit;

import groovy.transform.*;
import javax.swing.*
import java.awt.*;
import java.awt.event.*;
import io.jnorthr.toolkit.actions.*;
        //b1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.SHIFT_MASK ));  , ActionEvent.SHIFT_MASK
        //b1.setAccelerator(ks);
/*
* A class to make a GUI that allows the user to capture text payloads and tooltip info for keyboard
* function codes for your keyboard, using a key code in the text range of 'F1' thru 'F24'.
*/
public class TemplateGUI implements ActionListener, KeyListener
{
    /** a handle for our input/output framework */
    IO io = new IO(true);

    JFrame f = new JFrame();
    JButton b1 = new JButton("Clear");
    JButton b2 = new JButton("Paste");
    JButton b3 = new JButton("Save");
    JButton b4 = new JButton("Quit");
    JLabel label = new JLabel("Text Payload to SAVE or Paste to Your Clipboard");

	/* these two panels added to ContentPane of JFrame */
    JPanel jp = new JPanel();
    JPanel jp2 = new JPanel();

    JTextField functionField = new JTextField(3); 
    JTextField tooltip = new JTextField(30); 
    JTextArea area = new JTextArea(10,40);  

	/*
	* KeyStroke modifier choices :
	* SHIFT_MASK
	* CTRL_MASK
	* META_MASK
	* ALT_MASK
	* ALT_GRAPH_MASK
	*/
    KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
    KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
    
    boolean more = true;
    boolean audit = true;
    boolean ans   = false;
    
    // get checkmark .png image then place on pressed button
    Icon check = new ImageIcon("images/check.png");
    Icon icon7;
        
    /**
     * Default constructor builds a tool to get payload text for this function key and save in a file
     */
    public TemplateGUI()
    {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
       
       /*
        * GUI window frame listener for when user clicks RED X top of frame 
        *
        * if you also want to prevent the window from closing unless the user chooses 'Yes', you can add:
        *
        * frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        * but this does not trap window close on the JDialog,so if we need that, see: https://alvinalexander.com/java/jdialog-close-closing-event/
        * @param  java.awt.event.WindowAdapter that caused this action - probably click red X on JFrame
        * @return void
        */
        f.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override
        public void windowClosing(java.awt.event.WindowEvent windowEvent) 
        {
            if (JOptionPane.showConfirmDialog(f, 
                "Are you sure you want to close this window?", "Close Window?", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                {    
                    //if (!more) {return;}
                    //more = false;
                    f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
                    return;  //System.exit(0);
                }
            } // end of if
        }); // end of method & end of addWindowListener

        // if the Red X close button has not been clicked above, 'more' will be true
        if (more)
        {
            ans = ask("Do you want to construct a template file now ?"); 
            more = ans;
        } // end of if
                
		if (!more)
		{
            f.setTitle("F5 -> Default Values Used");
            JOptionPane.showMessageDialog(f, "If template found for this key, it will be copied to clipboard.");
            f.dispose();
			return;
        } // end of else
        else
        {
            // JPanel jp2 holds fields for top of dialog
            jp2.add(new JLabel("Function key :"));
			functionField.setText("");
            functionField.setEditable(true);
            functionField.setToolTipText( "Enter your function key choice between F1 & F24" ); 
            jp2.add(functionField);
			functionField.requestFocus();

            jp2.add(new JLabel("Tooltip Title :"));
            tooltip.setText("");
            tooltip.setToolTipText( "This will become the tooltip for your function key" ); 
            jp2.add(tooltip);
            tooltip.setEditable(true); 

            // payload text entry field
            area.setLineWrap(true);
            Font font = new Font("Courier", Font.BOLD, 12);
            area.setFont(font);
            area.setText("");
            area.setToolTipText( "Enter or paste text here to put on clipboard when function key pressed." );
            area.setEditable(true); 
      	    area.setBackground(Color.YELLOW);

            b1.addActionListener(this);
            b2.addActionListener(this);
            b3.addActionListener(this);
            b4.addActionListener(this);

            // JPanel jp holds fields for buttons of this dialog
            jp.setLayout(new GridLayout(4,1)); // 4 rows one column
            jp.add(b1);
            jp.add(b2);
            jp.add(b3);
            jp.add(b4);

            icon7 = new ImageIcon("images/CLS.png");
            b1.setIcon(icon7);
            b1.setToolTipText( "This will clear all fields in this dialog." );
                        
            icon7 = new ImageIcon("images/CROSS.png");
            b2.setIcon(icon7);
            b2.setToolTipText( "Your payload text is copied to your system clipboard." );

            icon7 = new ImageIcon("images/SAVE.png");
            b3.setIcon(icon7);
            b3.setToolTipText( "This choice writes your stuff to a .txt file in copybooks folder." );

            icon7 = new ImageIcon("images/ESC.png");            
            def quitAction = new QuitAction("Quit",icon7,"ESC",new Integer(KeyEvent.VK_ESCAPE));
            quitAction.setFrame(f);
            b4.getActionMap().put("Quit", quitAction);
            b4.setAction(quitAction); // when button mouse clicks this Quit button or the Escape key pressed
            b4.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, "Quit");
            b4.setToolTipText( "Use ESC key or this button to quit this dialog" );

            // setup frame options
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
            f.setTitle("F5 -> Payload Input Dialog"); 

            // jp2 is gui top for function key plus tooltip input
            f.add(jp2, BorderLayout.NORTH);
            f.add(jp,BorderLayout.EAST);  
            f.add(new JScrollPane(area), BorderLayout.CENTER);
            f.add(label, BorderLayout.SOUTH);
            f.setSize(600,400);
            f.pack();
            f.setLocationRelativeTo(null);
			f.setVisible(true);      		
        } // end of else      
    } // end of constructor
    
    
    /**
     * A method to see if user confirms capture tooltip and payload for a single function key
     * if it does not exist
     *
     * @return int value of YES_OPTION or NO_OPTION
     */
    private boolean ask(String ss) 
    {
        int answer = JOptionPane.showConfirmDialog(null, ss, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        boolean ok = (answer==0) ? true :false;
        String okt = (answer==0) ? "yes" : "no";
        say "... ask() function answer=${answer} returns ok="+ok+" meaning "+okt+" user confirms cancel."; 
        return ok;
    } // end of method


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


    /*
    * to satisfy KeyListener these methods must appear here :
    */
    public void keyPressed(KeyEvent ke)
    {
        say "... keyPressed="+ke;
    } // end of keyPressed

    /*
    * to satisfy KeyListener these methods must appear here :
    */
    public void keyReleased(KeyEvent ke)
    {
        say "... keyReleased="+ke;
    } // end of keyPressed

    /*
    * to satisfy KeyListener these methods must appear here :
    */
    public void keyTyped(KeyEvent ke)
    {
        say "... keyTyped="+ke;
    } // end of keyPressed


    /**
     * GUI actionPerformed logic to handle button presses 
     *
     * @param  ActionEvent that caused this action - probably a JButton
     * @return void
     */
    public void actionPerformed(ActionEvent e)
    {
        say "... some action happened at "+e.getSource();
            icon7 = new ImageIcon("images/CLS.png");
            b1.setIcon(icon7);
            icon7 = new ImageIcon("images/CROSS.png");
            b2.setIcon(icon7);
            icon7 = new ImageIcon("images/SAVE.png");
            b3.setIcon(icon7);

        // CLEAR  button logic comes here
        if (e.getSource()==b1)
        { 
            //say "... CLEAR actionPerformed";
            area.setText("");
            tooltip.setText("");
            functionField.setText("");
           
            b1.setIcon(check);
            functionField.requestFocus();
        } // end of if

        // PASTE choice here ...
        if (e.getSource()==b2)
        { 
            //say "... PASTE actionPerformed";
            Copier co = new Copier();
            String s2 = area.getText();
            co.paste(s2);
            //say "... button 2 copy payload text to clipboard:"+s2;
    	     b2.setIcon(check);
        } // end of if

        // SAVE button: use IO code to write new template payload and tooltip to chosen output folder
        if (e.getSource()==b3)
        { 
            // build payload text file name here, i.e. F11.txt
            //say "... SAVE action found functionField=|${functionField.getText()}|"
            String fi = functionField.getText().trim();
			//say "... TemplateGUI.getSource = b3 where fi="+fi;
            // get payload text for this Function Key choice
            String payload = area.getText(); 
            String tx = "";

            // remember tooltip title in our payload file delimited by || char.s
            String tt = tooltip.getText().trim();
            if ( tt.size() > 0 ) 
            { 
                payload = "|"+tt.trim()+"|"+area.getText();  
            } // end of if

			io.reset(fi);
            tx = io.write(payload); 
            say "... wrote ${fi} file with ${payload.size()} bytes;";

            b3.setIcon(check);
        } // end of if

        // exit button when clicked or ESC key hit
        if (e.getSource() == b4)
        { 
            say "... EXIT actionPerformed";
            f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
            return;
        } // end of if        
    } // end of method


    /**
     * A method to see if user wants to capture tooltip and payload for a single function key but does not populate this class 'functionField' var;
     * the calling method for this function must do that
     *
     * @return text value of function key chosen by user or blank. In the range of F1..F24
     */
    public String getChoice()
    {
        // a jframe here isn't strictly necessary, but it makes the example a little more real
        JFrame frame = new JFrame("Choose a Function Key");
        boolean ok = true;
        String name = null;

        while(ok)
        {
            // prompt the user to enter their name - note that if they press Cancel, 'name' will be null
            name = JOptionPane.showInputDialog(frame, "Which function key between F1 and F24 ?");
            if (!(name==null))
            {
                name = name.trim().capitalize()
                say "... TemplateGUI name=|${name}|";        
				boolean yn = (name.size() < 2 || name.size() > 3 || name < "F1" || name > "F24") ? false : true;
				if (yn)
				{
                    // get the user's input. note that if they press Cancel, 'name' will be null
                    say("... The user's chosen function key is '${name}'.\n");
                    ok = false; // got a valid name so loop is finished
                }
                else
                {  
                    say " need Function key between F1,F2,F3...F24"
					ok = true; // causes loop to repeat
                    JOptionPane.showMessageDialog(frame, "The name of your function key ${name} \nis not in range of F1 thru F24");
					name = "";
                } // end of else
            } // end of if
            
			// when user pressed ESC key
			else
			{
				ok = false;
				name = null;
			} // end of else
        } // end of while
    
		say "... getChoice() returns name=|${name}|"
        frame.dispose();
        
        return name;
    } // end of method

    // =============================================================================    
    /*
     * The primary method to execute this class. Can be used to test and examine logic and performance issues. 
     * 
     * argument is a list of strings provided as command-line parameters. 
     * 
     * @param  args a list of possibly zero entries from the command line; first arg[0] if present, is
     *         taken as a simple file name of a groovy script-structured configuration file;
     */
	public static void main(String[] args)
	{	
		println "TemplateGUI starting ..."

        Date date = new Date();
        println "----------------------------------- ";
        println "... TemplateGUI F12 updating Test 1 ...";
	    try{
			TemplateGUI f12 = new TemplateGUI();
                
			//def xxx = f12.ask("F12 updating Test 1"); println "... ask():"+xxx;
            println "----------------------------------- ";
            //xxx = f12.getChoice();
            //if (xxx==null) {xxx = "-no choice made -"; }
            //println "... f12.getChoice()="+xxx;
        } 
        catch (Exception x) 
        {
			println "--- F12 failed :"+x.toString();  // should pass as F12 is not in allowable list of keys 
        } // end of catch
        println "--- the end of TemplateGUI ---\n\n";
            
/*
            println "----------------------------------- ";
            println "... TemplateGUI F12 updating Test 2 ...";
            try{
                TemplateGUI f12 = new TemplateGUI("F12");
            } 
            catch (Exception x) 
            {
                println "--- F12 failed :"+x.toString();  // should pass as F12 is not in allowable list of keys 
            } // end of catch
            
            println "--- the end of TemplateGUI ---";
*/            
	} // end of main
 } // end of class 