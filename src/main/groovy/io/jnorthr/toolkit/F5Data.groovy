package io.jnorthr.toolkit;

public class F5Data
{
	/** a key/value map storage for know Function Keys; key is like F12,F2,F14 while value of it's tooltip text  */
    	Map tooltips = [:]

	/**
	* a key/value map storage for known Function Keys with a boilerplate 'payloads' below that can be copied to 
	* system clipboard; key is like F12 or F2 while value is true if payload text exists  
    	*/
	Map hasPayload = [:]

	/**
	* a key/value map storage for known Function Keys with a boilerplate payload that can be copied to 
	* system clipboard; key is like F12 or F2 while value is it's text payload if same hasPayload flag is true  
	*/
	Map payloads = [:]

	/**
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
