import java.io.File;
import java.nio.file.Files;

def fn = "C:\\Users\\Jim\\Documents\\Writings\\UNUSUAL.DOC"

println "file name is "+fn;
def file = new File(fn);
byte[] fileContent = Files.readAllBytes(file.toPath());

def ix = 0

new ByteArrayInputStream( fileContent ).each() { line ->
    ix += 1;
    if (ix<1000) { print line; print " "; }
}

println "--- the end ---"