package io.jnorthr.toolkit;

import java.io.*
import java.io.File;
import java.nio.file.*;
import java.io.IOException;
//import java.nio.file.Files;

/**
* This program implements a support application that gains system & user values for directory and folder names,
* confirms the existance of an optional text file  .F5.txt, and if that exists and has a text value that is also a valid
* system directory folder name with the name of a valid 'copybooks' folder, then copyPathFound boolean becomes true
*
* Use annotation to inject log field into the class.
*
* @author  jnorthr
* @version 1.1
* @since   2020-08-22
*/
public class PathFinder 
{       
    /** an O/S specific char. as a file path divider */
    String fs = java.io.File.separator;

    ChooseCopybooksFolder ccf = new ChooseCopybooksFolder();
    
    /**
     * This name points to current platform-independent folder path location the user is now working in, i.e. user.dir
     */
    String currentDirectory = ccf.currentUsersWorkDir;

    /**
     * This name points to platform-independent folder path location of the user's core folder, i.e. user.home  
     */
    String homePath = ccf.currentUsersHomeDir;

    // set true if copybook directory found in currentDirectory
    boolean useCurrent = ccf.workCopybooks;
    boolean useHome = ccf.homeCopybooks;
    

    /** an O/S specific location for the user's home folder name plus a trailing file separator
     * plus the 'copybook' group folder name - something like /Users/jim/copybooks which holds a
     * bunch of payload files, one per function key and named 'function key name' + .txt so
     * for example function key twelve payload is named '/Users/jim/copybooks/F12.txt'
     */ 
    String  copyPathDirectory = "";
        
    /** If we need to println audit log to work, this will be true */ 
    boolean audit = false;    
            
   // =========================================================================
   /** 
    * Class constructor.
    *
    * defaults to provide user.home & user.dir paths plus location of our 'copybooks' folder in currentFolderDirectory 
    */
    public PathFinder()
    {
        copyPathDirectory = ccf.getFolderName();
    } // end of constructor

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
    * Provides the calling logic with a platform-independent pointer to the current folder the user is working in
    */
    public String getCurrentDirectory()
    {
        return currentDirectory;
    } // end of method


   /** 
    * Provides the calling logic with a platform-independent pointer to the folder the present user owns
    */
    public String getHomePath()
    {
        return homePath;
    } // end of method


   /** 
    * Provides the calling logic with a platform-independent pointer to the copybook folder the user has for his
    * function key payload files
    */
    public String getCopyPathDirectory()
    {
        return copyPathDirectory;
    } // end of method
    

    /* return full path & filename for a target payload file for one function key
     * may or maynot include .txt file suffix
     * may include or omit  directory path to payload
     */
    public String getFunctionKeyFileName(String key)
    {
        if ( key == null ) return "";
        //assert key!=null, "BONG !  ---> PathFinder.getFunctionKeyFileName() received null value ????"
        String keyname = key.trim();
        int dots = keyname.count(".");
        int slash = keyname.count(fs);    
    
        String name = copyPathDirectory; 
        
        // if no folder or directory path included, add known copybook path
        if (slash < 1) 
        { 
            // missing .txt suffix, then add it else use as is
            if (dots < 1)
            {
                keyname = name + keyname.trim().toUpperCase() + ".txt"; 
            }
            else
            {
                keyname = name + keyname.trim().toUpperCase(); 
            }
        }
        // if folder or directory path included, keep it and do not add known copybook path
        else
        {
            // missing .txt suffix, then add one else use as is
            if (dots < 1)
            {
                keyname = keyname.trim() + ".txt"; 
            }
            else
            {
                keyname = keyname.trim(); 
            }        
        } // end of else

        say "... getFunctionKeyFileName(${key})=|"+keyname+"|";

        return keyname;
    } // end of method


    // confirms if a file present in this file system with full path & function key name and .txt
    public boolean hasFunctionKeyFileName(String key)
    {
        def has = false;
        def x = getFunctionKeyFileName(key);
        has = new File(x).exists();
        say "... hasFunctionKeyFileName(${key})="+has+" for file named:"+x;
        return has;
    } // end of method


    /*
     * Logic to reveal internal values
     */
    public void dump()
    {
		println "\nPathFinder.dump() --------------";
        println "... currentDirectory     ="+getCurrentDirectory();
        println "... user.home (homePath) ="+getHomePath();
        println "... copyPathDirectory    ="+getCopyPathDirectory();        
        println "... fs                   =|"+fs+"|";
        println "... currentDirectory     =|"+currentDirectory+"|";
        println "... homePath             =|"+homePath+"|";
        println "... copyPathDirectory    =|"+copyPathDirectory+"|";    	
        println "... useCurrent           =|"+useCurrent+"|";
    } // end of dump


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
        PathFinder pf;
        
        pf = new PathFinder();
        pf.dump();
        pf.audit = true;

        println "hasFunctionKeyFileName(F1)      = "+pf.hasFunctionKeyFileName("F1");
        println "getFunctionKeyFileName(F1)      = "+pf.getFunctionKeyFileName("F1");

        println "hasFunctionKeyFileName(F12.)    = "+pf.hasFunctionKeyFileName("F12.");
        println "getFunctionKeyFileName(F12.txt) = "+pf.getFunctionKeyFileName("F12.txt");
        
        println "hasFunctionKeyFileName(F12.txt) = "+pf.hasFunctionKeyFileName("F12.txt");
        
        println "getFunctionKeyFileName(F25)     = "+pf.getFunctionKeyFileName("F25");
        
        println "hasFunctionKeyFileName(F25)     = "+pf.hasFunctionKeyFileName("F25");

        println "getFunctionKeyFileName(/Users/jim/copybooks/F12.txt)     = "+pf.getFunctionKeyFileName("/Users/jim/copybooks/F12.txt");
        println "hasFunctionKeyFileName(/Users/jim/copybooks/F12.txt)     = "+pf.hasFunctionKeyFileName("/Users/jim/copybooks/F12.txt");
        def name =pf.getCopyPathDirectory();
       	name += "F1";

        println "pf.hasCopybook so name ="+name;
	    println "hasFunctionKeyFileName(${name}) = "+pf.hasFunctionKeyFileName(name);
	    println "getFunctionKeyFileName(${name}) = "+pf.getFunctionKeyFileName(name);

        name += "9";
	    println "hasFunctionKeyFileName(${name}) = "+pf.hasFunctionKeyFileName(name);
	    println "getFunctionKeyFileName(${name}) = "+pf.getFunctionKeyFileName(name);

        name += "X";
	    println "hasFunctionKeyFileName(${name}) = "+pf.hasFunctionKeyFileName(name);
	    println "getFunctionKeyFileName(${name}) = "+pf.getFunctionKeyFileName(name);

        pf.dump();

        println "\n---------------\n--- the end ---"
        //System.exit(0);
    } // end of main    
    
} // end of class