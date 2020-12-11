import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;

  String fs = java.io.File.separator;

    /** an O/S specific location for the user's home folder name plus a trailing file separator*/ 
    String home = System.getProperty("user.home")+fs;

    /** a folder-relative location for the user's home folder name */ 
    String metaFileName = home + "copybooks";
    JFileChooser fc = new JFileChooser();
    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fc.setDialogTitle("Hi kids");
                
    println "copybook folder name is "+metaFileName;
        File fi = new File(metaFileName);
        fc.setCurrentDirectory(fi);          
        def returncode = fc.showOpenDialog() ;
        println "... returncode ="+returncode;

        def xxx = fc.getSelectedFile();
        println "... getSelectedFile() = "+xxx;
        if (returncode==JFileChooser.APPROVE_OPTION)
        {
            println "JFileChooser.APPROVE_OPTION current dir:"+fc.getCurrentDirectory().getAbsolutePath();
            println "JFileChooser.APPROVE_OPTION choice dir:"+fc.getSelectedFile().getAbsolutePath();
        }

        File file = fc.getSelectedFile();
        println "... file="+file.toString();
 println "--- the end ---"       