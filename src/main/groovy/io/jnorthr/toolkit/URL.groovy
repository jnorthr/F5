package io.jnorthr.toolkit;

/* - if your build tool,i.e.Gradle does not have this dependency, then uncomment this Grapes
@Grapes([
    @Grab(group='org.slf4j', module='slf4j-api', version='1.6.1'),
    @Grab(group='ch.qos.logback', module='logback-classic', version='0.9.28')
])
*/

import org.slf4j.*
import groovy.util.logging.Slf4j
import groovy.transform.*;

/*
 * Feature to get text from remote URL.
 *
 * The initial text in the target file can have a tooltip  for a given key. It must be the first
 * text and surrounded by | characters. So text like :
 *
 * |This is tooltip to show when user hovers mouse over GUI key|Here is text copied to your clipboard 
 * when this function key is pressed
 *
 */
//@Slf4j
@Canonical
public class URL
{
    /** an O/S specific char. as a file path divider */
    String fs = java.io.File.separator;

    /** an O/S specific location for the user's home folder name plus a trailing file separator*/ 
    String home = System.getProperty("user.home")+fs;

    /** an O/S specific location for the user's home folder name plus a trailing file separator and 
    name of file holding payload text; */ 
    String fn = home+"copybooks"+fs+"F15.txt";

    /** If DollarSign {} keys found within the template String, this will be true */ 
    boolean hasMap = false;

    /** If we need to println audit log to work, this will be true */ 
    boolean audit = true;

    /**
     * Default constructor builds a tool 
     */
    public URL()
    {
        //log.debug "... constructor for URL.debug"
        //log.info  "... constructor for URL.info"
    } // end of default constructor
    

    /**
     * A method to print an audit log if audit flag is true
     *
     * @param text is text to show user via println
     * @return void
     */
    public void say(String text)
    {
        if (audit) { println text; }
    } // end of method


    /**
     * A method to get remote text
     *
     * @param fn is name of F5 text file with payload to show user
     * @return String of text from remote text file if it exists else null
     */
    public String getRemote(String fn)
    {
    	// see if given filename has path separator; if not assume 'fn' is simple
    	// name that needs home folder and path name added to it;
		  def ix = fn.indexOf(fs);
		  if (ix<0)
		  {
			   fn= home+"copybooks"+fs+fn;
			   say "... added home and folder name to filename given"
		  } // end of if

    	File fi = new File(fn)
    	if (!fi.exists())
    	{
    	   say "... could not find payload named "+fn;
    	   return null;
    	} // end of if

	    def line  
      String tx=fi.text;
    	
    	new File(fn).withReader { line = it.readLine() }  

    	say line  
    	line = line.trim()
    	say line  
    	
      if (line.startsWith("|") && line.endsWith("|"))
    	{
	        def xx = line.substring(1,line.length()-1 )
    	    xx=xx.trim()
        	say "... yes=>["+xx+"]"
        	try{
            	tx = xx.toURL().text  
            	say "... url ${xx} has "+tx.length()+" bytes"
        	} // end of try
        	catch (Exception e) 
        	{
            	tx = "... failed to get text from ${xx} :"+e.getMessage();
            	say "... url ${xx} failed:\n"+tx;
        	}    
	    } // end of if

	    return tx;
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
        println "Hello from URL.groovy"
        URL obj = new URL();
        println "... getting text from file:"+obj.fn;

        String ans = obj.getRemote(obj.fn);

        if (ans==null)
        {
          println "... did not find file "+obj.fn;
        }
        else
        {
          println "... ans.length()="+ans.length();
        }

        //assert ans.size() == 63;
        obj.say("Hello from URL class");

        println "--- the end---"
    } // end of main 

} // end of class
