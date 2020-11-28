package io.jnorthr.toolkit;

/*
 * Tool to read/write/update/delete/confirm thete is text from a File for function keys
 *
 * F1.txt has this content: The tooltip is located within a pair of ||'s
 * |Asciidoctor Template|Text payload for
 * the F1 function key ${abbrev}.${name}.
 *
 * so after loading, the 'payload' variable holds : 
 * Text payload for
 * the F1 function key ${abbrev}.${name}.
 * 
 * and 'tooltip' has :
 * Asciidoctor Template
 *
 */
public class IO
{
    /** If we need to print audit log to work, this will be true */ 
    private boolean audit = false;

    /** a class of most recent system and app metadata values for this F5 app */ 
    PathFinder pf = new PathFinder();

    /** a string for most recently loaded function key this instance of this class is using,
     * uppercase and trimmed of leading & trailing blanks, line feeds, etc. say like F4 
     */ 
    private String functionkey = "";

    /** a string of most recent file non-tooltip contents from external file for this functionkey */ 
    private String payload = "";

    /** a string of most recent file contents between ||  for this functionkey */ 
    private String tooltip = "";


    /** an O/S specific location for the user's home folder name plus a trailing file separator
     * plus the 'copybook' group folder name - something like /Users/jim/copybooks which holds a
     * bunch of payload files, one per function key and named 'function key name' + .txt 
     *
     * For example function key twelve payload is expected to be '/Users/jim/copybooks/F12.txt'
     */ 
    String  copybookFilename = "";

    /** a flag that tells us if there exists a physical file as named in copybookFilename */ 
    boolean copybookFound = false;


    /** valid functionkey choices for this program; 
     * both ESC & F0 are for the ESCape key while 'A' choice is to move panel side-to-bottom-to-side of display
     */
    def keys = ["A","F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12","F13","F14","F15","F16","F17","F18","F19","F20","F21","F22","F23","F24","F0","ESC"] 


    // --------------------------------------------------
    // Class Constructor Methods
    // --------------------------------------------------

    /**
     * Default constructor builds a tool to read text from a file for this function key
     */
    public IO()
    {
    } // end of default constructor

    /**
     * Non-Default constructor builds a tool to read text from a file and set audit flag on/off
     */
    public IO(boolean ok)
    {
        audit = ok;
    } // end of non-default constructor


    // --------------------------------------------------
    // Getter/Setter Methods for Private variables
    // --------------------------------------------------
    
    /**
     * A method to make all functionkey variables back to original - empty - values
     */
    public void reset()
    {
        functionkey = "";
        payload = "";
        tooltip = "";
        copybookFilename = "";
        copybookFound = false;
        say "... void reset() = ";
        show();
    } // end of method

    /**
     * A method to make all functionkey variables back to original - empty - values, 
     * then, if valid, parameter string is put in functionkey variable
     */
    public boolean reset(String key)
    {
        key = (key==null) ? "" : key;
        boolean yn = false;

        try {
            key = key.trim();
            key = ( key.size() < 4 ) ? key.toUpperCase() : key;
            yn = valid(key);

            if (!yn) 
            { 
                say "... reset exception: value |${key}| not usable here; must be within F1..F24 range" 
                return false;
            }
			else
			{
				functionkey = key;
			} // end of else
        } 
        catch(Exception ex) 
		{
            println("... A reset(${key}) exception failed.");
            return false;
        } // end of catch

        functionkey = key;
        payload = "";
        tooltip = "";
        copybookFound = pf.hasFunctionKeyFileName(functionkey);
        copybookFilename = pf.getFunctionKeyFileName(functionkey);
        say "... reset(${key}) = "+yn;
        
        return copybookFound;
    } // end of method


    /**
     * Method to retrieve a String of payload text from the text payload of a .txt file for this functionkey
     *
     * @return String tooltip content of file, if it exists or blank if not
     */
    protected String getPayload(String key) 
    {
        key = (key==null) ? "" : key;
        reset(key);
        boolean yn = read(key);
        return payload;
    } // end of method


    /**
     * Method to retrieve a String of payload text from the text payload of a .txt file for this functionkey
     *
     * @return String tooltip content of file, if it exists or blank if not
     */
    protected String getPayload() 
    {
        return payload;
    } // end of method


    /**
     * Method to find String of tooltip text from the text payload of a .txt file
     *
     * @return String tooltip content of file, if it exists or blank if not
     */
    public String getToolTip(String key) 
    {
        key = (key==null) ? "" : key;
        reset(key);
        boolean yn = read(key);
        functionkey = key;
        return tooltip;
    } // end of method


    /**
     * Method to return String of tooltip text from the text payload of a .txt file
     *
     * @return String tooltip content of file, if it exists or blank if not
     */
    public String getToolTip() 
    {
        return tooltip;
    } // end of method


    /**
     * Method to find which function key owns these member variables; typically taken from the text payload of a .txt file
     *
     * @return String tooltip content of file, if it exists or blank if not
     */
    public String getFunctionKey() 
    {
        return functionkey;
    } // end of method

   
    /**
     * Method to load a String of payload text for this functionkey
     *
     * @param  txt has text that belongs to this 'functionkey' payload
     * @return String tooltip content of file, if it exists or blank if not
     */
    protected boolean setPayload(String txt) 
    {
    	//this.payload = txt;
        //this.tooltip = 
        converter(txt); // is there any || delimited tooltip text at start of this text ?
        return true;
    } // end of method


    /**
     * Method to load a String of tooltip text from a piece of the text payload of a .txt file within a pair of || char.s at the start of text
     *
     * @param  is text holding the tooltip for this 'functionkey'
     * @return String tooltip content of file, if it exists or blank if not
     */
    public boolean setToolTip(String txt) 
    {
    	this.tooltip = txt;
        return tooltip;
    } // end of method


    /**
     * Method to load which function key owns these member variables; typically in the range of F1..F24
     *
     * @param  is text to show user via println
     * @return String tooltip content of file, if it exists or blank if not
     */
    public boolean setFunctionKey(String key) 
    {
        key = (key==null) ? "" : key;
        reset(key);
        return true;
    } // end of method


    // --------------------------------------------------
    // Utility Methods
    // --------------------------------------------------

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
     * @return yes/no boolean returned
     */
    public boolean exists()
    {
        boolean present = pf.hasFunctionKeyFileName(functionkey)
        return present;
    } // end of method


    public boolean exists(String key)
    {
        reset(key);
        boolean present = pf.hasFunctionKeyFileName(key)
        return present;
    } // end of method


    /* logic to see if a text payload can be found for this function key */
    public String getFileName(String functionkey)
    {
        functionkey = (functionkey==null) ? "" : functionkey;

      	// compose filename as F1 = /Users/jim/copybooks/F1.txt
        //boolean good = pf.hasFunctionKeyFileName(functionkey);
        String filename = pf.getFunctionKeyFileName(functionkey);
      	return filename;
    } // end of method


    /**
     * A method to see if provided value is one of allowable choice in F0..F24 range, where F0 is the escape key
     *
     * @return method boolean returned
     */
    public boolean valid(String key)
    {
        say "... valid(String |${key}|) called."
        key = (key==null) ? "" : key;

        //assert key != null, "... valid(String |${key}|) is null."
        key = ( key.size() > 3 ) ? key.trim() : key.trim().toUpperCase();

        if ( key in keys ) { return true; }
        
        say "... valid(String |${key}|) is not valid."
        //throw new RuntimeException("... IO.valid(key) method disallowed use of invalid function key of |${key}|");

        return false;
    } // end of method


    /**
     * Method to unpack the input text into possibly two pieces, a tooltip text
     * assumed to be the first piece, but is optional, and a second piece, the remainder
     * of text. The tooltip is delimited by | vertical bars. So 
     * |Hello |   World. 
     * would give us a tooltip of 'Hello ' and a payload of '   World'
     * @param  String text payload for this function key that may have tooltip text prefix within || delimiters
     */
    public void converter(String input)
    { 
        payload = "";
        tooltip = "";
        //println "String=<"+input+">";
        input = (input==null) ? "" : input.trim();
            
        boolean has = (input.startsWith("|"));
        def ix = input.indexOf('|') 

        // true when tooltip is thought to start this text
        if (has && ix > -1)
        {
            tooltip = input.substring(1);
            def ix2 = tooltip.indexOf('|') 
            
            // no second | found
            if (ix2 < 0)
            {
                payload = input;
                tooltip = "";
            }
            else
            {
                payload = tooltip.substring(ix2+1);
                tooltip = tooltip.substring(0,ix2)
            
                ix = tooltip.indexOf('|') 
            } // end of else          
        } // end of if
        else
        {
                payload = input;
        } // end of else
    } // end of method


    /**
     * --------------------------------------------------
     * IO File Utility Methods
     * 
     * Two versions of each method. Methods with no parameters will use existing values for the
     * declared 'functionkey'
     *--------------------------------------------------
     */

    /**
     * Method to see if a file exists for any functionkey. It does NOT change state of existing
     * var.s
     *
     * @param  String simple name of function key to find: F1 = /Users/jim/copybooks/F1.txt
     * @return Boolean becomes true if it exists or false if not
     */
    public boolean chkobj(String key) 
    {
        key = (key==null) ? "" : key;
        // if input value was a good valid choice
        boolean present = pf.hasFunctionKeyFileName(key);        
        say "... chkobj(${key}) exists ? "+present+"; looking for functionkey text file for |${key}|";
        say "    does not change state of var's in this instance"
        return present;
    } // end of method


    /**
     * Method to see if a file exists for our function key, using name of the copybook functionkey
     * but does NOT change state of any var.s for this class instance
     *
     * @return class-level boolean becomes true if it exists or false if not
     */
    public boolean chkobj() 
    {
        boolean present = pf.hasFunctionKeyFileName(functionkey);
        say "... chkobj(${functionkey}) exists ? "+present+"; looking for this functionkey file.";
        say "    does not change state of var's in this instance"
        return present;
    } // end of method


    /**
     * Method to read String of text from a file. copybookFilename has the filename. 
     * PathFinder code adds the home and folder name when we look for function key payloads for our F5 app.
     * Also if missing a trailing suffix like .txt then .txt is added
     *
     * @return  boolean that's true if file was read successfully or else false when it failed
     */
    private boolean read() 
    {
        say "--- IO.read() default using functionkey of "+functionkey;

        assert functionkey != null, ".. -> IO.read() found functionkey of null & failed"
        boolean good = pf.hasFunctionKeyFileName(functionkey);
        copybookFilename = pf.getFunctionKeyFileName(functionkey);
		
        if (good)
        {
            try
            {
                def xxx =  new File(copybookFilename).getText('UTF-8');
                say "... IO.read() found <${copybookFilename}> file with ${xxx.size()} byte payload";
                good = true; 
                
                // strips tooltip from payload if any & populates tooltip variable
                converter(xxx); 
                show();
            } // end of try
            catch(Exception e)
            {
                say "... IO.read() found exception reading <${copybookFilename}> :" + e.toString();
                good = false;
            } // end of else
        } // end of if good

        say "--- IO.read() returned="+good;
        return good;
    } // end of method


    /**
     * Method to read String of text from a file.  
     * boolean copybookFound is set/reset here if file exists meaning there must be a payload and/or tooltip
     *
     * @return  boolean that's true if file was read successfully or else false when it failed
     */
    private boolean read(String key) 
    {
        say "--- IO.read(${key})"
        key = (key==null) ? "" : key;
        reset(key);   // keeps new 'key' in functionkey var.
        show();
        return read();
    } // end of method


    /**
     * Method to write String of text to a simple named file; this method will prefix our own home + folder names
     * filename has simple name of text file to write like F12.txt
     *
     * @param  String of text to write to file
     * @param  String of name of file to write to 
     * @return true, if write was successful
     */
    public boolean write(String payload, String filename) 
    {
        say "--- IO.write(${payload} ${filename})"
    	def file5 = new File(filename)
        boolean present = file5.exists();
        def x3 = (present) ? "yes" : "no";
        say "    ${filename}) present="+x3;

        try
        {
            if (payload==null) { payload=""; }
                file5.withWriter('UTF-8') { writer ->
                writer.write(payload)
            } // end of withWriter
            present = true;
        } // end of try
        catch(IOException x)
        {
            say "... IO.write method failed to write <${filename}> due to:"+x.message;
            present = false;
        }

        say "   returned:"+present;
        return present;
    } // end of method


    /**
     * Method to write String of text to a simple named file; this method will use our own home + folder + file name
     * filename has simple name of text file to write like /Users/jim/copybooks/F12.txt
     *
     * @param  String of text to write to file
     * @return true, if write was successful
     */
    public boolean write(String payload) 
    {
        // present may be false if this payload file never existed, or not there
        boolean present = pf.hasFunctionKeyFileName(functionkey);
        String filename = pf.getFunctionKeyFileName(functionkey);

        say "--- IO.write(${payload} ${filename}) present="+present;
        def file6 = new File(filename)

        try
        {
            if (payload==null) { payload=""; }
                file6.withWriter('UTF-8') { writer ->
                writer.write(payload)
            } // end of withWriter
            present = true;
        } // end of try
        catch(IOException x)
        {
            say "... IO.write method failed to write <${filename}> due to:"+x.message;
            present = false;
        }

        say "   returned:"+present;
        return present;
    } // end of method

    /**
     * Method to write current payload String of text to a simple named file; 
     * this method will use our own home + folder + file name where
     * filename has simple name of text file to write like /Users/jim/copybooks/F12.txt
     *
     * @return true, if write was successful
     */
    public boolean write() 
    {
        boolean present = pf.hasFunctionKeyFileName(functionkey);
        String filename = pf.getFunctionKeyFileName(functionkey);

        say "--- IO.write(${payload} ${filename}) present="+present;
        def file7 = new File(filename)

        try
        {
            if (payload==null) { payload=""; }
            file7.withWriter('UTF-8') { writer ->
                writer.write(payload)
            } // end of withWriter
            present = true;
        } // end of try
        catch(IOException x)
        {
            say "... IO.write method failed to write <${filename}> due to:"+x.message;
            present = false;
        }

        say "   returned:"+present;
        return present;
    } // end of method


    /**
     * Method to delete and remove a simple named file; this method will prefix our own home + folder names
     *
     * @param  String simple name of text file to write like F12.txt
     * @return true, if delete was successful
     */
    public boolean delete(String key) 
    {
        key = (key==null) ? "" : key;
        say "--- IO.delete(${key})"
        reset(key);
        def file8 = new File(copybookFilename)
        boolean present = file8.delete();
        say "... deleted file <${copybookFilename}> ? "+present;
        return present;
    } // end of method


    /**
     * Method to delete and remove a simple named file; this method will prefix our own home + folder names
     *
     * @return true, if delete was successful
     */
    public boolean delete() 
    {
        say "--- IO.delete()"
        boolean present = pf.hasFunctionKeyFileName(functionkey);
        String filename = pf.getFunctionKeyFileName(functionkey);

        def file9 = new File(filename)
        present = file9.delete();
        say "... deleted file <${filename}> ? "+present;
        return present;
    } // end of method


    /**
     * Method to display current values of all metadata
     *
     */
    public boolean show()
    {
        say "-------------------------|"
        say " IO functionkey         =|"+functionkey+"|";
        say "    copybookFound       =|"+copybookFound+"|";
        say "    copybookFilename    =|"+copybookFilename+"|";
        say "    tooltip             =|"+tooltip+"|";
        say "    payload             =|"+payload+"|";
        say "    PathFinder variables:"
        say "    pf.currentDirectory =|"+pf.currentDirectory+"|";
        say "    pf.homePath         =|"+pf.homePath+"|";
        say "    pf.copyPathDirectory=|"+pf.copyPathDirectory+"|";
        say " ";
    } // end of show


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
        IO ck = new IO();
        ck.reset("F12");
        //ck.show();
        def xx = ck.getPayload(); 
        println "    F12 key payload is    |"+xx+"|";
        xx = ck.getPayload("F12") 
        println "    ck.getPayload(F12) is |"+xx+"|";
	xx = ck.getToolTip();
        println "    ck.getToolTip() is |"+xx+"|";

        ck.reset("F11");
        xx = ck.getPayload() 
        println "    F11 key payload is    |"+xx+"|";
        xx = ck.getPayload("F11") 
        println "    ck.getPayload(F11) is |"+xx+"|";
	xx = ck.getToolTip();
        println "    ck.getToolTip() is |"+xx+"|";
	ck.show();

System.exit(0);



        String x5 = " f17 ";
        ck.reset(x5);
        ck.show();

        ck.reset("f13");
        ck.show();

        boolean ok = true;
        ok = ck.delete(); 
        ck.say "    ok="+ok;
        ck.show();

        ok = ck.delete("F13"); 
        ck.say "    ok="+ok;
        ck.show();

        ok = ck.delete("/Users/jim/copybooks/F13.txt"); 
        ck.say "    ok="+ok;
        ck.show();

        ok = ck.setFunctionKey("F12"); 
        ck.say "    ok="+ok;
        ck.show();

        x5 = ck.getPayload("F12");     
        ck.say "    x5="+x5;
        ck.show();

        x5 = ck.getPayload();
        ck.say "    x5="+x5;
        ck.show();

        x5 = ck.getPayload("F13");
        ck.say "    x5="+x5;
        ck.show();

        x5 = ck.getPayload();
        ck.say "    x5="+x5;
        ck.show();


        //ck.say "... F12.filename ? "+ck.filename;
        //ck.say "... F12.present ? "+ck.present;
        ck.say "... ck.functionkey ? "+ck.functionkey+" copybookFound="+ck.copybookFound+" for "+ck.copybookFilename;
        ck.say "... ck.payload ? "+ck.payload+" copybookFound="+ck.copybookFound+" for "+ck.copybookFilename;
        ck.say "... ck.tooltip ? "+ck.tooltip+" copybookFound="+ck.copybookFound+" for "+ck.copybookFilename;

        // start again ....
        ck = new IO(true);
        ck.show();

        x5 = ck.getToolTip("F18");
        ck.say "--- New IO()"
        ck.show();
        ck.say "    getToolTip with value --------"
        ck.say "    getToolTip(F18)  =|"+x5+"| |"+ck.functionkey+"| copybookFound="+ck.copybookFound+" for "+ck.copybookFilename;

        x5 = ck.getToolTip();
        ck.say "    getToolTip with no value ------"
        ck.say "    getToolTip()     =|"+x5+"| |"+ck.functionkey+"| copybookFound="+ck.copybookFound+" for "+ck.copybookFilename;

        ok = ck.exists();
        ck.say "    exists()         =|"+ok+"| |"+ck.functionkey+"| copybookFound="+ck.copybookFound+" for "+ck.copybookFilename;
        ok = ck.exists("F24");
        ck.say "    exists(F24)      =|"+ok+"| |"+ck.functionkey+"| copybookFound="+ck.copybookFound+" for "+ck.copybookFilename;


        ck.say ""	
        boolean flag = ck.chkobj("F1");
        ck.say "... flag="+flag;
        ck.say "... ck.payload ? "+ck.payload;
        ck.say "... ck.tooltip ? "+ck.tooltip;

        String g = "|Here's a tip|Hi kids; this is a sample payload with tooltip.";
        ck.say "\n... g="+g;
        ck.converter(g);
        ck.say "\n... ck.converter() returned this payload:'"+ck.payload+"'";
        ck.say "... ck.tooltip ? '"+ck.tooltip+"'";

        ck.say "... ------------------------"
        ck = new IO(true);
        ck.say "... functionkey ? "+ck.functionkey;
        //ck.say "... F23.present ? "+ck.present;
        ck.say "... payload ? "+ck.payload;
        ck.say "... tooltip ? "+ck.tooltip;
        ck.say "";	

        ck.say "... ------------------------"

        ck.say "\n---------------- F15  -----------------------------"
        String ky = "F15";
        try{
            ck = new IO(true);
            ck.reset(ky);
        	ok = ck.delete();
        	ck.say "... did we delete |${ky}| file named |${ck.copybookFilename}| ? :"+ok;
            def dt = new Date().toString();
            ok = ck.write("|F15 tooltip goes here|Payload for F15 is here on "+dt);
        	ck.say "... did we write tooltip to <${ck.copybookFilename}> file ok ? :"+ok;
        } 
        catch (Exception x6)
        { 
            ck.say "... F15 access failed:"+x6;
        }

        // use chkobj to confirm F15.txt is there 
        ck.say "\n"
        ck.say "... ck.pf.CopyPathDirectory:"+ck.pf.getCopyPathDirectory();
        ck.say "... ck.copybookFilename:"+ck.copybookFilename;
        ck.say "... ck.functionkey        :"+ck.functionkey;

        boolean yn = ck.chkobj(ck.functionkey);
        ck.say "... ck.functionkey="+ck.functionkey+" yn="+yn;
        ck.say "... file "+ck.functionkey+" exists ? "+ck.exists("/Users/jim/copybooks/F15.txt");
        String tx = "";

        ck.say "\n -----------------"
        if (yn)
        {
            ck.say "... yn="+yn;
            ck.say "... yn="+yn;
            ck.copybookFilename = ck.pf.getFunctionKeyFileName();
            ck.say "... yn=${yn} and file "+ck.copybookFilename+" exists ? "+ck.exists(ck.getCopybookFilename())+"\n ";
            

            tx = ck.read();
            ck.say "... <${ky}> read found:"+tx.length()+" bytes in file "+ck.copybookFilename;
        } // end of if
            
        ck.say ""
        ck.say "\n---------------- F17  -----------------------------"
        ky = "f17";
        try{
            ck = new IO(true);
            ck.reset(ky);
            yn = ck.exists();
            if (yn) 
            { 
                yn = ck.delete();
                ck.say "    delete() yn="+yn; 
            } // end of if

            yn = ck.write("ok now we write ${ck.copybookFilename} file");
            ck.say "... F17 write trial gave yn="+yn;
            
            yn = ck.delete(ky);
            ck.say "    delete(${ky}) yn="+yn; 
        } 
        catch (Exception x7)
        { 
            ck.say "... F17.txt failed:"+x7;
            yn = false;
        }

        x5 = (yn) ? "yes" : "no"; 
        ck.say "... did we fix <${ck.copybookFilename}> ok ? :"+x5; // yes, writes to previously good invoke of IO module with F15 choice, so this msg goes into F15.txt    

        println "\n\n------------------------\n Test Session Two"
        IO io = new IO(true);
        io.show();
        io.say "   io.getFunctionKey() default="+io.getFunctionKey();
        io.say "   io.getToolTip() default="+io.getToolTip();
        io.say "   io.getPayload() default="+io.getPayload();
        io.say "   io.exists() default="+io.exists();


        io.reset();
        io.show();
        io.reset("F2")
        io.show();
        
        boolean ok2 = io.chkobj();
        io.say "    ok2="+ok2;
        io.show();
        boolean ok3 = io.chkobj("F1"); 
        io.say "    ok3="+ok3;
        io.show();
        io.reset(); // what values after default reset ?
        io.show();
        
        println "\n--- the end---"
    } // end of main 

} // end of class
