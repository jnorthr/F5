package io.jnorthr.toolkit;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

/**
 * This Swing program demonstrates JFrame positional features.
 *
 * It allows JFrame provider logic to move the frame to each corner of the
 * physical screen display using mouse on button or keyboard key backslash '\' or
 * DELETE key
 *
 * @author james.b.northrop@googlemail.com
 *
 */
public class PositionLogic extends ComponentAdapter{
    int windowX = 420; // width
    int windowY = 260; // height
    int windowXv = windowX;
    int windowYv = windowY * 1.1;

    String txt = "Hello World";
    int loc = 0;
    Point p = new Point(0, 0);
    
    /** a dimension of the current hardware screen size */
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    /** a dimension of the current JFrame screen size */
    Dimension windowSize = new Dimension(windowX,windowY);
    Dimension hWindow = new Dimension(windowX,windowY);
    Dimension vWindow = new Dimension(windowXv,windowYv);
    
    public void componentResized(ComponentEvent e) { 
		String nm = e.getComponent().getClass().getName();
        Dimension j = f.getSize();
		nm += "-> Resized as ${j}\n";
        label.setText(nm);            
    }
    
    JFrame f;  // = new JFrame("F5 -");
        
    // Create a JButton with text and icon and set its appearances
    JButton button = new JButton("Hi"); // use setter to set text
    JTextArea label = new JTextArea(10,20);
    JScrollPane sp = new JScrollPane(label);

    /**
     * Default constructor to build a tool to support function key usage to move frame around the hardware screen
     */
    public PositionLogic(JFrame frame){  // throws HeadlessException {
        f = frame;
        f.addComponentListener(this);

        f.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "DELETE");
        f.getRootPane().getActionMap().put("DELETE", moveAction);
        f.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESCAPE_KEY");
        f.getRootPane().getActionMap().put("ESCAPE_KEY", escapeAction);
        f.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SLASH, 0, false), "MOVER");
        f.getRootPane().getActionMap().put("MOVER", moveAction);        
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    } // end of constructor
    

    final AbstractAction escapeAction = new AbstractAction() { 
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent ae) 
        {
            println "... escapeAction done -";
            f.dispose();
        }; // end
    }; // end of escapeAction
    
    // this has all logic to position jframe to bottom, left,right, etc
    final AbstractAction moveAction = new AbstractAction() { 
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent ae) {
        switch(loc)
        {
            // top left
            case 0: 				
                windowSize = f.getSize();
                windowX = -3;
                windowY = +5;
                f.setPreferredSize(hWindow);
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+ " windowX="+windowX+" windowY="+windowY  + "\n"; 
                label.setText(txt);
                p = f.getLocationOnScreen();
                button.setText(txt);
                break;
                
            // middle left
            case 1: 
                f.setPreferredSize(vWindow);
                windowSize = f.getSize();
                windowY = (screenSize.height / 2) - (windowSize.height / 2);
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+ " windowX="+windowX+" windowY="+windowY + "\n"; 
                label.setText(txt);
                p = f.getLocationOnScreen();
                break;
                
            // bottom left
            case 2: 
                f.setPreferredSize(hWindow);
                windowSize = f.getSize();
                windowY = screenSize.height - (windowSize.height + 20);
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+ " windowX="+windowX+" windowY="+windowY+"\n"; 
                label.setText(txt);
                p = f.getLocationOnScreen();
                break;
                
            // bottom center
            case 3: 
                f.setPreferredSize(hWindow);
                windowSize = f.getSize();
                windowX = (screenSize.width / 2) - (windowSize.width / 2);
                windowY = screenSize.height - (windowSize.height + 30);
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+ " windowX="+windowX+" windowY="+windowY+"\n"; 
                label.setText(txt);
                p = f.getLocationOnScreen();
                break;

            // bottom right
            case 4: 
                f.setPreferredSize(hWindow);
                windowSize = f.getSize();
                windowX = screenSize.width - windowSize.width;
                windowY = screenSize.height - (windowSize.height + 30);
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+ " X="+windowX+" Y="+windowY+"\n"; 
                label.setText(txt);
                p = f.getLocationOnScreen();
                break;

            // middle right
            case 5: 
                f.setPreferredSize(vWindow);
                windowSize = f.getSize();
                windowX = screenSize.width - windowSize.width;
                windowY = (screenSize.height / 2) - (windowSize.height / 2);
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+" windowX="+windowX+" windowY="+windowY+"\n"; 
                label.setText(txt);
                p = f.getLocationOnScreen();
                break;

            // top right
            case 6: 
                f.setPreferredSize(vWindow);
                windowSize = f.getSize();
                windowX = screenSize.width - windowSize.width;
                windowY = +5;
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+ " windowX="+windowX+" windowY="+windowY+"\n"; 
                label.setText(txt);
                p = f.getLocationOnScreen();
                break;

            // top center
            case 7: 
                f.setPreferredSize(hWindow);
                windowSize = f.getSize();
                windowX = (screenSize.width / 2) - (windowSize.width / 2);
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+ " windowX="+windowX+" windowY="+windowY+"\n"; 
                label.setText(txt);
                p = f.getLocationOnScreen();
                break;

            // dead center
            case 8: 
                f.setPreferredSize(hWindow);
                f.pack();
                windowSize = f.getSize();
                f.setLocationRelativeTo(null);
                p = f.getLocationOnScreen();
                txt = "ctr: x="+p.x+" y="+p.y+" size:"+windowSize+"\n"; 
                label.setText(txt);
                break;
        } // end of switch
    
        loc +=1;
        if (loc > 8) {loc= 0};
        } // end actionPerformed
    }; // end of moveAction
    
    public setSize(int wide, int high)
    {
        windowX = wide;
        windowY = high;
        windowXv = windowX;
        windowYv = windowY * 1.1;
        windowSize = new Dimension(windowX,windowY);
        hWindow = new Dimension(windowX,windowY);
        vWindow = new Dimension(windowXv,windowYv);
    } // end of setSize
    
    public void setup()
    {
        button.setSize(new Dimension(60, 40));
        button.setText("Hi");    
        button.addActionListener(moveAction); 

        Container cp = f.getContentPane();
        cp.setBackground(Color.RED);
        cp.setLayout(new FlowLayout());
        cp.add(label);  // add label to container      
        cp.add(button); // add button to container      

        windowX = Math.max(0, (screenSize.width) / 2); // horizontal width
        windowY = (screenSize.height / 2); // vertical height
    
        f.setPreferredSize(hWindow);
        f.setSize(hWindow);    
        f.setLocation(windowX,windowY);
        f.pack();
	} // end of setup

    // ===========================================================================    
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
            println "--- Starting PositionLogic"
            JFrame fr = new JFrame("F5 -");
            PositionLogic pl = new PositionLogic(fr);  
            pl.setSize(420,260);
            pl.setup();
            fr.setVisible(true);
            println "--- the end of PositionLogic ---"
	} // end of main
} // end of class
