//package net.codejava.swing;
package io.jnorthr.toolkit;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
 
/**
 * This Swing program demonstrates JFrame positional features.
 *
 * @author james.b.northrop@googlemail.com
 *
 */
public class PositionLogic{
    int windowX = 250;
    int windowY = 80;
    int windowXv = windowX / 1.1;
    int windowYv = windowY * 1.5;

    String txt = "Hello World";
    int loc = 0;
    Point p = new Point(0, 0);
    
    /** a dimension of the current hardware screen size */
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    /** a dimension of the current JFrame screen size */
    Dimension windowSize = new Dimension(windowX,windowY);
    Dimension hWindow = new Dimension(windowX,windowY);
    Dimension vWindow = new Dimension(windowXv,windowYv);

    JFrame f = new JFrame("F5 -");
        
    // Create a JButton with text and icon and set its appearances
    JButton button = new JButton(); // use setter to set text
    JLabel label = new JLabel("Hi kids");
        
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
                f.setPreferredSize(vWindow);
                f.setSize(vWindow);
                windowX = -3;
                windowY = +5;
                f.setPreferredSize(hWindow);
                f.setSize(hWindow);
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+ " windowX="+windowX+" windowY="+windowY; 
                button.setText(txt);
                p = f.getLocationOnScreen();
                println  = "... loc ="+loc+ " p.x ="+p.x+" p.y="+p.y; 
                button.setText(txt);
                break;
                
            // middle left
            case 1: 
                f.setPreferredSize(vWindow);
                f.setSize(vWindow);
                windowSize = f.getSize();
                windowX = -3;
                windowY = (screenSize.height / 2) - (windowSize.height / 2);
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+ " windowX="+windowX+" windowY="+windowY; 
                button.setText(txt);
                p = f.getLocationOnScreen();
                println "... loc ="+loc+ " p.x ="+p.x+" p.y="+p.y; 
                break;
                
            // bottom left
            case 2: 
                f.setPreferredSize(hWindow);
                f.setSize(hWindow);
                windowSize = f.getSize();
                windowX = -3;
                windowY = screenSize.height - (windowSize.height + 40);
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+ " windowX="+windowX+" windowY="+windowY; 
                button.setText(txt);
                p = f.getLocationOnScreen();
                println "... loc ="+loc+ " p.x ="+p.x+" p.y="+p.y; 
                break;
                
            // bottom center
            case 3: 
                f.setPreferredSize(hWindow);
                f.setSize(hWindow);
                windowSize = f.getSize();
                //f.pack();
                windowX = (screenSize.width / 2) - (windowSize.width / 2);
                windowY = screenSize.height - (windowSize.height + 40);
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+ " windowX="+windowX+" windowY="+windowY; 
                button.setText(txt);
                p = f.getLocationOnScreen();
                println "... loc ="+loc+ " p.x ="+p.x+" p.y="+p.y; 
                break;

            // bottom right
            case 4: 
                f.setPreferredSize(hWindow);
                f.setSize(hWindow);
                windowSize = f.getSize();
                windowX = screenSize.width - windowSize.width;
                windowY = screenSize.height - (windowSize.height + 40);
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+ " X="+windowX+" Y="+windowY; 
                button.setText(txt);
                p = f.getLocationOnScreen();
                println "... loc ="+loc+ " p.x ="+p.x+" p.y="+p.y; 
                break;

            // middle right
            case 5: 
                f.setPreferredSize(vWindow);
                f.setSize(vWindow);
                windowSize = f.getSize();
                windowX = screenSize.width - windowSize.width;
                windowY = (screenSize.height / 2) - (windowSize.height / 2);
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+" windowX="+windowX+" windowY="+windowY; 
                button.setText(txt);
                p = f.getLocationOnScreen();
                println "... loc ="+loc+ " p.x ="+p.x+" p.y="+p.y; 
                break;

            // top right
            case 6: 
                f.setPreferredSize(vWindow);
                f.setSize(vWindow);
                windowSize = f.getSize();
                windowX = screenSize.width - windowSize.width;
                windowY = +5;
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+ " windowX="+windowX+" windowY="+windowY; 
                button.setText(txt);
                p = f.getLocationOnScreen();
                println "... loc ="+loc+ " p.x ="+p.x+" p.y="+p.y; 
                break;

            // top center
            case 7: 
                f.setPreferredSize(hWindow);
                f.setSize(hWindow);
                windowSize = f.getSize();
                windowX = (screenSize.width / 2) - (windowSize.width / 2);
                windowY = +5;
                f.setLocation(windowX,windowY);
                f.pack();
                txt = "@"+loc+ " windowX="+windowX+" windowY="+windowY; 
                button.setText(txt);
                p = f.getLocationOnScreen();
                println "... loc ="+loc+ " p.x ="+p.x+" p.y="+p.y; 
                break;

            // dead center
            case 8: 
                f.setPreferredSize(hWindow);
                f.setSize(hWindow);
                f.pack();
                windowSize = f.getSize();
                f.setLocationRelativeTo(null);
                p = f.getLocationOnScreen();
                txt = "ctr: x="+p.x+" y="+p.y; 
                button.setText(txt);
                println "  ... screenSize="+screenSize;
                break;
        } // end of switch
    
        loc +=1;
        if (loc > 8) {loc= 0};
        } // end actionPerformed
    }; // end of moveAction
    
    println "screenSize="+screenSize; // screenSize=java.awt.Dimension[width=1280,height=720]
    println "windowSize="+windowSize; // windowSize=java.awt.Dimension[width=120,height=44]

    button.setSize(new Dimension(120, 90));
    button.setText(txt);
    
    f.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SLASH, 0, false), "MOVER");
    f.getRootPane().getActionMap().put("MOVER", moveAction);
    button.addActionListener(moveAction); // makes escape key use same action as mouse does for this button

    Container cp = f.getContentPane();
    cp.setBackground(Color.RED);
    cp.setLayout(new FlowLayout());
    cp.add(label);  // add label to container      
    cp.add(button); // add button to container      

    windowX = Math.max(0, (screenSize.width) / 2); // horizontal width
    windowY = (screenSize.height / 2); // vertical height
    
    f.setPreferredSize(hWindow);
    f.setSize(hWindow);
    
    f.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "DELETE");
    f.getRootPane().getActionMap().put("DELETE", moveAction);
    f.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESCAPE_KEY");
    f.getRootPane().getActionMap().put("ESCAPE_KEY", escapeAction);
    f.setLocation(windowX,windowY);
    f.pack();
    f.setVisible(true);
    
 

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
			    PositionLogic pl = new PositionLogic();  //.setVisible(true);
			    println "--- the end ---";
			} // end of run
		}
		);

		//new F5().setVisible(true);
		println "--- the end of PositionLogic ---"
	} // end of main

} // end of class
   