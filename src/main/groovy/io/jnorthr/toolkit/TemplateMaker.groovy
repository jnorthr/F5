/*
 * Feature to get text & tooltip for valid function keys and save it with known .txt filename 
 */
package io.jnorthr.toolkit;

import groovy.transform.*;
import javax.swing.*
import java.awt.*;
import java.awt.event.*;
import io.jnorthr.toolkit.actions.*;

public class TemplateMaker implements ActionListener{

	JFrame f = new JFrame();

    /** a handle for our input/output framework */
    IO io = new IO(false);

    /** a button to remove existing text in our GUI */
    JButton b1 = new JButton("Clear");

    /** a button to paste existing text from our GUI onto System clipboard */
    JButton b2 = new JButton("Paste");

    /** a button to write existing text in our GUI to an external file */
    JButton b3 = new JButton("Save");

    /** a button to kill this app */
    JButton b4 = new JButton("Quit");

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
    boolean audit = false;

    /** If we need to do a System.exit(0), this will be true else disposes of jframe and returns to caller */ 
    boolean exitOnClose = true;

    /** a hook to use the Escape key */
    KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");

    /** valid choices for this program where F0 is the escape key */
    def fk = ["F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12","F13","F14","F15","F16","F17","F18","F19","F20","F21","F22","F23","F24"]; 

    Icon icon7;

    /**
     * Default constructor builds a tool to get payload text for this function key and save in a file
     */
    public TemplateMaker()
    {
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
        f.setTitle("F5 -> Create a payload for this key"); 
        setup();
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
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
		fn = stripper(fn);
		String g = "F5 -> Edit the payload for this ${fn} key";
		say g;
        f.setTitle(g); 
        boolean ans = io.reset(fn); // may return false if key val is bad
		boolean found = io.exists();
        exitOnClose = false;
		say "... TemplateMaker(String ${fn}) ans="+ans+" found="+found;

        if (ans)
        {
            functionField.setText(fn);
            // ask if user wants to use a GUI to create a template
			if (!found)
			{
				ans = ask("Do you want to construct a template file now ? ans=${ans}"); 
			} // end of if
        } // end of if
        else
        {
            f.setTitle("F5 -> Bad key |${fn}| value");
            JOptionPane.showMessageDialog(f, "WARNING: Cannot make template for key |${fn}|.");
            f.dispose();
        } // end of else

 
        // Yes, make a template file now
        if (ans && !found)
        {
            setup(fn);
        } // or no, don't make a template now, leave existing file untouched.
        else
        {
            f.dispose();
        } // end of else
    } // end of constructor


    /**
     * @param  key String of text declaring which function key this run is for like F1 thru F24 but with no other leading/trailing chars  
     * @return a function key string name like F7 with .txt suffix like F7.txt of return blank
     */
    public String maker(String key)
    {
	   key = stripper(key);
	   if (key.length() > 0) { return key+".txt" }
	   return "";
    } // end of method


    /**
     * @param  key String of text declaring which function key this run is for like F7 or F7.txt or xxx/fred/F7  or xxx/fred.bak 
     * @return a naked function key string name like F7 with no trailing or leading chars
     */
    public String stripper(String key)
    {
		String nakedkey = key.trim().toUpperCase();
        say "... TemplateMaker doing stripper() for function key |${nakedkey}|";
	
        if ( hasKey(nakedkey) ) { return nakedkey }

	    if (nakedkey.endsWith(".TXT")) 
	    {
            def i = nakedkey.length()
            if (i>3) { i=i-4 }
            nakedkey = nakedkey.toUpperCase().substring(0, i)
            say "... TemplateMaker stripper() found .txt so function key is now |${nakedkey}|";
        } // end of if

		if (nakedkey.contains("."))
		{
			int k = nakedkey.indexOf(".");
			nakedkey = nakedkey.substring(0,k);
			println "... nakedkey indexOf .  is "+k+" but now is |${nakedkey}|";
		} // end of if
		
        if ( hasKey(nakedkey) ) 
        { 	
            say "... TemplateMaker stripper() returned |"+nakedkey+"|"
            return nakedkey 
        }
		
		if (nakedkey.size() > 3) 
		{
			throw new Exception("|${nakedkey}| is too badly formed, not a function key.");
		} // end of fail
		
        say "... stripper() failed exception; could not convert |${nakedkey}|"
		throw new Exception("|${nakedkey}| is too badly formed, not a function key.")

        return "";
    } // end of method



    /**
     * A method to see if user confirms capture tooltip and payload for a single function key
     * if it does not exist
     *
     * @return int value of YES_OPTION or NP_OPTION
     */
    private boolean ask(String ss) 
    {
        int answer = JOptionPane.showConfirmDialog(f, ss, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        boolean ok = (answer==0) ? true :false;
        String okt = (answer==0) ? "yes" : "no";
        say "... ask() function returns ok="+ok+" meaning "+okt+" user confirms capture."; 
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


    /**
     * A method to only stuff text onto system clipboard, but not store in .txt file until SAVE button clicked
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
     * @param  ActionEvent that caused this action - probably a JButton
     * @return void
     */
    public void actionPerformed(ActionEvent e)
    {
        // get checkmark .png image then place on pressed button
        Icon check = new ImageIcon("images/check.png");
        
        icon7 = new ImageIcon("images/CLS.png");
        b1.setIcon(icon7);

        icon7 = new ImageIcon("images/CROSS.png");
        b2.setIcon(icon7);

        icon7 = new ImageIcon("images/SAVE.png");
        b3.setIcon(icon7);

        icon7 = new ImageIcon("images/ESC.png");
        b4.setIcon(icon7);

        // clear
        if (e.getSource()==b1)
        { 
            area.setText("");
            tooltip.setText("");
            functionField.setText("");
            b1.setIcon(check);
        } // end of if

        // paste choice here ...
        if (e.getSource()==b2)
        { 
            Copier co = new Copier();
            String s2 = area.getText();
            co.paste(s2);
    	    b2.setIcon(check);
        } // end of if

        // SAVE button: use IO code to write new template payload and tooltip to chosen output folder
        if (e.getSource()==b3)
        { 
            // build payload text file name here, i.e. F11.txt
            say "... 447  functionField=|${functionField.getText()}|"
            String fi = functionField.getText().trim()+".txt"

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

	        io.reset(fi);
            tx = io.write(payload); 
            say "... wrote ${fi} file with ${payload.size()} bytes; result was ${tx}";

            b3.setIcon(check);
        } // end of if


        // exit button when clicked or ESC key hit
        if (e.getSource() == b4)
        { 
            f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
        } // end of if        
    } // end of method


    
	// method to confirm string is a valid function key choice
	public boolean hasKey(String val)
	{    
		String ky = (val != null) ? val.trim().toUpperCase() : "";
		def list = ["F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12","F13","F14","F15","F16","F17","F18","F19","F20","F21","F22","F23","F24"];
		def yn = list.contains(ky);
		return yn;
	} // end of hasKey


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
        String name = "";

        while(ok)
        {
            // prompt the user to enter their name - note that if they press Cancel, 'name' will be null
            name = JOptionPane.showInputDialog(frame, "Which function key between F1 and F24 ?");
            if (!(name==null))
            {
                name = name.trim().capitalize()
                say "... TemplateMaker name=|${name}|"
				boolean yn = hasKey(name);
                if ( yn )
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
        frame.dispose();
        
        return name;
    } // end of method



    /**
     * A method to capture tooltip and payload for any single function key
     *
     * @return boolean is true if setup was successful
     */
    private boolean setup()
    {
        JRadioButton yesButton   = new JRadioButton("Yes", true);
        JRadioButton noButton    = new JRadioButton("No");
        JRadioButton maybeButton = new JRadioButton("Maybe");

        //... Create a button group and add the buttons.
        ButtonGroup bgroup = new ButtonGroup();
        bgroup.add(yesButton);
        bgroup.add(noButton);
        bgroup.add(maybeButton);
		//jp2.add(bgroup);
        f.add(bgroup, BorderLayout.SOUTH);
		
        jp2.add(new JLabel("Function key :"));
		functionField.setText("");
        functionField.setEditable(true); 
        jp2.add(functionField);
		functionField.requestFocus();

        jp2.add(new JLabel("Tooltip Title :"));
        tooltip.setText(""); 
        jp2.add(tooltip);
        tooltip.setEditable(true); 

		
        // gui top for function key plus tooltip input
        f.add(jp2, BorderLayout.NORTH);
        jp.setLayout(new GridLayout(4,1)); // 4 rows one column

        // payload text entry field
        area.setBounds(10,20, 240,200);  
        area.setLineWrap(true);
        Font font = new Font("Courier", Font.BOLD, 12);
        area.setFont(font);
        area.setText("");
        area.setEditable(true); 
		
        f.add(new JScrollPane(area), BorderLayout.CENTER);
	    f.setBackground(Color.YELLOW);
        f.add(jp,BorderLayout.EAST);  
        f.setSize(700,500);  
		f.pack();
		
		SwingUtilities.invokeAndWait(new Runnable()
		{
			@Override
			public void run()
			{
				f.setVisible(true);      
			}
		}
		);
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
        println "TemplateMaker starting ..."

        Date date = new Date();
        println "----------------------------------- ";
        println "... TemplateMaker F12 updating Test 1 ...";
		try{
			TemplateMaker f12 = new TemplateMaker();
			f12.setup();
        } 
		catch (Exception x) 
		{
            println "--- F12 failed :"+x.toString();  // should pass as F12 is not in allowable list of keys 
		} // end of catch

        println "----------------------------------- ";
        println "... TemplateMaker F12 updating Test 2 ...";
		try{
			TemplateMaker f12 = new TemplateMaker("F12");
			f12.setup();
        } 
		catch (Exception x) 
		{
            println "--- F12 failed :"+x.toString();  // should pass as F12 is not in allowable list of keys 
		} // end of catch
        println "--- the end of TemplateMaker ---";

		System.exit(0);
		
	} // end of main
 } // end of class 