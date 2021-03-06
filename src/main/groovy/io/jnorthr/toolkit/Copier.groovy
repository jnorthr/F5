package io.jnorthr.toolkit;

import groovy.transform.*;
import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

import java.awt.Image;
import java.io.IOException;

/*
 * Feature to copy text to system clipboard
 */
public class Copier
{    
    /** If we need to println audit log to work, this will be true */ 
    boolean audit = false;

    /**
     * Default constructor builds a tool to interact with the System Clipboard for most operatng systems
     */
    public Copier()
    {
    } // end of default constructor


    /**
     * A method to print an audit log if audit flag is true
     *
     * @param  is text to show user via println
     * @return void
     */
    public void say(String text)
    {
        if (audit) { println text; }
    } // end of method

    /**
     * Method to place provided String of text on the System Clipboard for most operating systems
     *
     * @param  text string to copy onto system clipboard
     * @return void
     */
    public void paste(String s) 
    {
        say "... Copier paste() putting "+s.size()+" bytes on clipboard"
        ClipboardOwner owner = null;
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferable = new StringSelection(s);
        clipboard.setContents(transferable, owner);
    } // end of method


    /**
     * Method to retrieve a String of text from the System Clipboard for most operatng systems
     *
     * @return text currently held on System Clipboard
     */
    public String copy() 
    {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        DataFlavor flavor = DataFlavor.stringFlavor;
        String text = " - could not paste from clipboard"
        say "... Copier copy() reading payload of  "+text.size()+" bytes from system clipboard"
        if (clipboard.isDataFlavorAvailable(flavor)) 
        {
            try 
            {
                text = (String)clipboard.getData(flavor);
            } 
            catch (UnsupportedFlavorException e) 
            {
                text = e.getMessage();
            } 
            catch (IOException e) 
            {
                text = e.getMessage();
            }
        } // end of if
        
        return text;
    } // end of method


    /**
     * Method to copy provided image data on the System Clipboard for most operatng systems
     * into java variable This method can help handle the "paste" portion of a copy and paste operation.
     *
     * @return Image object
     */
    public Image copyImageFromClipboard() throws Exception
    {
        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor))
        {
          say "... Copier copyImageFromClipboard() reading image from system clipboard"
          return (Image) transferable.getTransferData(DataFlavor.imageFlavor);
        }
        else
        {
          say "... Copier copyImageFromClipboard() did not find an imageFlavor on system clipboard"
          return null;
        }
    } // end of method
        

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
        println "Hello from Copier.groovy"
        Copier ck = new Copier();

        ck.paste("Hi from Copier.groovy");
        // writeToClipboard(textArea.getText(), null);
        
        assert "Hi from Copier.groovy" == ck.copy();

        println "--- the end---"
    } // end of main 

} // end of class