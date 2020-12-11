package io.jnorthr.toolkit;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
* The Chooser program implements a support application that
* allows user to pick a single file or single folder directory.
*
* Initially starts to choose artifacts from program working directory and saves user
* choice of path in a local text file 
*
* Use annotation to inject log field into the class.
*
* @author  jnorthr
* @version 1.0
* @since   2016-08-01 
*/
public class Chooser 
{
    /**
     * A class reflecting values chosen by the user of JFileChooser.
     */
    Response re;

    // Offers access to app-specific and system metadata & values
    PathFinder pf = new PathFinder();

    /** If we need to println audit log to work, this will be true */ 
    private boolean audit = false;
	
    /**
     * Handle to component used by the chooser dialog.
     */
    JFileChooser fc = new JFileChooser();
 

   // =========================================================================
   /** 
    * Private Class constructor.
    */
    private Chooser()
    {
    } // end of private constructor
    

   /** 
    * Class constructor.
    * defaults to let user pick either a file or a folder
    */
    public Chooser(String newTitle)
    {
        fc.setDialogTitle(newTitle);
        say "... Chooser(String) initialPath=|${pf.home}|"
  	    re = new Response();
      	re.fullname = pf.home;
      	re.path = pf.home;
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //found = pf.fnFound;
    } // endof constructor
    

    // =============================================================================
    /**
     * Returns a Response object to indicate what the user did in the JFileChooser dialog. 
     * 
     * This method always returns true if user clicked the APPROVE button indicating 
     * an actual choice was made else returns false if user aborted and failed to make a choice.
     *
     * @return Response object including a boolean true if user clicked the APPROVE button
     *                false if user did not make a choice
     */
    public Response getChoice()
    {
        re.found = false;
        re.chosen = false;
        re.abort = false;
        re.eof = false;

        re.returncode = fc.showOpenDialog(null) ;

        switch ( re.returncode )
        {
            case JFileChooser.APPROVE_OPTION:
				File file = fc.getSelectedFile();
		        re.found = file.exists();

		        say "JFileChooser.APPROVE_OPTION chose dir:"+fc.getCurrentDirectory().getAbsolutePath();
				say "fc.getSelectedFile()="+fc.getSelectedFile().toString();
                // was this a directory folder ?
                re.isDir = new File(file.toString()).isDirectory();

                re.fullname = file.toString().trim();
                re.path = fc.getCurrentDirectory().getAbsolutePath().trim();

                def ct = re.fullname.count("/"); 
                ct = re.fullname.lastIndexOf("/")
                re.artifact = re.fullname.substring(ct+1);

                say "APPROVE path="+re.path+" artifact="+re.artifact+" fullname="+re.fullname+" isDir="+re.isDir;

		        pf.write(re.fullname.trim());                  
                re.chosen = true;
                say "response="+re.toString();
                break;

            case JFileChooser.CANCEL_OPTION:
                  re.abort = true;
                  say "user cancelled action";
                  break;

            case JFileChooser.ERROR_OPTION:
                  say "user action caused error";
                  re.abort = true;
                  re.eof = true;
                  break;

            default:
                  say "user action caused unknown response:"+re.returncode;
                  re.eof = true;
                  break;

        } // end of switch
        
        say "... Chooser.getChoice() exited with response="+re.returncode;
        return re;
    } // end of pick


   /** 
    * Produce log messages using .info method
    */
    public void say(String msg)
    {
        if (audit) { println msg; } 
    } // end of say
    
    
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
	/*
	 * To test the feature to allow user to choose a folder name 
	 */    
	    Response re;  
    
	// ---------------------------------------------------------------------
	/*
	 * need to test selecting folders only
	 */
        Chooser ch = new Chooser("Pick input Folder");
        println "Pick a folder-only test"
        
        re = ch.getChoice();
        println ""; 
        if (re.abort)
        {
          println "user clicked 'cancel' button"
        } // end of if
    	
  	    ch.say re.toString();

        if (re.chosen && !re.abort)
        {
            println "path="+re.path+"\nfile name="+re.artifact;    
            println "the full name of the selected folder is "+re.fullname;    
            println "isDir ? = "+re.isDir;    
        }
        else
        {
            println "no choice was made so folder will be "+re.path+" and name="+re.fullname;
        }
	
        println "------------------------\n";

        System.exit(0);
    } // end of main
       
} // end of class
