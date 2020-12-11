package io.jnorthr.toolkit;

import java.io.*
import java.io.File;
import java.nio.file.*;
import java.io.IOException;
import java.io.File;
import java.nio.file.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChooseCopybooksFolder
{ 
    String fs = java.io.File.separator;
    boolean homeCopybooks = false;
    boolean workCopybooks = false;
    boolean audit = false;

    String xxx = "";
    File f = new File(xxx);

    String currentUsersWorkDir = System.getProperty("user.dir");
    String currentUsersHomeDir = System.getProperty("user.home");
    public boolean getHomeCopybooks() { return homeCopybooks; }
    public boolean getWorkCopybooks() { return workCopybooks; }
    
    public ChooseCopybooksFolder()
    {
        say("\n======================\n ChooseCopybooksFolder() ---");
        say("... user.home= |"+currentUsersHomeDir+"|"); 
        xxx = currentUsersHomeDir;
        f = new File(xxx);

        if(f.exists()) 
        { 
            say("... |"+currentUsersHomeDir+"| exists"); 
            if (f.isDirectory())  
            { 
                say("... |"+currentUsersHomeDir+"| is a folder"); 
                currentUsersHomeDir+=fs;
                currentUsersHomeDir+="copybooks"; // try to find copybooks folder
        
                f = new File(currentUsersHomeDir);
                if(f.exists())
                {
					currentUsersHomeDir+=fs;
                    homeCopybooks = true;
                    say("... have found a copybook folder :"+currentUsersHomeDir);
                } // end of if
                else
                {
                    say("... have not found any copybook folder named :"+currentUsersHomeDir);
                } // end of else            
            } // end of if
        
        } // end of if
        else
        {
            say("... |"+currentUsersHomeDir+"| does not exist"); 
        } // end of else

        say("\n=========================================\n");

        // -----------------------------------------------
        // try current working folder found via user.dir
        currentUsersWorkDir = System.getProperty("user.dir");
        say("... user.dir= |"+currentUsersWorkDir+"|");

        xxx = currentUsersWorkDir;
        f = new File(xxx);

        if(f.exists()) 
        { 
            say("... |"+currentUsersWorkDir+"| exists"); 
            if (f.isDirectory())  { say("... user.dir |"+currentUsersWorkDir+"| is a folder"); }
            if (!f.isDirectory()) { say("... user.dir |"+currentUsersWorkDir+"| is a file"); }

            currentUsersWorkDir+=fs;
            currentUsersWorkDir+="copybooks"; // try to find copybooks folder
        
            f = new File(currentUsersWorkDir);
            if(f.exists())
            {
				currentUsersWorkDir+=fs;
                workCopybooks = true;
                say("... have found a copybook folder in currentUsersWorkDir :"+currentUsersWorkDir);
            } // end of if
            else
            {
                say("... have not found any copybook folder named :"+currentUsersWorkDir);
            } // end of else
        } // end of if
        else
        {
            say("... user.dir |"+currentUsersWorkDir+"| does not exist"); 
        } // end of else
    } // end of constructor


    public String getFolderName()
    {
        say("\n======================\ngetFolderName() ");
        say("... ${currentUsersHomeDir} found homeCopybooks="+homeCopybooks);
        say("... ${currentUsersWorkDir} found workCopybooks="+workCopybooks);
		/*
		* If not copybooks folder 
		*/
        if (homeCopybooks==false && workCopybooks==false)
        {
            // copybooks folder not found, so make one in the user's home folder
            Path p = Paths.get(currentUsersHomeDir);
            Files.createDirectories(p);
            say("... created a copybooks folder in "+currentUsersHomeDir);
        } // end of if to create a user-based copybooks folder if none exists in neither user's folder or current work folder
                
        // if there is a copybooks folder in the current working directory use that, else point to (possibly just-created) user's home version
        return (workCopybooks) ? currentUsersWorkDir : currentUsersHomeDir;
    } // end of get
    
    public void say(def txt) { if (audit) {println txt;} }
    public static void main(String[] args)
    {
        ChooseCopybooksFolder ccf = new ChooseCopybooksFolder();
        println "... folder to use:"+ccf.getFolderName();
        println "... 'copybooks' folder found in current working folder ? "+ccf.getWorkCopybooks();
        println "... user's home folder has 'copybooks' folder ? "+ccf.getHomeCopybooks();
		println "\n... currentUsersHomeDir=|"+ccf.currentUsersHomeDir+"|";
		println "... currentUsersWorkDir=|"+ccf.currentUsersWorkDir+"|";
    } // end of main
} // end of class