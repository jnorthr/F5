//package net.codejava.swing;
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
public class PositionLogicTest
{    
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
            JFrame fr = new JFrame("F5 -");
            PositionLogic pl = new PositionLogic(fr);  
            pl.setSize(800,600);
            //pl.setup();
            fr.setVisible(true);

            println "--- the end of PositionLogicTest ---"
	} // end of main

} // end of class
   