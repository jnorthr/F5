package io.jnorthr.toolkit;

/*
 * Feature to read/write/update text from a File
 */
public class IO
{
    /** an O/S specific char. as a file path divider */
    String fs = java.io.File.separator;

    /** an O/S specific location for the user's home folder name plus a trailing file separator*/ 
    String home = System.getProperty("user.home")+fs;

    /** a folder-relative location for the user's home folder name */ 
    String folder = "copybooks"+fs;


    /**
    * a folder-relative location for the user's filename storage location currently being revised/written
    * like /Users/jim/copybooks/F1.txt,etc.
    */ 
    String fn = home + folder;

    /** a structure of most recent answers from Chooser folder picking dialog */ 
    Response re = new Response(); 


    /** a string of most recent identified function key this instance of this class is using */ 
    private String functionkey = null;

    /** a string of most recent file contents from external file */ 
    private String payload = "";

    /** a string of most recent file contents between || */ 
    private String tooltip = "";

    /**
    * present is true if 'fn' file exists meaning there must be a payload and/or tooltip
    */
    private boolean present = false;

    /** If we need to println audit log to work, this will be true */ 
    private boolean audit = false;

    /** a string of most recent file name composed of home directory + F5 folder name + functionkey + .txt 
    * like /Users/jim/copybooks/F11.txt
    */ 
    private String filename = home + folder;


        
    /** valid functionkey choices for this program; F0 is for the ESCape key */
    def keys = ["F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12","F13","F14","F15","F16","F17","F18","F19","F20","F21","F22","F23","F24","F0"] 

    // --------------------------------------------------
    // Class Constructor Methods
    // --------------------------------------------------

    /**
     * Default constructor builds a tool to read text from a file for this function key
     */
    public IO()
    {
        Chooser ch = new Chooser("Pick input Folder");
        say "... Pick a folder-only "
        
        re = ch.getChoice();

	say "... Response="+re.toString();
        if (re.chosen && !re.abort)
        {
            say "... path="+re.path+"\nfile name="+re.artifact;    
            say "... the full name of the selected folder is "+re.fullname;    
            say "... isDir ? = "+re.isDir;    
        }
        else
        {
            say "... no choice was made so folder will be "+re.path+" and name="+re.fullname;
	}
	
        confirm();
        say "... IO() home folder of ${home} plus absolute folder of ${folder}  gives target folder of |${fn}|"
    } // end of default constructor

    /**
     * Non-Default constructor builds a tool to read text from a file and set audit flag on/off
     */
    public IO(boolean ok)
    {
        audit = ok;
        Chooser ch = new Chooser("Pick input Folder");
        say "... Pick a folder-only "
        
        re = ch.getChoice();

	say "... Response="+re.toString();
        if (re.chosen && !re.abort)
        {
            say "... path="+re.path+"\nfile name="+re.artifact;    
            say "... the full name of the selected folder is "+re.fullname;    
            say "... isDir ? = "+re.isDir;    
        }
        else
        {
            say "... no choice was made so folder will be "+re.path+" and name="+re.fullname;
	}
	
        confirm();
        //say "... IO() home folder of ${home} plus absolute folder of ${folder}  gives target folder of |${fn}|"
    } // end of non-default constructor


    // --------------------------------------------------
    // Getter/Setter Methods for Private variables
    // --------------------------------------------------
    
    /**
     * Method to retrieve a String of payload text from the text payload of a .txt file for this functionkey
     *
     * @return String tooltip content of file, if it exists or blank if not
     */
    protected String getPayload() 
    {
	say "--- IO.getPayload() of |${payload}|"
        return payload;
    } // end of method


    /**
     * Method to find String of tooltip text from the text payload of a .txt file
     *
     * @return String tooltip content of file, if it exists or blank if not
     */
    public String getToolTip() 
    {
	say "--- IO.getToolTip() =|${tooltip}|"
        return tooltip;
    } // end of method


    /**
     * Method to find which function key owns these member variables; typically taken from the text payload of a .txt file
     *
     * @return String tooltip content of file, if it exists or blank if not
     */
    public String getFunctionKey() 
    {
	say "--- IO.getFunctionKey() =|${functionkey}|"
        return functionkey;
    } // end of method

   
    /**
     * Method to load a String of payload text for this functionkey
     *
     * @param  is text that belongs to this functionkey's payload
     * @return String tooltip content of file, if it exists or blank if not
     */
    protected boolean setPayload(String txt) 
    {
    	this.payload = txt;
	say "--- IO.setPayload(${payload})"
	payload = findToolTip(); // is there any || delimited tooltip text at start of this text ?
        return payload;
    } // end of method


    /**
     * Method to load a String of tooltip text from a piece of the text payload of a .txt file within a pair of || char.s at the start of text
     *
     * @param  is text holding the tooltip for this functionkey
     * @return String tooltip content of file, if it exists or blank if not
     */
    public boolean setToolTip(String txt) 
    {
    	this.tooltip = txt;
	say "--- IO.setToolTip(${tooltip})"
        return tooltip;
    } // end of method


    /**
     * Method to load which function key owns these member variables; typically in the range of F1..F24
     *
     * @param  is text to show user via println
     * @return String tooltip content of file, if it exists or blank if not
     */
    public boolean setFunctionKey(String txt) 
    {
    	this.functionkey = txt.trim().toUpperCase();
	boolean yn = valid(functionkey);
	assert yn==true
	say "--- IO.setFunctionKey(${functionkey})"
        return functionkey;
    } // end of method


    // --------------------------------------------------
    // Utility Methods
    // --------------------------------------------------

    /**
     * Initialize this class with the function key
     */
    public boolean setup()
    {
	say "--- IO.setup() for key ${functionkey}"
	getFileName();

	//chkobj();
	present = read(); 

	say "... IO.setup() of |${functionkey}| gives filename of |${filename}|";
        say "... IO.setup() home folder of ${home} plus absolute folder of ${folder}  gives target folder of |${fn}|"
	say "... IO.setup(String |${functionkey}|) gives filename of |${filename}|; does it currently exist? "+present;
	return present;
    } // end of method


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
     * A method to see if class level flag says latest known filename points to a real actual file
     *
     * @return class-level boolean returned
     */
    public boolean exists()
    {
	say "--- exists()"
	return present;
    } // end of method


    /* logic to see if a text payload can be found for this function key */
    public String getFileName()
    {
	say "--- IO.getFileName()"
      	//functionkey = functionkey.trim();
      	//def i = functionkey.lastIndexOf('.');
      	//String key = (i<0) ? functionkey : functionkey.substring(0, functionkey.length() - (i+1)) ;
      	//key = key.trim();

      	// true when value is a choice in the range of F1..F24
      	//boolean yn = valid(key);
	//if (!yn)  { throw new Exception("... failed to IO.getFileName for |${functionkey}|");}	

	// make input text value into a filename for text files
      	//if ( i < 0 && functionkey.length() > 0 ) { functionkey += ".txt"; }

      	// compose filename as F1.txt = /Users/jim/copybooks/F1.txt
      	filename = re.fullname + fs + functionkey + ".txt";
      	say "\n... IO.getFileName() for key ${functionkey} has filename of |${filename}|"
      	return filename;
    } // end of method


    /**
     * A method to see if provided value is one of allowable choice in F0..F24 range, where F0 is the escape key
     *
     * @return method boolean returned
     */
    public boolean valid(String key)
    {
	say "--- IO.valid(${key})"
	key = key.trim();

        if ( key in keys ) { return true; }
        
        say "... valid(String |${key}|) will return not valid"
	throw new RuntimeException("... IO.valid method disallowed use of invalid function key of |${key}|");
	
	return false;
    } // end of method


    /**
     * Method to find String of tooltip text from the text payload of a .txt file
     *
     * @param  String text payload for this function key that may have tooltip text prefix within || delimiters
     * @return String content of payload from file after tooltip is removed, if it exists, else return original payload
     */
    private String findToolTip() 
    {
	say "--- IO.findToolTip() in payload ${payload})"
        if (payload==null || payload.size()<1)
        {
	    tooltip = "";
            return "";
        } // end of if

        String tt = payload.trim();
        boolean ok = (tt.startsWith("|"));
        def ix = tt.indexOf('|') 
        say "... IO.findToolTip() in payload found | at ix=<${ix}>; is tooltip text present ? "+ok;

        if (ok && ix > -1)
        {
                tt = tt.substring(1);
                ix = tt.indexOf('|') 
                tt = payload.trim().substring(1, ix+1);
		payload = payload.substring(ix+2)
                say "... IO.findToolTip() tt=<${tt}>";
        } // end of if
        else
        {
            tt = "";
            say "... IO.findToolTip() has | at ix=${ix} of first | & no tooltip."
        }

	tooltip = tt;
        return payload.trim();
    } // end of method



    // --------------------------------------------------
    // IO File Utility Methods
    // --------------------------------------------------

    /**
     * Method to read String of text from a file. The @param gives the simple filename. 
     * This code adds the home and folder name when we look for function key payloads for our F5 app.
     * Also if missing a trailing suffix like .txt then .txt is added
     *
     * boolean present is set/reset here if file exists means there must be a payload and/or tooltip
     *
     * @return  boolean that's true if file was read successfully or else false when it failed
     */
    private boolean read() 
    {
	say "--- IO.read()"
        payload = "";
	tooltip = "";
	filename = getFileName();
	boolean good = false;
		
        try{
          payload =  new File(filename).getText('UTF-8');
          say "... IO.read() found <${filename}> file with ${payload.size()} byte payload";
	  good = true; 
	  // strips tooltip from payload if any & populates tooltip variable
	  payload = findToolTip(); 
	} // end of if
	catch(Exception e)
	{
          say "... IO.read() found exception reading <${filename}> :" + e.toString();
	} // end of else

        return good;
    } // end of method



    /**
     * Method to write String of text to a simple named file; this method will prefix our own home + folder names
     * filename has simple name of text file to write like F12.txt
     *
     * @param  String of text to write to file
     * @return true, if write was successful
     */
    public boolean write(String payload) 
    {
	say "--- IO.write(${payload})"
    	def file3 = new File(filename)
        present = file3.exists();

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
            say "... IO.write method failed to write <${filename}> due to:"+x.message;
            present = false;
        }

        return present;
    } // end of method


    /**
     * Method to write String of text to a simple named file; this method will prefix our own home + folder names
     *
     * @param  String simple name of text file to write like F12.txt
     * @param  String of text to write to file
     * @return true, if write was successful
     */
    public boolean write(String key, String payload) 
    {
	say "--- IO.write(${payload})"
    	def file4 = new File(key)
        present = file4.exists();
        try
        {
            if (payload==null) { payload=""; }
	    file4.withWriter('UTF-8') { writer ->
            	writer.write(payload)
	    } // end of withWriter
            present = true;
        } // end of try
        catch(IOException x)
        {
            say "...  IO.write method failed to write <${key}> due to:"+x.message;
            present = false;
        }

        return present;
    } // end of method


    /**
     * Method to delete and remove a simple named file; this method will prefix our own home + folder names
     *
     * @param  String simple name of text file to write like F12.txt
     * @return true, if delete was successful
     */
    public boolean delete() 
    {
	say "--- IO.delete()"
        def file3 = new File(filename)
        present = file3.delete();
        say "... deleted file <${filename}> ? "+present;
        return present;
    } // end of method


    /**
     * Method to see if a file exists for provided filename
     * This code does changes filename to be confirmed; but the home plus copybooks/ folder names are added by our method.
     *
     * @param  String simple name of text file to read: F1.txt= /Users/jim/copybooks/F1.txt
     * @return class-level boolean becomes true if it exists or false if not
     */
    public boolean chkobj() 
    {
      	present = false;

      	// if input value was a good valid choice
      	present = new File(filename).exists();
      	
      	say "... IO.chkobj() exists ? "+present+"; looking for file |${filename}|";

      	return present;
    } // end of method


    /**
     * Method to confirm copybooks folder exists, or build it if it does not
     *
     * @return true, if folder build was successful
     */
    private boolean confirm() 
    {
	say "--- IO.confirm() will use |${re.fullname}|"
        def fi = new File(re.fullname);
        boolean yn = fi.exists() ? true : false;
        String fo = fi.getAbsolutePath(); //re.fullname;
        if (!yn)
        {
            say "... confirm() folder fo=<${fo}> to build '${re.fullname}';"
            FileTreeBuilder treeBuilder = new FileTreeBuilder(new File(fo))
            treeBuilder.dir(fo);
            say "... confirm() ${re.fullname} built folder"
            yn = true;
        } // end of if
	else
	{
	    //fo = home + folder;
	    re.fullname = fo;
            say "... confirm() folder fo=<${fo}> already exists to build '${re.fullname}';"
	} // end of else

	say "... end of confirm()\n\n"
        return yn;
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
        println "Hello from IO.groovy"
        println "... ------------------------"
        IO ck = new IO(true);
        ck.setup();  //"F12");
        
	ck.say "... F12.filename ? "+ck.filename;
	ck.say "... F12.present ? "+ck.present;
	ck.say "... F12.functionkey ? "+ck.functionkey;
	ck.say "... F12.payload ? "+ck.payload;
	ck.say "... F12.tooltip ? "+ck.tooltip;
System.exit(0);



	println ""	
	ck.say "... confirm root copybooks folder exists:"+ck.confirm();
        boolean flag = ck.chkobj();
	println "... flag=${flag} while file "+ck.filename+" exists ? "+ck.exists();
	println " "
	ck.say "... ck.payload ? "+ck.payload;
	ck.say "... ck.tooltip ? "+ck.tooltip;

	String g = "|Here's a tip|Hi kids";
	println "\n... g="+g;
	g = ck.findToolTip();
	ck.say "\n... ck.findToolTip() returned this:'"+g+"'";
	ck.say "... ck.tooltip ? '"+ck.tooltip+"'";

	g = ck.findToolTip();
	println "\n... g="+g;
	ck.say "\n... ck.findToolTip() with no tooltip returned this:'"+g+"'";
	ck.say "... ck.tooltip ? '"+ck.tooltip+"'";


	g = ck.findToolTip();
	println "\n... g="+g;
	ck.say "\n... ck.findToolTip() for empty string returned this:'"+g+"'";
	ck.say "... ck.tooltip ? '"+ck.tooltip+"'";

	g = ck.findToolTip();
	println "\n... g="+g;
	ck.say "\n... ck.findToolTip() for null string returned this:'"+g+"'";
	ck.say "... ck.tooltip ? '"+ck.tooltip+"'";

        println "... ------------------------"
        ck = new IO("F23",true);
	ck.say "... F23.filename ? "+ck.filename;
	ck.say "... F23.present ? "+ck.present;
	ck.say "... F23.payload ? "+ck.payload;
	ck.say "... F23.tooltip ? "+ck.tooltip;
	println ""	

        println "... ------------------------"

        println "\n---------------- F15  -----------------------------"
        String fn = "F15";
	try{
		ck = new IO(fn,true);
        	boolean ok = ck.delete();
        	println "... did we delete ${fn} file named |${ck.filename}| ? :"+ok;

		ok = ck.write("|F15 tooltip goes here|Payload for F15 is here.");
        	println "... did we write tooltip to <${ck.filename}> file ok ? :"+ok;
	} 
	catch (Exception xx)
	{ println "... F15.txt failed:"+xx;}

        // use chkobj to confirm F15.txt is there 
        boolean yn = ck.chkobj();
	println "... file "+ck.filename+" exists ? "+ck.exists();
	String tx = "";

        if (yn)
        {
	    ck.filename = ck.getFileName();
	    println "... yn=${yn} and file "+ck.filename+" exists ? "+ck.exists();
	    tx = ck.read();
    	    println "... <${fn}> read found:"+tx.length()+" bytes in file "+ck.filename;
    	} // end of if

        println ""
        println "\n---------------- F17  -----------------------------"
        fn = "F17.txt";
	try{
		ck = new IO(fn,true);
	        yn = ck.write("ok now we write ${ck.filename} file");
	} 
	catch (Exception xx){ println "... F17.txt failed:"+xx;}
        println "... did we write <${ck.filename}> ok ? :"+yn; // yes, writes to previously good invoke of IO module with F15 choice, so this msg goes into F15.txt


        // try without file .txt suffix
        println ""
        println "\n---------------- F13  -----------------------------"
        fn = "F13";
	ck = new IO(fn,true);
        tx = ck.getPayload();
        if (tx.size() > 0) { println "... found <${fn}> file ? :\n    payload has "+tx.size()+" bytes"; }
        if (tx.size() < 1) { println "    - no payload found for |${fn}|"}

        println ""
        println "\n---------------- F14  -----------------------------"
        fn = "F14";
	ck = new IO(fn,true);
        tx = ck.getPayload();
        println "... found <${fn}> file ? "
        println "... payload has "+tx.size()+" bytes";
        if (tx.size() < 1) { println "... no payload found for <${fn}>"}

        println ""
        tx = ck.getToolTip();
        println "... found <${fn}> tooltip file ? "
        println "... payload has "+tx.size()+" bytes";
        if (tx.size() < 1) { println "    - no tooltips found for <${fn}>"}

        println ""
        println "\n---------------- F18  -----------------------------"
        fn = "F18";
	ck = new IO(fn,true);
        boolean ok = ck.write("|F18 tooltip goes here|The payload for F18 is this.");
        println "... did we write tooltip to <${fn}> file ok ? :"+ok;

        println ""
        println "\n---------------- F19  -----------------------------"
        fn = "F19";
	ck = new IO(fn,true);
        ok = ck.delete();
        if (ok){ println "... deleted F19.txt"; }
        if (!ok){ println "... did not delete F19 file "+ck.filename; }
        ok = ck.write("");
        println "... did we write tooltip to <${ck.filename}> file ok ? :"+ok;
        tx = ck.getToolTip();
        if (tx.size() < 1) { println "    - no tooltips found for <${fn}>"}
        tx = ck.getPayload();
        if (tx.size() < 1) { println "    - no payload found for <${fn}>"}


        println "\n---------------- F20  -----------------------------"
	ck = new IO("F20",true);
        ck.getFileName();
        boolean ok2 = ck.delete();
        if (ok2){ println "... deleted F20"; }
        
        ok2 = ck.write(null);
        println "... did we write tooltip to F20 file ok ? :"+ok2;

        tx = ck.getToolTip();
        if (tx.size() < 1) { println "    - no tooltips found for F20"}
        if (tx.size() > 0) { println "    - tooltip found for F20 has "+tx.size()+" bytes"}
        
        tx = ck.getPayload();
        if (tx.size() < 1) { println "    - no payload found for F20"}
        if (tx.size() > 0) { println "    - payload found for F20 has "+tx.size()+" bytes"}

        println "-------------------------------------------------"
	ok2 = ck.valid("F2");
	println "... is this F2 value a valid text value for this module ? :"+ok2;

        println "-------------------------------------------------"
	try{ ok2 = ck.valid("F2.adoc"); } catch(Exception x) { println "... F2.adoc failed";};
	println "... is this F2.adoc value a valid text value for this module ? :"+ok2;

        println "-------------------------------------------------"
	try{ ok2 = ck.valid("F26"); } catch(Exception x) { println "... F26 failed";};
	println "... is this F26 value a valid text value for this module ? :"+ok2;

        println "\n---------------- typical logic flow  -----------------------------"
	IO io = new IO("F12",true);
	println "... is this unset value a valid text value for this module ? :"+ok2;
        println "... is any function set at start of this class invoke ? "+io.exists();
	String ss = "";
        println "... is any 'blank' filename known for this function value set at start of class ? "+io.chkobj();




        println "\n--- the end---"
    } // end of main 

} // end of class
