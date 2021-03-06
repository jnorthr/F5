package io.jnorthr.toolkit;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.*;
 
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
 
public class MenuAccessibility 
{ 
    public static void main(String[] args) 
	{
		// Create frame with specific title 
		JFrame frame = new JFrame("Example Frame");
		frame.dispose();
		/* 
		* Create a container with a flow layout, which arranges its children
		* horizontally and center aligned. A container can also be created with
		* a specific layout using Panel(LayoutManager) constructor, e.g.
		* Panel(new FlowLayout(FlowLayout.RIGHT)) for right alignment
		*/
 
		Panel panel = new Panel();
 
		// Create a Menu
		JMenu menu = new JMenu("Menu");
 
		// Set a mnemonic for the menu. This makes all the menus and menu items accessible
		menu.setMnemonic('m');
 
		// Create a menu item
		JMenuItem menuItem = new JMenuItem("Item");
		
		// Set an accelerator key for the menu item 	
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.SHIFT_MASK));
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
 
		// Add the item to the menu 
		menu.add(menuItem);
 
		// Create a MenuBar and add the menu
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);
 
		// Create a component to add to the panel; in this case a text field with sample text
		Component nameField = new TextField("Enter your name");
 
		// Create a component to add to the panel; in this case a label for the name text field
		JLabel nameLabel = new JLabel("Name:");
 
		// Set a mnemonic on the label. The associated component will get the focus when the mnemonic is activated
		char nn = 'N';
		nameLabel.setDisplayedMnemonic(nn);
 
		// make the association explicit
		nameLabel.setLabelFor(nameField);
		
		// Add label and field to the container
		panel.add(nameLabel);
		panel.add(nameField);
 
		// Create a component to add to the frame; in this case an image button - change to where your image file is located
		JButton button = new JButton(new ImageIcon("image.png"));
 
		// The tool tip text, if set, serves as the accessible name for the button
		button.setToolTipText("Button Name");
 
		// If tool tip is being used for something else, set the accessible name.
		button.getAccessibleContext().setAccessibleName("Button Name");
 
		// Set mnemonic for the button
		button.setMnemonic('B');
 
		// Add the components to the frame; by default, the frame has a border layout
		frame.setJMenuBar(menuBar);
		frame.add(panel, BorderLayout.NORTH);
		frame.add(button, BorderLayout.SOUTH);
 
		// Display the frame
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);		
		int frameWidth = 300;
		int frameHeight = 300;
		frame.setSize(frameWidth, frameHeight); 	
		frame.setVisible(true);
    } // end of main 
} // end of class
