package io.jnorthr.toolkit;

// Start of class declaration
public class Sample3
{
    def copybookFile = "/Users/jim/copybooks"
    def fs = java.io.File.separator;

    /* return full path & filename for a target payload file for one function key
     * may or maynot include .txt file suffix
     * may include or omit  directory path to payload
     */
    public String getFunctionKeyFileName(String key)
    {
        String keyname = key.trim();
        int dots = keyname.indexOf(".");
        int slash = keyname.indexOf(fs);    
    
        String name = copybookFile + fs;
        
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
                keyname = name + keyname.trim().toUpperCase() + ".txt"; 
            }
            else
            {
                keyname = name + keyname.trim().toUpperCase(); 
            }        
        } // end of else

        return keyname;
    } // end of method

    // confirms if a file present in this file system with full path & function key name and .txt
    public boolean hasFunctionKeyFileName(String key)
    {
        def has = false;
        def x = getFunctionKeyFileName(key);
        has = new File(x).exists();
        return has;
    } // end of method


    public static void main(String[] args)
    { 
        println "---> Starting code"
        Sample3 s = new Sample3();

        println "... getFunctionKeyFileName(String /fred/F1) = |"+s.getFunctionKeyFileName("/fred/F1")+"|";
        println "... hasFunctionKeyFileName(String /fred/F1) = |"+s.hasFunctionKeyFileName("/fred/F1")+"|";

        println "... getFunctionKeyFileName(String F12) = |"+s.getFunctionKeyFileName("F12")+"|";
        println "... hasFunctionKeyFileName(String F12) = |"+s.hasFunctionKeyFileName("F12")+"|";

        println "... getFunctionKeyFileName(String F1.txt) = |"+s.getFunctionKeyFileName("F1.txt")+"|";
        println "... hasFunctionKeyFileName(String F1.txt) = |"+s.hasFunctionKeyFileName("F1.txt")+"|";

        println "---> Ending code"
    } // end of main
} // end of class