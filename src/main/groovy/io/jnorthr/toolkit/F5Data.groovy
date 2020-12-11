package io.jnorthr.toolkit;

/**
 * This class retains the instance values for the currently running F5 class
 *
 * @author james.b.northrop@googlemail.com
 *
 */
public class F5Data
{
	/** A Variable to hold the text, if any, to be used to provide a tooltip for each function key when/if a mouse hovers over that key on the GUI
	* 
	* a key/value map storage for know Function Keys; key is like F12,F2,F14 and value,if any, of it's tooltip text  
	*/
    Map tooltips = [:]

	/**
	* a True/False variable storage array for each key. If any text is in the payload for this key, the value is true else false means no text to copy to clipboard
	*
	* a key/value map storage for known Function Keys with a boilerplate 'payloads' below that can be copied to 
	* system clipboard; key is like F12 or F2 while value is true if payload text exists  
    	*/
	Map hasPayload = [:]

	/**
	* The actual content (if any) to be copied to the system clipboard for a given function key pressed in the GUI
	*
	* a key/value map storage for known Function Keys with a boilerplate payload that can be copied to 
	* system clipboard; key is like F12 or F2 while value is it's text payload if same hasPayload flag key is true  
	*/
	Map payloads = [:]

	/**
	* This variable is a map of all the metadata for each function key used in F5
	*
	* a key/value map storage for button Function Keys with a boilerplate payload that can be copied to 
	*/
	Map buttons = [:]


	/**
	* This variable holds current working directory name and home path name of this user to be used in F5
	*/
    PathFinder pf;
    
	/** If we need to println audit log to work, this will be true */ 
    boolean audit = true;    
        
    // constructor    
	public F5Data()
	{
        pf = new PathFinder();
 
        println "... F5Data from PathFinder user.home (homePath) ="+pf.getHomePath();
        println "... F5Data from PathFinder user.dir (pwd)="+pf.getCurrentDirectory();
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
     * A method to show internal session variables if audit flag is true
     *
     * @return void
     */
    public void dump()
    {
    	say "\nF5Data internals:"
		def tx = "";

		hasPayload.eachWithIndex { val, index ->
    		tx = (val) ? "yes" : "no" ;
    		say "entry ${index} is ${val.key} = ${tx} hasPayload="+hasPayload[val.key]
    		say "                tooltip:"+tooltips[val.key];
    		say "                payloads:"+payloads[val.key];
		} // end of each

    	say "\nF5Data end"
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
		println "-------------------------"
		F5Data f5d = new F5Data();
		f5d.hasPayload["F1"] = true;
		f5d.tooltips["F1"] = "Tooltip for F1";
		f5d.payloads["F1"] = "Hello World";
		f5d.audit = true;
		f5d.dump();
		println "--- the end of F5Data ---"
	} // end of main

} // end of class
