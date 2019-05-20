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

    /** If we need to println audit log to work, this will be true */ 
    boolean audit = false;


    /** a string of most recent file contents from external file */ 
    String payload = "";


    /** a string of most recent file contents between || */ 
    String tooltip = "";

    /**
    * present is true if 'fn' file exists meaning there must be a payload and/or tooltip
    */
    boolean present = false;

    /**
     * Default constructor builds a tool to read text from a file
     */
    public IO()
    {
        say "... IO() home=<${home}> relative folder=<${folder}>"
        confirm();
    } // end of default constructor


    /**
     * Non-Default constructor builds a tool to read text from a file and set audit flag on/off
     */
    public IO(boolean ok)
    {
        audit = ok;
        say "... IO() home=<${home}> absolute folder=<${folder}>"
        confirm();
    } // end of non-default constructor


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
     * Method to see if a file exists
     * This code does not change filename to be confirmed; but the home plus copybooks/ folder names are added by our method.
     *
     * @param  String simple name of text file to read: F1.txt= /Users/jim/copybooks/F1.txt
     * @return boolean true if it exists or false if not
     */
    public boolean chkobj(String s) 
    {
      def i = s.lastIndexOf('.');
      if (i<0) { s += ".txt"; }

      fn = home + folder + s;
      boolean good = new File(fn).exists();
      say "... chkobj(String ${s}) exists ?="+good+"; looking for file <${fn}>";

      return good;
    } // end of method


    /**
     * Method to read String of text from a file. The @param gives the simple filename. 
     * This code adds the home and folder name when we look in for our F5 app.
     * Also if missing a trailing suffix like .txt then .txt is added
     *
     * boolean present is set/reset here if file exists means there must be a payload and/or tooltip
     *
     * @param  String simple name of text file to read: F1.txt = /Users/jim/copybooks/F1.txt
     * @return String content of file, if it exists or null if not
     */
    public String read(String s) 
    {
    	def i = s.lastIndexOf('.');
    	if (i<0) { s += ".txt"; }

        // home + folder name temp. added in chkobj
        present = chkobj(s); 

        String cb = null;
        if (present)
        {
	    	  fn = home + folder + s;
          cb =  new File(fn).getText('UTF-8');
        	say "... IO.read(${s}) found path <${fn}> file with ${cb.size()} byte payload";
        } // end of if

        return cb;
    } // end of method


    /**
     * Method to read String of text from an F5 text file. 
     * This code adds the home and folder names we look in for our F5 app plus a '.txt' suffix.
     * For example, a @param of 'F12' may point to an existing "/Users/jim/copybooks/F12.txt" file
     *
     * @param  String name of text file to read, like 'F6' - we add the '.txt' suffix when looking
     * @return String content of file, if it exists or blank if not
     */
    public String getPayload(String fn) 
    {
        String cb = read(fn);
        payload = "";
        tooltip = "";

        if (cb!=null)
        {
            cb = cb.trim();

            // logic to strip off leading tooltip text between || symbols
            def ix = cb.indexOf('|') 
            if (ix > -1)
            {
                String xx = cb.substring(1);
                ix = xx.indexOf('|')
                cb = xx.substring(ix+1);
                tooltip = xx.substring(0,ix);

                payload = cb+" ";
                say "... ${fn} has payload=<${cb}> tooltip=<${tooltip}>";
            } // end of if
            else
            {
                cb+=" ";
            }
        } // end of if
        else
        {
            cb = "";
        	say "... did not find file for getPayload(${fn})";
        } // end of else

        return cb;
    } // end of method


    /**
     * Method to find String of tooltip text from the text payload of a .txt file
     * This code adds the home and folder name we look in for our F5 app plus a '.txt' suffix.
     *
     * @param  String text payload for this function key that may have tooltip text prefix within || delimiters
     * @return String tooltip content of file, if it exists or blank if not
     */
    public String findToolTip(String cb) 
    {
        if (cb==null || cb.size()<1)
        {
            return "";
        } // end of if

        String tt = cb.trim();
        boolean present = (tt.startsWith("|"));
        def ix = tt.indexOf('|') 
        say "... findToolTip(String cb) ix=<${ix}>; is tooltip text present ? "+present;

        if (present && ix > -1)
        {
                tt = tt.substring(1);
                ix = tt.indexOf('|') 
                tt = cb.trim().substring(1, ix+1);
                say "... findToolTip(String cb) tt=<${tt}>";
        } // end of if
        else
        {
            tt = "";
            say "... findToolTip(String cb) ix=${ix} of first | & no tooltip."
        }

        return tt;
    } // end of method


    /**
     * Method to find String of tooltip text from the text payload of a .txt file
     *
     * @param  String text string for this function key that may have tooltip text within || delimiters
     * @return String tooltip content of file, if it exists or blank if not
     */
    public String getToolTip(String key) 
    {
        String cb = read(key);
        say "... IO.getToolTip(String ${key})="+cb;
        if (cb==null || cb.size() < 1)
        {
            return "";
        }

        String tt = findToolTip(cb);
        say "... IO.findToolTip(String ${cb})="+tt;
        if (tt==null || tt.size() < 1)
        {
            return "";
        }
        
        /*
        boolean ok = (tt.startsWith("|"));
        tt = tt.substring(1);
        def ix = tt.indexOf('|') 
        if (ok && ix > 1)
        {
                tt = cb.trim().substring(1, ix+1);
                say "... getToolTip(String ${key}) ix=${ix} tt=<${tt}>";
        } // end of if
        else
        {
            tt = "";
            say "... getToolTip(String ${key}) ix=${ix} tt=<${tt}>"
        }
        */
        
        return tt;
    } // end of method


    /**
     * Method to write String of text to a simple named file; this method will prefix our own home + folder names
     *
     * @param  String simple name of text file to write like F12.txt
     * @param  String of text to write to file
     * @return true, if write was successful
     */
    public boolean write(String fn, String payload) 
    {
        String s = home + folder + fn;
        def i = s.lastIndexOf('.');
        if (i<0) { s += ".txt"; }

    	  def file3 = new File(s)
        boolean present = file3.exists();

        try
        {
            if (payload==null) { payload=""; }
            
    		   file3.withWriter('UTF-8') { writer ->
              writer.write(payload)
			     }
            
          present = true;
        } // end of try
        catch(IOException x)
        {
          say "... write(String ${fn}) failed to write <${s}> due to:"+x.message;
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
    public boolean delete(String fn) 
    {
        String s = home + folder + fn;
        def file3 = new File(s)
        boolean present = file3.delete();
        say "... delete(String ${fn}) deleted file <${s}> ? "+present;
        return present;
    } // end of method


    /**
     * Method to confirm copybooks folder exists, or build it if it does not
     *
     * @return true, if folder build was successful
     */
    public boolean confirm() 
    {
        def fi = new File(home + folder);
        boolean yn = fi.exists() ? true : false;
        if (!yn)
        {
            String fo = home + fs;
            say "... confirm() folder fo=<${fo}> to build 'copybooks';"
            FileTreeBuilder treeBuilder = new FileTreeBuilder(new File(fo))
            treeBuilder.dir("copybooks");
            say "... confirm() built folder"
            yn = true;
        } // end of if

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
        IO ck = new IO(true);

        println "... ------------------------"
        String fn = "F15.txt";
        boolean ok = ck.delete(fn);
        println "... did we delete ${fn} file ? :"+ok;

        ok = ck.write(fn, "|F15 tooltip goes here|Payload for F15 is here.");
        println "... did we write tooltip to <${fn}> file ok ? :"+ok;

        // use chkobj to confirm F15.txt is there 
        boolean yn = ck.chkobj(fn);
	      String tx = "";

        if (yn)
        {
	        tx = ck.read(fn);
    	    println "... <${fn}> read found:"+tx;
    	  } // end of if

        println ""
        fn = "F17.txt";
        ok = ck.write(fn, "ok now we write ${fn} file");
        println "... did we write <${fn}> ok ? :"+ok;

        // try with file .txt suffix
        println ""
        fn = "F16.txt";
        ok = ck.write(fn, "|F16 tooltip goes here|");
        println "... did we write tooltip to <${fn}> file ok ? :"+ok;


        // try without file .txt suffix
        println ""
        fn = "F13";
        tx = ck.getPayload(fn);
        println "... found <${fn}> file ? :\n    payload has "+tx.size()+" bytes";
        if (tx.size() < 1) { println "    - none found for |${fn}|"}

        println ""
        fn = "F14";
        tx = ck.getPayload(fn);
        println "... found <${fn}> file ? "
        println "... payload has "+tx.size()+" bytes";
        if (tx.size() < 1) { println "... no payload found for <${fn}>"}

        println ""
        tx = ck.getToolTip(fn);
        println "... found <${fn}> tooltip file ? "
        println "... payload has "+tx.size()+" bytes";
        if (tx.size() < 1) { println "    - no tooltips found for <${fn}>"}

        println ""
        fn = "F18.txt";
        ok = ck.write(fn, "|F18 tooltip goes here|The payload for F18 is this.");
        println "... did we write tooltip to <${fn}> file ok ? :"+ok;

        println ""
        fn = "F19.txt";
        ok = ck.delete(fn);
        if (ok){ println "... deleted F19.txt"; }
        ok = ck.write(fn, "");
        println "... did we write tooltip to <${fn}> file ok ? :"+ok;
        tx = ck.getToolTip(fn);
        if (tx.size() < 1) { println "    - no tooltips found for <${fn}>"}
        tx = ck.getPayload(fn);
        if (tx.size() < 1) { println "    - no payload found for <${fn}>"}


        println ""
        fn = "F20";
        ok = ck.delete(fn);
        if (ok){ println "... deleted ${fn}"; }
        
        ok = ck.write(fn, null);
        println "... did we write tooltip to <${fn}> file ok ? :"+ok;

        tx = ck.getToolTip(fn);
        if (tx.size() < 1) { println "    - no tooltips found for <${fn}>"}
        
        tx = ck.getPayload(fn);
        if (tx.size() < 1) { println "    - no payload found for <${fn}>"}

        println "-------------------------------------------------"
        println "--- the end---"
    } // end of main 

} // end of class
