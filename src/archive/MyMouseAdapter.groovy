import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.*;
import javax.swing.*;

public class MouseAdapter extends Object implements MouseListener  //extends MouseAdapter
{
    public void mouseClicked(MouseEvent e)
    {
        println "MyMouseAdapter.mouseClicked = "+e;
    } // end of 
} // end of class
    
public class MyMouseTester
{
    public static void main(String[] args) 
    {        
        MouseAdapter mma = new MouseAdapter();
        JFrame fr = new JFrame("Mice Test");
        JLabel lab = new JLabel("Rodents");
        fr.setSize( 400, 400 );
        fr.add(lab);
        fr.setVisible(true);
        mma.mouseClicked(fr);
        println "--- the end of job ---"
    } // end of main

} // end of class
