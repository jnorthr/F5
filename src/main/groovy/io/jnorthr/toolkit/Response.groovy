package io.jnorthr.toolkit;

//import org.apache.log4j.*
//import groovy.util.logging.*  

/**
* A class that contains results of most recent user dialog
*
* Use annotation to inject log field into the class.
*
* @author  jnorthr
* @version 1.1
* @since   2020-08-27
*/
//@Log4j
public class Response
{
    /**
     * t/f indicator of the user's inter-action with the dialog. True when user hits JFileSaver.APPROVE_OPTION
     */
    boolean chosen = false;

    /**
     * Integer indicator of the user's inter-action with the chooser. For example: JFileChooser.APPROVE_OPTION
     */
    int returncode = -1;

    /**
     * Flag set when user hits cancel button on dialog 
     */
    boolean abort = false;

    /**
     * Flag set when user hits button on dialog to end it - red button topleft 
     */
    boolean eof = false;

    /**
     * Text explanation of the user's inter-action with the GUI or interpretation of the returncode
     */
	String answer = "";
	
    /**
     * Temp work area holding the absolute path to the user's artifact selected with the chooser. 
     * For example: fc.getCurrentDirectory().getAbsolutePath() 
	 *
	 * example choosing a file artifact:
	 * APPROVE path=/Users/jimnorthrop/Dropbox/Projects/Web/config artifact=logback.xml 
	 * fullname=/Users/jimnorthrop/Dropbox/Projects/Web/config/logback.xml 
	 * rememberpath=/Users/jimnorthrop/.path.txt 
	 * isDir=false
     */
    def path = System.getProperty("user.home");


    /**
     * Temp work area holding only the name of the file the user's artifact selected with the chooser, 
     * but not it's path. Holds a value when Directory_Only choice is in effect of lowest level folder name
     * and parent path is in 'path' variable above.
     */
    def artifact = System.getProperty("user.home");
    
    /**
     * Temp work area holding the full and complete absolute path plus file name of the user's artifact 
     * selected with the chooser. 
     */
    def fullname = System.getProperty("user.home");


    /**
     * Flag set when name of the user's artifact selected with the chooser is a folder directory 
     */
    boolean isDir = true;

    /**
     * Flag set when name of the user's artifact points to an actual file or folder that really does exist 
     */
    boolean found = true;


    /**
      * default class constructor
      */
    public Response()
    {
    }

    /**
      * class method to pump out log entries as 'info'
      */
    def say()
    {
	   println this.toString();
    }

    /**
      * class toString() method
      */
        String toString() {
"""
... chosen    =${chosen}
... returncode=${returncode}
... cancelled =${abort}
... eof       =${eof}
... answer    =${answer}
... path      =${path}
... artifact  =${artifact} 
... fullname  =${fullname}
... isDir     =${isDir}
... found     =${found}
""".toString()
         } // end of toString()    
         

    // =============================================================================    
    /**
     * The primary method to execute this class. Can be used to test and examine logic and performance issues. 
     * 
     * argument is a list of strings provided as command-line parameters. 
     * 
     * @param  args a list of possibly zero entries from the command line
     */
    public static void main(String[] args)
    {
        def obj = new Response();
        println "Response object="+obj.toString();
        println "--- Response end ---\n";
	} // end of main
	
} // end of class 