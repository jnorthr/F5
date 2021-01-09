import javax.swing.*
import java.awt.*;
// tut: https://www.developer.com/java/data/how-to-code-java-clipboard-functionality.html
import java.awt.event.*;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.*;
import java.awt.image.*;

import io.jnorthr.toolkit.actions.*;

/*   
public abstract class CopyImagetoClipBoard implements ClipboardOwner
{
    public void copyImage(BufferedImage bi)
    {
        // TransferableImage trans = new TransferableImage( bi ); <-- research this class more
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        c.setContents( trans, this );
    }

    public void addImage() {
        BufferedImage bim;
        // set bim to your desired BufferedImage content
        // ...
        //CopyImagetoClipBoard ci = new CopyImagetoClipBoard(); <-- fix abstract class & implement logic there 
        ci.copyImage(bim);
    } // end of method
    
} // END OF CLASS
*/

public class TestGUI extends JFrame // implements KeyListener
{
    JPanel tp = new JPanel();
    JTextArea ta = new JTextArea(20,50);
    KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_F1, Event.CTRL_MASK);
    KeyStroke f1 = KeyStroke.getKeyStroke(KeyEvent.VK_1, Event.CTRL_MASK);

    /** a handle for our input/output framework */
    public TestGUI(){
        super("This is TestGUI");
        ta.setLineWrap(true);
        ta.setEditable(true); 
      	 ta.setBackground(Color.YELLOW);
        ta.setText("Hi kids");
        
        ta.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(stroke, "F1");
        def icon7 = new ImageIcon("images/ESC.png");           
        def fromAction = new FromClipboardAction("From",icon7,"FROM",new Integer(KeyEvent.VK_F1)); 
        ta.getActionMap().put(f1, fromAction);
        //ta.getActionMap().put("F1", fromAction);
        //ta.setAccelerator(f1);
        tp.add(ta);
        
        getContentPane().add(tp,BorderLayout.CENTER);
        this.setSize(800,300);
    } // end of constructor
    
    private static int getMenuShortcutKeyMask() {
    // #152050 - work in headless environment too
        try {
            if (!GraphicsEnvironment.isHeadless()) {
              return Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
            }
        } catch (Throwable ex) 
        {
            // OK, just assume we are headless
        }
        return Event.CTRL_MASK;
    } // end of method
    
    public static void main(String[] args){
        TestGUI test = new TestGUI();
        int x = test.getMenuShortcutKeyMask();
        println "\n... mask int ="+x;
        if (x==Event.CTRL_MASK) { println "... CTRL_MASK";}
        test.setVisible(true);
        println "--- the end ---"
    } // end of main
} // end of class