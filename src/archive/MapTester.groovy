println "\nF5Data dump() :"
Map hasPayload = [:];
Map tooltips = [:];
Map payloads = [:];

def tx = "";
hasPayload["F1"] = true;
tooltips["F1"] = "Tooltip for F1";
payloads["F1"] = "Hello World";

hasPayload.eachWithIndex { val, index ->
    tx = (val) ? "yes" : "no" ;
    println "entry ${index} is ${val.key} = ${tx} hasPayload="+hasPayload[val.key]
    println "                tooltip:"+tooltips[val.key];
    println "                payloads:"+payloads[val.key];
}


println "--- the end ---"
