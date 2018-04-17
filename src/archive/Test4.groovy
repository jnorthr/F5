import javax.swing.*
import java.awt.*;
import java.io.*;
import java.awt.event.*;

boolean ok = false;
LayoutManager H = new GridLayout(1, 0);
LayoutManager V = new GridLayout(0, 1);
    
/** a handle for our GUI framework */
def f = new JFrame();
f.setTitle("F5 -> Create a payload for this key"); 

//Lay out the label and scroll pane from top to bottom.
JPanel listPane = new JPanel();
listPane.setLayout(H);  // BoxLayout.PAGE_AXIS
JLabel label = new JLabel("Hi kids");
JLabel label2 = new JLabel("Mary Freedom");
JButton b=new JButton("\u21E9");  
b.setSize(80,30);  

listPane.add(label);
listPane.add(label2);
listPane.add(b);
b.addActionListener(new ActionListener()
{
  public void actionPerformed(ActionEvent e)
  {
    b.setText("!");
    ok = !ok;
    if (ok) {listPane.setLayout(V);b.setText("\u21E8");}
    if (!ok) {listPane.setLayout(H);b.setText("\u21E9");}
    listPane.validate();
  }
});

f.add(listPane);
f.addWindowListener(new WindowAdapter() 
{
        //@Override 
        public void windowIconified(WindowEvent e) 
        {
            println "... window icon"
            ok = !ok;
            if (ok) {listPane.setLayout(V);}
            if (!ok) {listPane.setLayout(H);}
            listPane.validate();
        }

        @Override 
        public void windowDeiconified(WindowEvent e) 
        {
            println "... window deicon"
        }
        
        //@Override
        public void windowClosing(WindowEvent e) 
        {
            println "... window close"
            f.dispose();
            //System.exit(0);
        }

    });    

f.setSize(200,100);  
f.setVisible(true);      
  