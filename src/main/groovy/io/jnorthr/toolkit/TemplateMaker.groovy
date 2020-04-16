package io.jnorthr.toolkit;

import groovy.transform.*;

import javax.swing.*
import java.awt.*;
import java.io.*;
import java.awt.event.*;
//import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import io.jnorthr.toolkit.IO;
import io.jnorthr.toolkit.Copier;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.KeyEvent;

/*
 * Class to gain text for this function key and save it with known .txt filename 
 */
class TemplateMaker implements ActionListener
{
    /** a handle for our GUI framework */
    def f = new JFrame();

    /** a handle for our input/output framework */
    IO io = new IO(true);

    /** a button to remove existing text in our GUI */
    JButton b1 = new JButton("Clear");

    /** a button to paste existing text from our GUI onto System clipboard */
    JButton b2 = new JButton("Paste");

    /** a button to write existing text in our GUI to an external file */
    JButton b3 = new JButton("Save");

    /** a button to kill this app */
    JButton b4 = new JButton("Exit");

    /** a place to enter text for clipboard  */
    JTextArea area=new JTextArea("");  

    /** a place where text for clipboard is held  */
    String payload = "";

    /** a panel for control buttons */
    JPanel jp = new JPanel();

    /** a panel for function key and it's title */
    JPanel jp2 = new JPanel();

    /** a field for function key declaration has a simple function key name, like F15, populates the function key variable */
    JTextField functionField = new JTextField(3); 
    
    /** a field for function key tooltip title */
    JTextField tooltip = new JTextField(30); 

    /** If we need to println audit log to work, this will be true */ 
    boolean audit = true;

    /** If we need to do a System.exit(0), this will be true else disposes of jframe and returns to caller */ 
    boolean exitOnClose = true;

    /** a hook to use of the Escape key */
    KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");

    /** valid choices for this program where F0 is the escape key */
    def fk = ["F0","F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12","F13","F14","F15","F16","F17","F18","F19","F20","F21","F22","F23","F24"] 

    /**
     * Default constructor builds a tool to get payload text for this function key and save in a file
     */
    public TemplateMaker()
    {
        f.setTitle("F5 -> Create a payload for this key"); 
    } // end of constructor


    /**
     * Non-Default constructor builds a tool to get payload text for this function key and save in a file
     * if .txt file does not exist
     *
     * @param  fn String of text declaring which key this run is for like F7 
     * @return a tool to get payload text for function key 'fn' and save in a file
     */
    public TemplateMaker(String fn)
    {
        f.setTitle("F5 -> Build a payload for this |${fn}| key"); 
        functionField.setText(fn);
        exitOnClose = false;

        // ask if user wants to use a GUI to create a template
        def ans = ask(); 

        // Yes, make a template file
        if (ans)
        {
            setup(fn);
        } // end of if
        else
        {
            f.dispose();
        }    
    } // end of constructor


    /**
     * Non-Default constructor builds a tool to build new file or overlay existing payload text plus title for this function key and save or rewrite in a file
     *
     * @param  fn String of text declaring which key this run is for like F7 
     * @param  data String of text declaring clipboard payload for this key  
     * @return a tool to manage payload text for function key 'fn' and save in a file
     */
    public TemplateMaker(String fn, String data)
    {
        f.setTitle("F5 -> Edit the payload for this ${fn} key"); 
        exitOnClose = false;
        functionField.setText(fn.trim());
        boolean yn = fn < "F0" || fn > "F24";
        if (yn)
        {
            say "... TemplateMaker can not setup(${fn}) as function key |${fn.trim()}| invalid"
            functionField.setText("F0"); // reset default
        }
        else
        {
            payload = data;
            setup(fn);
            
            String fi = fn.trim()+".txt"
            String tx = "";

            // automatic put payload on clipboard when saving; note tooltip text is a diff.gui field
            Copier co = new Copier();
            co.paste(payload);

            // remember tooltip title in our payload file delimited by || char.s
            //String tt = tooltip.getText().trim();
            //if ( tt.size() > 0 ) 
            //{ 
              //  payload = "|"+tt.trim()+"|"+area.getText();  
            //} // end of if

            tx = io.write(fi, payload ); 
            say "... TemplateMaker write constructor wrote ${fi} file with ${payload.size()} bytes; result was ${tx}";
            






        } // end of else

    } // end of constructor


    /**
     * @param  key String of text declaring which function key this run is for like F1 thru F12 but with no other leading/trailing chars  
     * @return a function key string name like F7 with .txt suffix like F7.txt of if empty then return blank
     */
    public String maker(String key)
    {
	key = stripper(key)
	if (key.length() > 0) { return key+".txt" }
	return "";
    } // end of class


    /**
     * @param  key String of text declaring which function key this run is for like F7 or F7.txt or xxx/fred/F7  or xxx/fred.bak 
     * @return a naked function key string name like F7 with no trailing or leading chars
     */
    public String stripper(String key)
    {
	String nakedkey = key.trim();
        say "... TemplateMaker doing stripper() for function key |${key}|";
	key = key.toUpperCase();
        if ( key in fk ) { return key }

	key = key.toLowerCase();
	if (key.endsWith(".txt")) 
	{
		def i = nakedkey.length()
		if (i>3) { i=i-4 }
		nakedkey = key.toUpperCase().substring(0, i)
	        say "... TemplateMaker stripper() found .txt so function key is now |${nakedkey}|";
	} // end of if

        if ( nakedkey in fk ) 
	{ 	
		say "... TemplateMaker stripper() returned |"+nakedkey+"|"
		return nakedkey 
	}
	say "... stripper() failed exception; could not convert |${nakedkey}|"
        return "";
    } // end of class



    /**
     * A method to capture tooltip and payload for a single function key
     *
     * @param  key String of text declaring which function key this run is for like F7 
     * @return void
     */
    public void setup(String key)
    {
        say "... TemplateMaker doing setup(${key})";

        //UIManager.getCrossPlatformLookAndFeelClassName();
        say "... TemplateMaker doing setup() for current function key :"+key;
        jp2.add(new JLabel("Function key :"));
        functionField.setText(key);
        functionField.setEditable(false); 
        jp2.add(functionField);
        jp2.add(new JLabel("Tooltip Title :"));


        // ask for prior tooltip from .txt file, if any
        //IO io = new IO(key);
	io.setFunctionKey(key);
        String tx = io.getToolTip();
        say "... TemplateMaker getting key ${key} tooltip of "+tx;
    
        tooltip.setText(tx); 
        jp2.add(tooltip);
	    f.addWindowListener( new WindowAdapter() 
        {
    	   public void windowOpened( WindowEvent e )
    	   {
		      tooltip.requestFocus(); // gui sets initial focus on tooltip text field
    	   }
	    });

        // gui top for function key plus tooltip input
        f.add(jp2, BorderLayout.NORTH);
        jp.setLayout(new GridLayout(4,1)); // 4 rows one column

        // payload text entry field
        area.setBounds(10,20, 240,200);  
        area.setLineWrap(true);
        Font font = new Font("Courier", Font.BOLD, 12);
        area.setFont(font);
        payload = io.getPayload();
        area.setText(payload);

        f.add(new JScrollPane(area), BorderLayout.CENTER);
        font = new Font("Arial", Font.BOLD, 14);


        // Clear button b1 erases tooltip and payload areas
        b1.setFont(font);
        b1.addActionListener(this);
        b1.setToolTipText( "Remove text in this panel" );
        b1.setOpaque(true);
    	b1.setBackground(Color.BLACK);
    	b1.setForeground(Color.BLUE);
        b1.setMnemonic(KeyEvent.VK_C);

        // Paste button b2 puts payload onto system clipboard
        b2.addActionListener(this);
        b2.setToolTipText( "Paste text (unchanged) onto System Clipboard" );
        b2.setFont(font);
        b2.setOpaque(true);
	b2.setBackground(Color.BLACK);
    	b2.setForeground(Color.BLUE);
        
        // Save button is b3
        b3.addActionListener(this);
        b3.setToolTipText( "Save this text into ${key}.txt external file" );
        b3.setFont(font);
        b3.setOpaque(true);
	b3.setBackground(Color.BLACK);
    	b3.setForeground(Color.BLUE);
        
        Action quitAction = new AbstractAction("ESC") {
            @Override
            public void actionPerformed(ActionEvent evt) {
            	say("\nQuit quitAction run when ESCAPE function key WAS pressed ...");
            	if (exitOnClose)
            	{
	            	f.dispose();
                	System.exit(0);
            	}
	            else
    	        {
        	        f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
            	} // end of else
                	
            } // end of ActionPerformed
        };


        // Escape key button b4 forces GUI to quit
        b4.addMouseListener(new MouseAdapter() 
        {
            public void mouseEntered(MouseEvent mEvt) 
            {
                b4.setToolTipText( "ESC key will quit this app" );
            }
        });

        b4.getActionMap().put("Quit", quitAction);
        b4.setAction(quitAction); // when button mouse clicked
        b4.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, "Quit");
        quitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_ESCAPE);
        //b4.setToolTipText( "Quit this application" );
        b4.setFont(font);
        b4.setOpaque(true);
	    b4.setBackground(Color.BLACK);
    	b4.setForeground(Color.BLUE);

        // add buttons to button panel at right of gui
        jp.add(b1);
        jp.add(b2);
        jp.add(b3);
        jp.add(b4);


        //def ks = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
        //b1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.SHIFT_MASK )); // , ActionEvent.SHIFT_MASK
        //b1.setAccelerator(ks);

        jp.setSize(20,60)
        f.add(jp,BorderLayout.EAST);  
        //b2.addActionListener(this);
        //b4.addActionListener(this);

        //f.add(b2);  
        f.setSize(800,300);  
        //f.setLayout(null);  
        f.setVisible(true);      
    } // end of method
    

    /**
     * A method to see if user confirms capture tooltip and payload for a single function key
     * if it does not exist
     *
     * @return int value of YES_OPTION or NP_OPTION
     */
    private boolean ask() 
    {
        //if (functionkey=="F0") { return false; }
        //def jframe = new JFrame()
        String ss = "Do you want to construct a template file now ? ";
        int answer = JOptionPane.showConfirmDialog(f, ss, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        boolean ok = (answer==0) ? true :false;
	String okt = (answer==0) ? "yes" : "no";
	say "... ask() function returns ok="+ok+" meaning "+okt+" user confirms capture."; 
	return ok;
    } // end of method


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
     * A method to stuff text onto system clipboard, but not stored in .txt file until SAVE button clicked
     *
     * @param  text is string to go on system clipboard
     * @return boolean true if all was ok or false if not
     */
    public boolean paste(String text)
    {
        say("... use TemplateMaker to paste text to clipboard :"+text);
        boolean ok = true;
        // paste
        try
        { 
            Copier co = new Copier();
            co.paste(text);
            say("... TemplateMaker pasted ok");
        } // end of try
        catch (Exception e) 
        {
            say("... failed to paste text to clipboard"+e.getMessage());
            ok = false;
        }
        return ok;
    } // end of method


    /**
     * GUI actionPerformed logic to handle button presses 
     *
     * @param  ActionEvent e that caused this action - probably a JButton
     * @return void
     */
    public void actionPerformed(ActionEvent e)
    {
        // clear
        if(e.getSource()==b1)
        { 
            area.setText("");
            tooltip.setText("");
            b2.setIcon(null);
            b3.setIcon(null);
        } // end of if

        // paste choice here ...
        if(e.getSource()==b2)
        { 
            Copier co = new Copier();
            String s2 = area.getText();
            co.paste(s2);
            File sourceimage = new File("images/check.png");
            b3.setIcon(null);
            if (sourceimage.exists() )
            {
    	        Image image = ImageIO.read(sourceimage);    
    	        b2.setIcon(new ImageIcon(image));
    	    } // end of if

        } // end of if

        // SAVE button: use IO code to write new template payload and tooltip
        if(e.getSource()==b3)
        { 
            b2.setIcon(null);
            // build payload text file name here, i.e. F11.txt
            //IO io = new IO();
            //assert functionkey!=null;
            String fi = functionField.trim()+".txt"

            // get payload text for this Function Key choice
            String payload = area.getText(); 
            String tx = "";

            // automatic put payload on clipboard when saving; note tooltip text is a diff.gui field
            Copier co = new Copier();
            co.paste(payload);

            // remember tooltip title in our payload file delimited by || char.s
            String tt = tooltip.getText().trim();
            if ( tt.size() > 0 ) 
            { 
                payload = "|"+tt.trim()+"|"+area.getText();  
            } // end of if

	    io.setup(functionField);
            tx = io.write(fi, payload ); 
            say "... wrote ${fi} file with ${payload.size()} bytes; result was ${tx}";

            // erase fields
            // area.setText( " ");
            // tooltip.setText( " ");

            // get checkmark .png image then place on SAVE button
            File sourceimage = new File("images/check.png");
            if (sourceimage.exists())
            {
	           Image image = ImageIO.read(sourceimage);    
    	       b3.setIcon(new ImageIcon(image));
    	    }
        } // end of if

        // exit button when clicked or ESC key hit
        if(e.getSource() == b4)
        { 
            if (exitOnClose)
            {
	            f.dispose();
                System.exit(0);
            }
            else
            {
                f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
            } // end of else
        } // end of if
        
    } // end of method
    


    /**
     * A method to see if user wants to capture tooltip and payload for a single function key but does not populate this class 'functionField' var;
     * the calling method for this function must do that
     * @return text value of function key chosen by user or blank. In the range of F1..F12
     */
    public String getChoice()
    {
        // a jframe here isn't strictly necessary, but it makes the example a little more real
        JFrame frame = new JFrame("Choose a Function Key");
        boolean ok = true;
        String name = "";

        while(ok)
        {
            // prompt the user to enter their name - note that if they press Cancel, 'name' will be null
            name = JOptionPane.showInputDialog(frame, "Which function key between F1 and F12 ?");
            if (!(name==null))
            {
                name = name.trim().capitalize()
                say "... TemplateMaker name=|${name}|"

                if (name in fk )
                {
                    // get the user's input. note that if they press Cancel, 'name' will be null
                    say("... The user's chosen function key is '${name}'.\n");
                    ok = false; // got a valid name so loop is finished
                }

                else
                {  
                    say " need Function key between F1,F2,F3...F12"
		    ok = true; // causes loop to repeat
                    JOptionPane.showMessageDialog(frame, "The name of your function key ${name} \nis not in range of F1 thru F12");
		    name = "";
                }
            } // end of if
            
	    // when user pressed ESC key
	    else
            {
                ok = false;
		name = "";
            } // end of else

        } // end of while
    
	say "... getChoice() returns name=|${name}|"
        return name;
    } // end of method


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
        println "TemplateMaker starting ..."
        Date date = new Date()

        SwingUtilities.invokeLater(new Runnable() 
        {
                @Override
                public void run() 
                {
                    println "----------------------------------- "
                    println "... TemplateMaker F12 updating Test 1 ..."
		    try{
                    	TemplateMaker f12 = new TemplateMaker("F12","this f12 text was updated by TemplateMaker\ntest one in main() method");                    
                    } 
		    catch (Exception x) 
		    {
			println "--- F12 failed :"+x.toString();  // should pass as F12 is not in allowable list of keys 
		    } // end of catch

                System.exit(0);
                    
                    
                    println "\n----------------------------------- "
                    println "\n... TemplateMaker F16 updating Test 2 ..."
		    try{
                    	TemplateMaker f16 = new TemplateMaker("F16","this f16 text was updated by TemplateMaker\ntest one in main() method\non ${date}");                    
                    } 
		    catch (Exception x) 
		    {
			println "--- failed :"+x.toString();  // should fail as F16 is not in allowable list of keys 
		    } // end of catch


                    // try default function key test 3 
                    println "\n----------------------------------- "
                    println "... TemplateMaker() Test 3"
                    TemplateMaker tm = new TemplateMaker();                    
                    String s = tm.getChoice();
                    tm.functionField.setText(s); 
                    println "... user wants |${s}| and key=|${tm.functionField}|"
                    if (s!=null)
                    {
                        println "... doing tm.setup()"
                        tm.setup(s);    
                    } // end of if          

                    println "\n----------------------------------- "
		    // try some stripper functions
		    println "... test 3 stripper: F12=|"+tm.stripper("F12")+"|";
		    println "... test 3 stripper: f12=|"+tm.stripper("f12")+"|";
		    println "... test 3 stripper: F12.txt=|"+tm.stripper("F12.txt")+"|";
		    println "... test 3 stripper: F12.adoc=|"+tm.stripper("F12.adoc")+"|";
		    println "... test 3 stripper: fred/F12.txt=|"+tm.stripper("fred.F12.txt")+"|";


                    println "\n----------------------------------- "
		    // try some maker functions
		    println "... test 3 maker: |F12|"+tm.maker("F12")+"|";
		    println "... test 3 maker: |blank|="+tm.maker("")+"|";
		    println "... test 3 maker: |f12|="+tm.maker("f12")+"|";
		    println "... test 3 maker: |F12.txt|="+tm.maker("F12.txt")+"|";
		    println "... test 3 maker: |F12.adoc|="+tm.maker("F12.adoc")+"|";
		    println "... test 3 maker: |fred\\F12.txt|="+tm.maker("fred\\F12.txt")+"|";


                    println "\n----------------------------------- "
                    println "... TemplateMaker(F11) Test 4"
                    TemplateMaker f11 = new TemplateMaker("F11");
                    boolean ok = f11.paste("F11 file ${f11.functionField} pasted by TemplateMaker Test 4 on ${date}");
				    def x = (ok) ? "successful" : "failed" ;
                    println "... 4th Test said that on ${date} paste request was "+x;


                    println " "
                    println "... TemplateMaker(F17) Test 5"
                    println "... TemplateMaker F17 update with bad function key 17 value - Test 5"
		    // this one should fail
		    try{ TemplateMaker f17 = new TemplateMaker("F17 tooltip","this f17 text was updated by TemplateMaker\ntest one in main() method");                    
                    } 
		    catch (Exception ex) 
		    {
			println "--- failed :"+ex.toString();  // should fail as F17 is not in allowable list of keys 
		    } // end of catch

                    System.exit(0);
                } // end of run()
            } // end of runnable
        );

        println "--- the end of TemplateMaker ---"
    }
}    