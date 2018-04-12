package io.jnorthr.toolkit;

import groovy.transform.*;

import javax.swing.*
import java.awt.*;
import java.io.*;
import java.awt.event.*;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import io.jnorthr.toolkit.IO;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/*
 * Class to gain text for this function key and save it with known filename.F5 
 */
class TemplateMaker implements ActionListener
{
    /** a handle for our GUI framework */
    def f = new JFrame();

    /** a simple name, like F12, for our function key filename */
    String filename = "";

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

    /** a field for function key declaration */
    JTextField functionkey = new JTextField(3); 
    
    /** a field for function key title */
    JTextField tooltip = new JTextField(30); 

    /** If we need to println audit log to work, this will be true */ 
    boolean audit = false;

    /** If we need to do a System.exit(0), this will be true else disposes of jframe and returns to caller */ 
    boolean exitOnClose = true;


    /**
     * Default constructor builds a tool to get payload text for this function key and save in a file
     */
    public TemplateMaker()
    {
        f.setTitle("F5 -> Create a payload for this key"); 
    } // end of constructor


    /**
     * Non-Default constructor builds a tool to get payload text for this function key and save in a file
     *
     * @param  fn String of text declaring which key this run is for like F7 
     * @return a tool to get payload text for function key 'fn' and save in a file
     */
    public TemplateMaker(String fn)
    {
        f.setTitle("F5 -> Build a payload for this ${fn} key"); 
        filename = fn;
        exitOnClose = false;

        // ask if user wants to use a GUI to create a template
        def ans = ask(); 

        // Yes, make a template file
        if (ans==0)
        {
            setup();
        } // end of if
        else
        {
            f.dispose();
        }    
    } // end of constructor


    /**
     * Non-Default constructor builds a tool to get payload text for this function key and save in a file
     *
     * @param  fn String of text declaring which key this run is for like F7 
     * @param  ok boolean to trigger initial creation of a text file for this 'fn' 
     * @return a tool to enter payload text for function key 'fn' and save in a file
     */
    public TemplateMaker(String fn, boolean ok)
    {
        f.setTitle("F5 -> Build a payload for this ${fn} key"); 
        filename = fn;
        exitOnClose = false;
        payload = "";
        setup();
        //f.dispose();
    } // end of constructor


    /**
     * Non-Default constructor builds a tool to get payload text plus title for this function key and save in a file
     *
     * @param  fn String of text declaring which key this run is for like F7 
     * @param  data String of text declaring clipboard payload for this key  
     * @return a tool to manage payload text for function key 'fn' and save in a file
     */
    public TemplateMaker(String fn, String data)
    {
        f.setTitle("F5 -> Edit the payload for this ${fn} key"); 
        exitOnClose = false;
        filename = fn;
        payload = data;
        setup();    
    } // end of constructor


    /**
     * A method to capture tooltip and payload for a single function key
     *
     * @return void
     */
    public void setup()
    {
        //UIManager.getCrossPlatformLookAndFeelClassName();

        jp2.add(new JLabel("Function key :"));
        functionkey.setText(filename);
        functionkey.setEditable(false); 
        jp2.add(functionkey);

        jp2.add(new JLabel("Tooltip Title :"));
        IO io = new IO();
        String tx = io.getToolTip(filename);

        tooltip.setText(tx);
        jp2.add(tooltip);
	f.addWindowListener( new WindowAdapter() {
    	public void windowOpened( WindowEvent e )
    		{
		  tooltip.requestFocus();
    		}
	});

        f.add(jp2, BorderLayout.NORTH);
        jp.setLayout(new GridLayout(4,1));
        area.setBounds(10,20, 240,200);  
        area.setLineWrap(true);
        Font font = new Font("Courier", Font.BOLD, 12);
        area.setFont(font);
        area.setText(payload);

        f.add(new JScrollPane(area), BorderLayout.CENTER);
        font = new Font("Arial", Font.BOLD, 14);
        b1.setFont(font);
        b1.addActionListener(this);
        b1.setToolTipText( "Remove text in this panel" );
        b1.setOpaque(true);
	b1.setBackground(Color.BLACK);
    	b1.setForeground(Color.BLUE);

        b2.addActionListener(this);
        b2.setToolTipText( "Paste text (\${x} unchanged) onto System Clipboard" );
        b2.setFont(font);
        b2.setOpaque(true);
	b2.setBackground(Color.BLACK);
    	b2.setForeground(Color.BLUE);
        
        b3.addActionListener(this);
        b3.setToolTipText( "Save this text into ${filename}.F5 external file" );
        b3.setFont(font);
        b3.setOpaque(true);
	b3.setBackground(Color.BLACK);
    	b3.setForeground(Color.BLUE);
        
        b4.addActionListener(this);
        b4.setToolTipText( "Quit this application" );
        b4.setFont(font);
        b4.setOpaque(true);
	b4.setBackground(Color.BLACK);
    	b4.setForeground(Color.BLUE);

        jp.add(b1);
        jp.add(b2);
        jp.add(b3);
        jp.add(b4);

        //def ks = KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0);
        //b2.setAccelerator(ks);

        jp.setSize(20,60)
        f.add(jp,BorderLayout.EAST);  
        b2.addActionListener(this);

        //f.add(b2);  
        f.setSize(800,300);  
        //f.setLayout(null);  
        f.setVisible(true);      
    }
    

    /**
     * A method to see if user wants to capture tooltip and payload for a single function key
     *
     * @return int value of YES_OPTION or NP_OPTION
     */
    private int ask() 
    {
        //def jframe = new JFrame()
        String ss = "Do you want to construct a ${filename} template file now ? ";
        int answer = JOptionPane.showConfirmDialog(f, ss, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        answer
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

        // paste
        if(e.getSource()==b2)
        { 
            Copier co = new Copier();
            String s2 = area.getText();
            co.paste(s2);
            File sourceimage = new File("images/check.png");
            if (sourceimage.exists() )
            {
	        Image image = ImageIO.read(sourceimage);    
    	        b2.setIcon(new ImageIcon(image));
    	    } // end of if

        } // end of if

        // save: use IO code to write new template payload
        if(e.getSource()==b3)
        { 
            String s1 = area.getText();
            //area.setText( " ");
            IO ck = new IO();
            String fi = filename.trim()+".F5"
            String tx = ck.write(fi, s1);

            // write tooltip title file
            fi = filename.trim()+".txt";
            tx = tooltip.getText().trim();
            if ( tx.size() > 0 ) { tx = ck.write(fi, tx ); }

            File sourceimage = new File("images/check.png");
            if (sourceimage.exists())
            {
	        Image image = ImageIO.read(sourceimage);    
    	        b3.setIcon(new ImageIcon(image));
    	    }
        } // end of if

        // exit
        if(e.getSource()==b4)
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
     * A method to see if user wants to capture tooltip and payload for a single function key
     *
     * @return text value of function key chosen by user or null. In the range of F1..F12
     */
    public String getChoice()
    {
        // a jframe here isn't strictly necessary, but it makes the example a little more real
        JFrame frame = new JFrame("Choose a Function Key");
        boolean ok = true;
        String name = "";
        def fk = ["F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12"]

        while(ok)
        {
            // prompt the user to enter their name
            name = JOptionPane.showInputDialog(frame, "Which function key between F1 and F12 ?");
            if (!(name==null))
            {
                name = name.trim().capitalize()
                say "... TemplateMaker name=|${name}|"

                if (name in fk )
                {
                    // get the user's input. note that if they press Cancel, 'name' will be null
                    System.out.printf("The user's name is '%s'.\n", name);
                    ok = false;
                }
                else
                {  
                    say " need Function key between F1,F2,F3...F12"
                    JOptionPane.showMessageDialog(frame, "The name of your function key ${name} \nis not in range of F1 thru F12");
                }
            } // end of if
            else
            {
                ok = false;
            } // end of else
        } // end of while
    
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

        SwingUtilities.invokeLater(new Runnable() 
            {
                @Override
                public void run() 
                {
                    //println "TemplateMaker F14 updating ..."
                    //obj=new TemplateMaker("F14","some text here\nas payload");                    
                    def obj=new TemplateMaker();                    
                    String s = obj.getChoice();
                    obj.filename = s;
                    println "... s=|${s}|"
                    if (s!=null)
                    {
                        println "... doing setup()"
                        obj.setup();    

                    } // end of if

                    //System.exit(0);
                } // end of run()
            } // end of runnable
        );

        println "--- the end of TemplateMaker ---"
    }
}    