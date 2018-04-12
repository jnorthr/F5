package io.jnorthr.toolkit;

/*
 * Feature to read text from a File
 */
public class IO
{
    /** an O/S specific char. as a file path divider */
    String fs = java.io.File.separator;


    /** an O/S specific location for the user's home folder name plus a trailing file separator*/ 
    String home = System.getProperty("user.home")+fs;


    /** a folder-relative location for the user's home folder name */ 
    String folder = "copybooks"+fs;


    /** If we need to println audit log to work, this will be true */ 
    boolean audit = false;


    /**
     * Default constructor builds a tool to read text from a file
     */
    public IO()
    {
        say "... IO() home=[${home}] relative folder=[${folder}]"
        confirm();
    } // end of default constructor


    /**
     * Non-Default constructor builds a tool to read text from a file
     */
    public IO(boolean ok)
    {
        audit = ok;
        say "... IO() home=[${home}] absolute folder=[${folder}]"
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
     * @param  String simple name of text file to read: F1.F5= /Users/jim/copybooks/F1.F5
     * @return boolean true if it exists or false if not
     */
    public boolean chkobj(String s) 
    {
    	String fn = home + folder + s;
        boolean good = new File(fn).exists();
        say "... chkobj(String ${s}) exists ?="+good+"; looking for file |${fn}|";

        return good;
    } // end of method


    /**
     * Method to read String of text from a file. The @param gives the simple filename. 
     * This code adds the home and folder name when we look in for our F5 app.
     * Also if missing a trailing suffix like .txt or .F5 then .F5 is added
     *
     * @param  String simple name of text file to read: F1.F5 = /Users/jim/copybooks/F1.F5
     * @return String content of file, if it exists or null if not
     */
    public String read(String s) 
    {
    	def i = s.lastIndexOf('.');
    	if (i<0) { S += ".F5"; }

        boolean present = chkobj(s); // home + folder added in chkobj

        String cb = null;
        if (present)
        {
	    	String fn = home + folder + s;
            cb =  new File(fn).getText('UTF-8');
        	say "... found ${fn} file with ${cb.size()} byte payload";
        } // end of if

        return cb;
    } // end of method


    /**
     * Method to read String of text from an F5 file. 
     * This code adds the home and folder names we look in for our F5 app plus a '.F5' suffix.
     * For example, a @param of 'F12' may point to an existing "/Users/jim/copybooks/F12.F5" file
     *
     * @param  String name of text file to read, like 'F6' - we add the '.F5' suffix when looking
     * @return String content of file, if it exists or blank if not
     */
    public String getPayload(String fn) 
    {
    	boolean ok = (fn.endsWith(".F5")) ? true : false;
        if (!ok) { fn+=".F5"} 

        String s = home + folder + fn; // = ex.: /Users/jim/copybooks/F6.F5
        String cb = "";

        // check for this function key file in current folder
        ok = chkobj(fn);
        say "... IO.getPayload(${fn}) looking for |${s}| file; ok="+ok;

        if (ok)
        {
            def fin = new File(s);
        	say "... setup file for ${s}; ok=${ok}";
            cb = fin.getText('UTF-8');
        } // end of if
        else
        {
        	say "... did not find ${s} for getPayload(${fn})";
        } // end of else

        return cb;
    } // end of method


    /**
     * Method to read String of tooltip text from a .txt file
     * This code adds the home and folder name we look in for our F5 app plus a '.txt' suffix.
     *
     * @param  String name of tooltip file to read, like 'F6' to find /Users/jim/copybooks/F6.txt
     * @return String tooltip content of file, if it exists or blank if not
     */
    public String getToolTip(String fn) 
    {
        if (!fn.endsWith(".txt")) { fn += ".txt"} 

        String s = home+folder+fn;
        String cb = "";

        say "... IO.getToolTip(${fn}) looking for file |${s}|"

        // check for this tooltip for this function key file in known home + folder
        def f =  new File(s)
        boolean present = f.exists();

        if (present)
        {
            say "... found tooltip ${s}";
            cb = f.getText('UTF-8');
        } // end of if

        return cb;
    } // end of method


    /**
     * Method to write String of text to a simple named file; this method will prefix our own home + folder names
     *
     * @param  String simple name of text file to write like F12.F5
     * @param  String of text to write to file
     * @return true, if write was successful
     */
    public boolean write(String fn, String payload) 
    {
        String s = home + folder + fn;
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
            say "... failed to write |${s}| due to:"+x.message;
        	present = false;
        }

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
            println "... confirm folder fo=|${fo}| to build 'copybooks';"
            FileTreeBuilder treeBuilder = new FileTreeBuilder(new File(fo))
            treeBuilder.dir("copybooks");
            println "... built folder"
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
        String fn = "F15.F5";
        boolean yn = ck.chkobj(fn);
	    String tx = "";

        if (yn)
        {
	        tx = ck.read(fn);
    	    println "... |${fn}| read found:"+tx;
    	} // end of if

        boolean ok = ck.write(fn, "ok now we write ${fn} file");
        println "... did we write |${fn}| ok ? :"+ok;

        fn = "F15.txt";
        ok = ck.write(fn, "F15 tooltip goes here");
        println "... did we write tooltip to |${fn}| file ok ? :"+ok;

        fn = "F15";
        tx = ck.getPayload(fn);
        println "... found |${fn}| file ? :\n... payload has "+tx.size()+" bytes";
        if (tx.size() < 1) { println "... none found for |${fn}|"}

        fn = "F14";
        tx = ck.getPayload(fn);
        println "... found |${fn}| file ? :\n... payload has "+tx.size()+" bytes";
        if (tx.size() < 1) { println "... none found for |${fn}|"}

        tx = ck.getToolTip(fn);
        println "... found |${fn}| tooltip file ? :\n... payload has "+tx.size()+" bytes";
        if (tx.size() < 1) { println "... no tooltips found for |${fn}|"}

        println "--- the end---"
    } // end of main 

} // end of class
