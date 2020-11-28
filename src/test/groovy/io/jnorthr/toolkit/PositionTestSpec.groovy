package io.jnorthr.toolkit;
import io.jnorthr.toolkit.Mapper;

/*
* see: http://code.google.com/p/spock/wiki/SpockBasics
* A spock test wrapper around the base class
*/
import java.util.logging.Logger;
import spock.lang.*
import java.lang.*;
import javax.swing.*;

/**
  * Tests a tool to ask user for missing text in replacement payloads like ${name} 
  */
class PositionTestSpec extends Specification 
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
  def "1st Test: Setup PositionLogic object without fail"() {
    given:
  	  println "1st Test: Setup PositionLogic object without fail"
      JFrame fr = new JFrame("PositionLogic Test");
      PositionLogic  ma = new PositionLogic (fr);
      ma.setup();
    
    when:
      fr.setVisible(true);

    then:
		  // Asserts are implicit and not need to be stated.
    	true == ma!=null;
  } // end of test


  // Second Test
  def "2nd Test: Setup PositionLogic object with fail"() {
    given:
  	println "2nd Test: Setup PositionLogic object with fail"
 
    when:
      PositionLogic  ma = new PositionLogic ();

    then:    
      // Asserts are implicit and not need to be stated.
	    null == ma;
  } // end of test
  
} // end of spec
