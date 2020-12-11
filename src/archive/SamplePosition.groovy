import java.awt.Dimension;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.*;

Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
JFrame frame = new JFrame("Dimensions Test");
JButton quitbutton = new JButton("Quit");

/** a dimension of the current F5 screen size */
Dimension windowSize = null;

println "--- the start ---"
frame.setSize(760, 80);
frame.setPreferredSize(new Dimension(760, 80));

windowSize = frame.getSize();
int windowX = Math.max(0, (screenSize.width  - windowSize.width ) / 2);
int windowY = Math.max(0, (screenSize.height - windowSize.height)) - 80;

frame.setLocation(windowX, windowY);
frame.revalidate();
//windowY = screenSize.height - (windowSize.height+40);
println "... windowSize.width=${windowSize.width} and windowSize.height=${windowSize.height}"
println "... screenSize.width=${screenSize.width} and screenSize.height=${screenSize.height}"
		
frame.setVisible(true);
//this.setLocation(windowX, windowY);  

frame.setSize(80, 360);
frame.setLocation(100, 200);
frame.revalidate();

println "--- the end  ---"