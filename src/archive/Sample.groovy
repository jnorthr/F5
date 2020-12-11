package io.jnorthr.toolkit;
 
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.*;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import java.lang.annotation.*;
import io.jnorthr.toolkit.actions.*;

// makeQuitButton()

// Start of class declaration
public class Sample
{
    //=============================================
    // declare some variable names and object types
    JFrame frame = new JFrame("Sample");
    JToolBar toolbar = new JToolBar();
    /** a single row grid layout manager  */
    LayoutManager H = new GridLayout(1, 0, 0, 0);
    
    /** a single column grid layout manager  */
    LayoutManager V = new GridLayout(0, 1, 0, 0);
    
    /** a dimension of the current hardware screen size */
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    /** a dimension of the current F5 screen size */
    Dimension windowSize = null;

    ButtonMaker bm = new ButtonMaker();
    
    /** a button to stop this app - usually tied to the ESC key on a keyboard*/
    JButton quitbutton = new JButton("Quit");

    /** a hook to use of the Escape key */
    KeyStroke escKeyStrokeHit = KeyStroke.getKeyStroke("ESCAPE");

    String image = "images/ESC.png";  //"/Users/jim/Dropbox/Projects/F5/images/ESC.png";
    File imageURL = new File(image);

    def icon = new ImageIcon(image, "hi kids");
	
    // build an abstract shell of an action with known reaction when F1 function key pressed on user keyboard
    public class keyAction extends AbstractAction    // "${fn}" 
    {
            @Override
            public void actionPerformed(ActionEvent evt) 
            { 
                println "... ${fn} key WAS pressed ..."; 
            } // end of ActionPerformed
    }; // end of keyAction
 
 
    // build an abstract shell of an action with known reaction when ESC key pressed on user keyboard
    public class quitAction extends AbstractAction 
    {
        @Override
        public void actionPerformed(ActionEvent evt) 
        {
            println "\nQuit quitAction run when ESCAPE function key WAS pressed ...";
            System.exit(0);
        } // end of ActionPerformed
    }; // end of quitAction	


    public JButton crtButton(String fn)
    {
        JButton b = new JButton("${fn}");
        b.setMargin(new Insets(0, -9, 0, -9)); // top margin,left,bottom,right
        image = "images/${fn}.png";
        imageURL = new File(image);
        icon = new ImageIcon(image, "${image}");
        boolean ok = false;
        if (imageURL.exists() ) { println "... found ${image}"; ok = true;}
        if (!imageURL.exists() ) { println "... did not find  ${image}"; ok=false; }
        if (ok) 
        { 
            b.setText(""); 
            b.setIcon(icon); 
        } 
        else 
        { 
            b = new JButton("Hi")
            b.setText("${fn}");
            b.setBackground(Color.RED);
            b.setOpaque(true); 
            b.setPreferredSize(new Dimension(40, 40))
        } // end of else
    
        b.setBorderPainted(false);
        b.setToolTipText("${image}");
        b.setPreferredSize(new Dimension(40, 40));
            
        KeyStroke f1Key = KeyStroke.getKeyStroke("${fn}");
        b.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(f1Key, "${fn} key");		

        //b.getActionMap().put("${fn}", keyAction);
        //b.setAction(keyAction); // when button mouse clicked

        /** this marries the keyAction event to the keyboard F1 button */
        //keyAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F1);
        
        b.revalidate();
        println "...fn=|${fn}| b="+b.toString();
        return b;   
    } // end of crtButton


    // logic starts here
    public void add(JButton b)
    {
        frame.add(b);
        println "... Sample.add"+b.toString();
    } // end of add
	

    // ================================
    // declare constructor	
    public Sample() 
    {
        //def yn = checkIcon("ESC");

        frame.setTitle("Sample Dialog");
        frame.getContentPane().setBackground(Color.BLACK);
        toolbar.setBackground(Color.BLACK)
        frame.setLayout(H);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(790, 79);
        frame.setPreferredSize(new Dimension(790, 79));
        windowSize = frame.getSize();

        int windowX = Math.max(0, (screenSize.width  - windowSize.width ) / 2);
        int windowY = Math.max(0, (screenSize.height - windowSize.height)) - 10;
        //windowY = screenSize.height - (windowSize.height+40);

        println "... windowSize.width=${windowSize.width} and windowSize.height=${windowSize.height}"
        println "... screenSize.width=${screenSize.width} and screenSize.height=${screenSize.height}"
		
        frame.setLocation(windowX, windowY);  
        frame.revalidate();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // jbutton logic starts here ...
        quitbutton.setFont(new Font("Arial", Font.BOLD, 8));
        quitbutton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escKeyStrokeHit, "Quit");		
        quitbutton = bm.makeQuitButton();
        
        /** this marries the abstract quitAction to the ESC button */
        //quitbutton.getActionMap().put("Quit", quitAction);
        //quitbutton.setAction(quitAction); // when button mouse clicked

        /** this marries the quitAction event to the keyboard ESC button */
        //quitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_ESCAPE);
        //quitbutton.setIcon(icon);
        //quitbutton.setText("");
        //quitbutton.setToolTipText("Press your ESC key to end this app");
        //quitbutton.setBorderPainted(false);

    //toolbar.setRollover(true);  
    //JButton button = new JButton("button");
    //toolbar.add(button);
    //toolbar.addSeparator();    
    //toolbar.add(new JButton("button 2"));    
    //toolbar.add(new JComboBox(new String[]{"A","B","C"}));    

        Container contentPane = frame.getContentPane();
        contentPane.add(toolbar, BorderLayout.NORTH);

        toolbar.addSeparator();    
        //toolbar.add(quitbutton);

        ["ESC","CROSS","F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12"].each
        {va-> 
            def c = crtButton(va);
            toolbar.add(c) 
        } // end of each

        frame.revalidate();

    } // constructor ends here

    public static void main(String[] args)
    { 
        //SwingUtilities.invokeAndWait(new Runnable() 
        //{
            //@Override
            //public void run() 
            //{ 
                println "---> Starting code"
                Sample s = new Sample();
                s.frame.setVisible(true);
                println "---> Ending code"
            //} // end of run
        //} // end of invoke
        //);
    } // end of main
} // end of class