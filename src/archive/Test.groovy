import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
 
/** Test setting Swing's JComponents properties and appearances */
@SuppressWarnings("serial")
public class SwingJComponentSetterTest extends JFrame implements MouseListener { 
 
    // Image path relative to the project root (i.e., bin) for icons
    boolean ok = false;
    boolean lr = false; // left/right side of screen to move to
    
    JTextArea textField = new JTextArea("Text Field", 10, 20);

    //int offset = 100;
    //int wide = 440;
    //int high = 150;
    int windowX = 0;
    int windowY = 0;
    
    /** a dimension of the current hardware screen size */
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    /** a dimension of the current JFrame screen size */
    Dimension windowSize = null;
      
    final AbstractAction escapeAction = new AbstractAction() { 
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent ae) {
            println "... escapeAction done -"
            dispose();
        } 
    };

    // this has all logic to position jframe to bottom, left,right, etc
    final AbstractAction moveAction = new AbstractAction() { 
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent ae) {
            //wide = 440;
            //high = 150;
            if (ok)
            {
                // sets panel as horizontal
                //setLocationRelativeTo(null); // center window on the screen
                //setSize(wide, high);
                windowX = Math.max(0, (screenSize.width) / 2); // horizontal width
                windowY = (screenSize.height / 2); // vertical height
                setLocation(windowX,windowY);
                ok = false;
                windowSize  = getSize();
                textField.append("\nHorizontal Size   : " + windowSize)
            }
            else
            {
                // set panel as vertical
                //wide = 150;
                //high = 440
                //setSize(wide, high);
                windowY = ( screenSize.height / 2 ); 
                
                ok = true;
                if (lr)
                {
                    lr = false;
                    windowX = 0;   // Math.max(0, 5); //(screenSize.width - windowSize.width)/2); // horizontal width position
                }
                else
                {
                    lr = true;
                    windowX = screenSize.width - 66;//Math.max(0, (screenSize.width / 2 )); // horizontal width position
                } // end of else
                windowSize = getSize();
                textField.append("\nVertical Size   : " + windowSize)
                
            } // end of else

            setLocation(windowX,windowY);

        } // end 
    };


    // because class implements MouseListener, we need these 5 methods to handle mouse activity
    public void mouseClicked(MouseEvent e) {  
        textField.append("\nMouse Clicked");  // COMMENT as it overwrites 'Mouse Pressed' text
    }  
    public void mouseEntered(MouseEvent e) {  
        //textField.append("\nMouse Entered");  
    }  
    public void mouseExited(MouseEvent e) {  
        //textField.append("\nMouse Exited");  
    }  
    public void mousePressed(MouseEvent e) {  
        textField.append("\nMouse Pressed");  
    }  
    public void mouseReleased(MouseEvent e) {  
        textField.append("\nMouse Released");  
    }  
    
   /** Constructor to setup the GUI */
   public SwingJComponentSetterTest() {
   
      // logic to make this JFrame use mouse logic methods declared above
      addMouseListener(this);  
         
      // Prepare ImageIcons to be used with JComponents
 
      Container cp = getContentPane();
      cp.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
 
      // Create a JLabel with text and icon and set its appearances
      JButton label = new JButton("JButton 1");
      label.setFont(new Font(Font.DIALOG, Font.ITALIC, 12));
      label.setOpaque(true);  // needed for JLabel to show the background color
      label.setBackground(Color.BLACK);  // light black
      label.setForeground(Color.RED);    // foreground text color
      //label.setPreferredSize(new Dimension(50, 20));
      label.setToolTipText("This is a JButton");  // Tool tip
      label.addActionListener(moveAction); // makes escape key use same action as mouse does for this button
      cp.add(label);
 
      // Create a JButton with text and icon and set its appearances
      JButton button = new JButton(); // use setter to set text and icon
      //button.setText("Button"); // 
      //button.setHorizontalTextPosition(SwingConstants.CENTER); // of text relative to icon
      //button.setVerticalTextPosition(SwingConstants.BOTTOM);    // of text relative to icon

      //button.setIcon(iconNought);
      button.setVerticalAlignment(SwingConstants.CENTER);  // of text and icon
      button.setHorizontalAlignment(SwingConstants.CENTER); // of text and icon

      //button.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
      //button.setBackground(Color.YELLOW);
      //button.setForeground(Color.RED); // AFFECTS COLOR OF TEXT, if setText() is set
      button.setOpaque(true);  // needed for JLabel to show the background color: false won't show setBackground(Color.YELLOW)
      button.setPreferredSize(new Dimension(80, 60));
      button.setToolTipText("This is a JButton");
      // setTitle("Hello World"); only works inside an action to change JFrame heading
      
      //button.addMouseListener(this);  dunno as won't do listener logic on button when used somewhere else

      // logic here is to capture key press activity
      button.setMnemonic(KeyEvent.VK_ESCAPE);  
      button.addActionListener(escapeAction); // makes escape key use same action as mouse does for this button
      //button.addMouseListener(escapeAction);  // have not figured this out yet
      cp.add(button); // add button to container      
 
      // Create a JTextField with text and icon and set its appearances
      textField.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 12));
      textField.setForeground(Color.RED);
      //textField.setHorizontalAlignment(JTextField.RIGHT);  // Text alignment
      textField.setToolTipText("This is a JTextArea");
        
      cp.add(textField);
 
      // logic to improve JFrame follows :
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      setTitle("JComponent Test");
      setLocationRelativeTo(null); // center window on the screen
      //setSize(wide, high);  // or pack()
      setVisible(true);
      this.setBackground(Color.YELLOW); // changes panel heading color
      
      // these 2 lines of code marry this keystroke to an abstract action to the keyboard escape key for this JFrame instance

      getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "BACK_SPACE_KEY");
      getRootPane().getActionMap().put("BACK_SPACE_KEY", moveAction);
      pack();
      windowSize = getSize();
      textField.append("\nFrame Size   : wide=" + windowSize.width + " high="+windowSize.height);
      textField.append("\nLocated at x="+getLocation().x);
      textField.append("\nLocated at y="+getLocation().y);
		
      //setLocationRelativeTo(null); // to center panel on screen
      int windowX = Math.max(0, (screenSize.width)); // horizontal width
      int windowY = (screenSize.height); // vertical height

      //setLocation(windowX,windowY);
      getContentPane().setBackground(Color.PINK); // sets color of jframe content panel background
      
      // Print description of the JComponents via toString()
      //System.out.println(label);
      //System.out.println(button);
      //System.out.println(textField);
   } // end of constructor
 
   /** The entry main() method */
   public static void main(String[] args) {
      // Run the GUI codes on Event-Dispatching thread for thread safety
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
             def obj = new SwingJComponentSetterTest(); // Let the constructor do the job
         }
      });
   }
}