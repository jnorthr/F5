package io.jnorthr.toolkit;
import io.jnorthr.toolkit.TemplateMaker;

/*
* see: http://code.google.com/p/spock/wiki/SpockBasics
* A spock test wrapper around the base class
*/
import java.util.logging.Logger;
import spock.lang.*
import java.lang.*;

/**
  * Tests a tool to create and save text for the System Clipboard for most operatng systems
  */
class TemplateMakerTestSpec extends Specification 
{
  // fields
  
  // Fixture Methods
  
  // run before every feature method
  def setup() 
  { 
  }          

  // run after every feature method
  def cleanup() {}
  
  
  // Note: The setupSpec() and cleanupSpec() methods may not reference instance fields.
  def setupSpec() 
  {
  } // run before the first feature method
    
  def cleanupSpec() {}   // run after the last feature method}

/*
Feature methods are the heart of a specification. They describe the features (properties, aspects) that you expect to find in the system under specification. By convention, feature methods are named with String literals. Try to choose good names for your feature methods, and feel free to use any characters you like!

Conceptually, a feature method consists of four phases:

 . Set up the feature's fixture
 . Provide a stimulus to the system under specification
 . Describe the response expected from the system
 . Clean up the feature's fixture
 . Whereas the first and last phases are optional, the stimulus and response phases are always present (except in interacting feature methods), and may occur more than once.
*/

  // Feature Methods

  // First Test
  def "1st Test: Setup TemplateMaker for copy to system clipboard"() {
    given:
  		println "1st Test: Use TemplateMaker to save text for system clipboard"
    
    when:
      	TemplateMaker tm = new TemplateMaker();

    then:
		  // Asserts are implicit and not need to be stated.
    	true == tm!=null;
  } // end of test


  // Second Test
  def "2nd Test: Confirm TemplateMaker to capture text for system clipboard "() {
    given:
  		println "2nd Test: Use TemplateMaker to paste text on system clipboard"
      	TemplateMaker tm = new TemplateMaker();
 
    when:
      	boolean ok = tm.paste("pasted by TemplateMakerTestSpec");
      	println "... 2nd Test ok="+ok;

    then:    
  		// Asserts are implicit and not need to be stated.
		ok == true;
  } // end of test
  
} // end of spec
