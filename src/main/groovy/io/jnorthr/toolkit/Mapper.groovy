package io.jnorthr.toolkit;
import groovy.transform.*;
import javax.swing.*

/*
 * Feature to find template replacement values within a function key payload template string like: '${xxx}'
 * user is prompted via dialog for a replacement text value for 'xxx' field. So if users keys 'fred' then '${xxx}' becomes 'fred' 
 */
public class Mapper
{
    /** If null keys found within the template String, this will be true */ 
    boolean hasMap = false;

    /** If we need to println audit log to work, this will be true */ 
    boolean audit = false;

    /** a variable holding Groovy template String */ 
    String tempText = "";

    /**
     * Default constructor builds a tool 
     */
    public Mapper()
    {
    } // end of default constructor
    

    /**
     * Non-Default constructor builds a tool 
     */
    public Mapper(boolean yn)
    {
        audit = yn;
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
     * A method to ask user for a value for one replacement variable
     *
     * @param  i is text name of this replacement variable to show user in dialog
     * @return String value user provided
     */
    private String prompt(String i) 
    {
        JFrame jframe = new JFrame()
        String ss = "Enter value for {${i}} parameter ";
        String answer = JOptionPane.showInputDialog(jframe, ss)
        jframe.dispose()
        answer
    } // end of method


    /**
     * A method to look at a string for evidence of replacement variables needing values
     *
     * @param  s is text string of possible replacement variables that need values assigned to them in a dialog
     * @return Map of found replacement variables as keys plus user 
     */
    protected Map getMap(String s) 
    {
        tempText = s;
        Map map=[:]
        say s;
        int b = 0;
        int kv = 0;
        int k1 = 0;
        int k2 = 0;
        int k3 = 0;
        int k4 = 0;

        def x = s.split('\\$')
        say "... x.size()="+x.size()
        
        x.each{xx->
        	say "... xx=|->${xx};" 
		}
		say "\n------------------------\n"

        x.each{e-> 
            b+=1; // count all 'x' entries
            say " and e=|${e}| (b=${b}) ";
            int k = (e.size() > 1) ? 1 : 0;
            String y = e.substring(0,k);
            say " & y=|${y}| ";

            if (y=='{')
            { 
                say " has { "; 
                def i = e.indexOf('}');
                if (i > 0)
                {
                    say " and } i=|${i}| ";
                    y = e.substring(1,i).trim();
            
                    if(y.size()>0)
                    { 
                        say " has y=|${y}| has y.size() ="+y.size();
                        if(map.containsKey(y))
                        {
                            kv += 1;
                        }
                        else
                        {
                            hasMap = true;
                            map[y]=""
                            say " and added map key of =|${y}| ";
                
                            def ans = prompt(y); 
                            if (!(ans==null)) 
                            {
                                kv += 1;
                				ans = ans.trim();
                                map[y]=ans;
	                            say " added map[${y}] value of |${map[y]}|";
                            } // end of if 
                        } // end of else
                        
                    } // end of if
                    else
                    {
                        k4 +=1 ;
                    }
                        
                }
                else
                {
                    k1+=1;
                }                    
            } // end of if    
            else    
            {
                k2+=1;
            } // end of else

            k3+=1;
            say " ";
        } // end of each
        
        say "... b="+b+" and kv="+kv+" k1="+k1+" k2="+k2+" k3="+k3+" k4="+k4;
        return map;
    } // end of method


    /**
     * A method to translate a Template  
     *
     * @param  m is a Map of possibly zero entries
     * @return text string after template translation, or original constructor text if no ${} symbols exist
     */
    public String getTemplate(Map m)
    {
        if (!hasMap) { return tempText; }

        def engine = new groovy.text.SimpleTemplateEngine() 
        def template = engine.createTemplate(tempText).make(m);

        return template.toString(); 
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
        println "Hello from Mapper.groovy"
        def s = '''Hello. My name is ${name} and live in ${country} with the name of ${name}.'''.toString();

        Mapper ma = new Mapper(true);
        Map map = ma.getMap(s);
        
        println "----------------\neach:";
        map.each{k,v->
            println "... map[${k}]=|${v}|;"
        } // end of each

        s = ma.getTemplate(map);
        println "----------------\ntemplate:\n---------------------------\n"+s+"\n--------------------------";


        // second test
        s = '''Hello.'''.toString();
        map = ma.getMap(s);
        println "----------------\neach:";
        map.each{k,v->
            println "... map[${k}]=|${v}|"
        } // end of each

        s = ma.getTemplate(map);
        println "----------------\ntemplate:\n----------------------------\n"+s+"\n--------------------------";

	s = '''This is F4.F5 code in
the user folder using ${stuff} values.

/*
* Class to Write Things
*/
public class ${classname}
{

} // end of class
'''.toString();

        map = ma.getMap(s);

        println "----------------\neach:";
        map.each{k,v->
            println "... map[${k}]=|${v}|"
        } // end of each

        s = ma.getTemplate(map);
        println "----------------\ntemplate:\n------------------------------\n"+s+"\n--------------------------";

        println "--- the end---"
		//System.exit();
    } // end of main 

} // end of class