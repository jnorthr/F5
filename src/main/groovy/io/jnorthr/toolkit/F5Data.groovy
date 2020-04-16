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

    // =============================================================================    
    /**
      * The primary method to execute this class. Can be used to test and examine logic and performance issues. 
      * 
      * argument is a list of strings provided as command-line parameters. 
      * 
      * @param  args a list of possibly zero entries from the command line; first arg[0] if present, is
      *         taken as a simple file name of a groovy script-structured configuration file;
      */
	public static void main(String[] args) {
		F5Data f5d = new F5Data();
		println "-------------------------"
		println "buttons:"+f5d.buttons.toString();
		println "--- the end of F5Data ---"
	} // end of main

} // end of class
