import javax.swing.*
import java.awt.*;
import javax.swing.JOptionPane;

public String getChoice()
{
    // a jframe here isn't strictly necessary, but it makes the example a little more real
    JFrame frame = new JFrame("Choose a Function Key");
    boolean ok = true;
    String name = "";
    def fk = ["F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12"]

    while(ok)
    {
        // prompt the user to enter their name
        name = JOptionPane.showInputDialog(frame, "Which function key between F1 and F12 ?");
        if (!(name==null))
        {
            name = name.trim().capitalize()
            println "name=|${name}|"

            if (name in fk )
            {
                // get the user's input. note that if they press Cancel, 'name' will be null
                System.out.printf("The user's name is '%s'.\n", name);
                ok = false;
            }
            else
            {  
                println " need Function key between F1,F2,F3...F12"
                JOptionPane.showMessageDialog(frame, "The name of your function key ${name} \nis not in range of F1 thru F12");
            }
        } // end of if
        else
        {
            ok = false;
        } // end of else
    } // end of while
    
    return name;
} // end of method

def xxx = getChoice();    
println "xxx=|${xxx}|\n... the end ..."