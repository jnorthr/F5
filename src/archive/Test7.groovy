package io.jnorthr.toolkit;
import javax.swing.*

/*
 * Feature to read text from a File
 */
public class Test7
{
    /** a handle for our input/output framework */
    IO io = new IO(true);
    String functionkey = "F12";
    String copybookFound = "";
    String copybookFilename = "";

    public Test7()
    {
        copybookFound = io.reset(functionkey);
        //copybookFound = io.pf.hasFunctionKeyFileName(functionkey);
        copybookFilename = io.copybookFilename;
    } // end of constructor
    
/*
 * A method to ask our user to provide a value for unknown keys within a Groovy Template-provided string 
 */
String prompt(String i) {
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
        println "Hello from Test7.groovy"
        Test7 ck = new Test7();
        println "... copybookFound="+ck.copybookFound;
        println "... copybookFilename="+ck.copybookFilename;
        println "--- the end---"
    } // end of main 

} // end of class

/*
Hello from Test7.groovy
... valid(String |F12|) called.
... reset(F12) = true
-------------------------|
 IO functionkey         =|F12|
    copybookFound       =|false|
    copybookFilename    =|/Users/jim/copybooksF12.txt|
    tooltip             =||
    payload             =||
    PathFinder variables:
    pf.currentDirectory =|/Users/jim/Dropbox/Projects/F5/|
    pf.home             =|/Users/jim/|
    pf.homePath         =|/Users/jim/|
    pf.metafile         =|/Users/jim/.F5.txt|
    pf.metaFound        =|true|
    pf.copyPathFound    =|true|
    pf.copyPathDirectory=|/Users/jim/copybooks|
 
... copybookFound=false
... copybookFilename=/Users/jim/copybooksF12.txt
--- the end---
*/