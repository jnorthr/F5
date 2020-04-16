package io.jnorthr.toolkit;
// groovy sample to choose one folder using java's JFileChooser then write choice into <user>\.F5.txt file
// will only allow choice of a single directory by setting another JFileChooser feature
// http://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
// see more examples in above link to include a file filter
// fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
// fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
// **************************************************************
import io.jnorthr.toolkit.PathFinder;

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


    /** an O/S specific char. as a file path divider */
    String fs = java.io.File.separator;

    /** an O/S specific location for the user's home folder name plus a trailing file separator*/ 
    String home = System.getProperty("user.home")+fs;

    /** a folder-relative location for the user's home folder name */ 
    String metaFileName = home + ".F5.txt";

    /** If we need to println audit log to work, this will be true */ 
    private boolean audit = true;

    /**
     * A path value to influence the JFileChooser as where to allow the user to initially pick a local file artifact.
     * Can be over-written by a value chosen in the previous run of this module. 
     */
    def initialPath = home;
	
	
    /**
     * Handle to component used by the chooser dialog.
     */
    JFileChooser fc = new JFileChooser();
 
    /**
     * This is the title to appear at the top of user's dialog. It confirms what we expect from the user.  
     */
    String menuTitle = "Make a Selection";
    

   // =========================================================================
   /** 
    * Class constructor.
    * defaults to let user pick either a file or a folder
    */
    public Chooser()
    {
    	say("this is an .info msg from the Chooser default constructor");
        initialPath = read();
        
        say "... Chooser() initialPath=|${initialPath}|"
	re = new Response();
    	re.fullname = initialPath;
    	re.path = initialPath;

        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        setup();
    } // endof constructor
    

   /** 
    * Class constructor.
    * defaults to let user pick either a file or a folder
    */
    public Chooser(String newTitle)
    {
    	say("this is an .info msg from the Chooser non-default constructor");
        initialPath = read();

        fc.setDialogTitle(newTitle);
        
        say "... Chooser(String) initialPath=|${initialPath}|"
	re = new Response();
    	re.fullname = initialPath;
    	re.path = initialPath;
    	
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        setup();
    } // endof constructor
    
    
   /**
    * Method to prepare class variables by reading a possibly non-existent cache file written in prior run.
    * It holds name of folder holding payloads for our F5 function key choices.
    */
    public void setup()
    {
    	say "\n... Chooser.setup() has initialPath=|${initialPath}|"
        boolean present = new File(initialPath).exists();
        if (present) 
        { 
        	re.artifact = initialPath; 
        	if (initialPath.size() > 0)
        	{
	        	re.fullname = initialPath;
	        } // end of if
        } // end of if 
        else
        {
        	initialPath = System.getProperty("user.home").toString();
        } // end of else

	say "... Chooser.setup() present=${present} initialPath=|${initialPath}|"

        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        File fi = new File(initialPath);
	fc.setCurrentDirectory(fi);   
    } // endof setup


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
    	say "... Chooser.getChoice() using null"
        re.returncode = fc.showOpenDialog(null) ;
        re.chosen = false;
        
        switch ( re.returncode )
        {
            case JFileChooser.APPROVE_OPTION:
                  File file = fc.getSelectedFile();
		  re.returncode = JFileChooser.APPROVE_OPTION;
		  re.found = file.exists();
		  say "JFileChooser.APPROVE_OPTION chose dir:"+fc.getCurrentDirectory().getAbsolutePath();
		
		  // was this a directory folder ?
                  re.isDir = new File(file.toString()).isDirectory();

                  re.fullname = file.toString();
                  re.path = fc.getCurrentDirectory().getAbsolutePath();
                  re.artifact = (re.isDir) ? "" : file.name; 

                  say "APPROVE path="+re.path+" artifact="+re.artifact+" fullname="+re.fullname+" isDir="+re.isDir;
		  write(re.fullname);                  
                  re.chosen = true;
                  break;

            case JFileChooser.CANCEL_OPTION:
		  re.returncode = JFileChooser.CANCEL_OPTION;
                  re.chosen = false;
                  re.found = false;
                  re.abort = true;
                  say "user cancelled action";
                  break;

            case JFileChooser.ERROR_OPTION:
		  re.returncode = JFileChooser.ERROR_OPTION;
                  re.found = false;
                  re.chosen = false;
                  say "user action caused error";
                  break;
        } // end of switch
        
        return re;
    } // end of pick


   /** 
    * Produce log messages using .info method
    */
    public void say(String msg)
    {
        if (audit) { println msg; } 
    } // end of say
    
    
    /**
     * Method to write String of text to a simple named file; this method will use our own home folder name + file name of .F5.txt
     *
     * @param  String of text to write to file
     * @return true, if write was successful
     */
    public boolean write(String payload) 
    {
	say "--- write(${payload}) into "+metaFileName;
	
    	def file3 = new File(metaFileName)
        boolean present = file3.exists();

        try
        {
            if (payload==null) { payload=""; }
	    file3.withWriter('UTF-8') { writer ->
            	writer.write(payload)
	    } // end of withWriter
            present = true;
        } // end of try
        
        catch(IOException x)
        {
            say "...  write method failed to write <${metaFileName}> due to:"+x.message;
            present = false;
        }

        return present;
    } // end of method



    
    /**
     * Method to read prior PWD string of text from a simple named file .F5.txt; this method will use our own home folder name + file name
     *
     * @return text from user.home/.F5.txt, if read was successful
     */
    public String read() 
    {
	say "--- read() |${metaFileName}| file";
	
    	def file4 = new File(metaFileName)
        boolean present = file4.exists();
	def payload = "";
	
        try
        {
	    payload =  file4.getText('UTF-8').trim();
        } // end of try
        
        catch(IOException x)
        {
            say "...  read method failed to get text from <${metaFileName}> due to:"+x.message;
            present = false;
        }

	say "... read found pwd=|${payload}| in |${metaFileName}|";
        return payload;
    } // end of method

    
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
	// ---------------------------------------------------------------------
	/*
	* To test the feature to allow user to choose a folder name 
	*/    
	Response re;  
    
	// ---------------------------------------------------------------------
	/*
	 * need to test selecting folders only
	 */
        Chooser ch = new Chooser("Pick input Folder");
        ch.say "Pick a folder-only test"
        
        re = ch.getChoice();
    	ch.say ""; 
    	if (re.abort)
    	{
    	    ch.say "user clicked 'cancel' button"
    	} // end of if
    	
	ch.say re.toString();

        if (re.chosen && !re.abort)
        {
            ch.say "path="+re.path+"\nfile name="+re.artifact;    
            ch.say "the full name of the selected folder is "+re.fullname;    
            ch.say "isDir ? = "+re.isDir;    
        }
        else
        {
            ch.say "no choice was made so folder will be "+re.path+" and name="+re.fullname;
	}
	
	ch.say "------------------------\n";

       System.exit(0);
    } // end of main
   
    
} // end of class