        //String tt = "|tool tip| This is the payload";
        String inputx = null;
        payload = "";
        tooltip = "";
        /*
         * Method to unpack the input text into possibly two pieces, a tooltip text
         * assumed to be the first piece, but is optional, and a second piece, the remainder
         * of text. The tooltip is delimited by | vertical bars. So 
         * |Hello |   World. 
         * would give us a tooltip of 'Hello ' and a payload of '   World'
         */
        public void converter(String input)
        { 
            payload = "";
            tooltip = "";
            //println "String=<"+input+">";
            input = (input==null) ? "" : input;
            
            boolean has = (input.startsWith("|"));
            def ix = input.indexOf('|') 

            if (has && ix > -1)
            {
                tooltip = input.substring(1);
                def ix2 = tooltip.indexOf('|') 
            
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

converter(inputx);        
println "tooltip =<"+tooltip+">";
println "payload =<"+payload+">\n";            

converter(null); // nulls are converted to empty string within method        
println "tooltip =<"+tooltip+">";
println "payload =<"+payload+">\n";            


converter("|  x|");        
println "tooltip =<"+tooltip+">";
println "payload =<"+payload+">\n";            


converter("|This is a tooltip. ±§!@£+={}[]| The payload " + "\n" + "goes here.");        
println "tooltip =<"+tooltip+">";
println "payload =<"+payload+">\n";            

println "=== the end ==="