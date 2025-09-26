package Sim;

import java.io.File;

public class LocalFile {
    String systemOs;
    String userHome;
    File dir;
    String fileName;
    File savedFile;

    public LocalFile() {
        systemOs = System.getProperty("os.name");
        userHome = System.getProperty("user.home");

        if (systemOs.equals("Windows")){
            dir = new File(userHome + "\\Desktop\\VCSFiles");
            if (!dir.exists()){
                dir.mkdir();
            }
        } else {
            dir = new File(userHome + "/Desktop/VCSFiles");
            if (!dir.exists()){
                dir.mkdir();
            }
        }
    }

    public File createLocalFile(String fileName){
        this.fileName = fileName;
        File createdFile = new File(dir, fileName);
        savedFile = createdFile;
        return createdFile;
    }

    public File getSavedFile(){
        if (savedFile.exists()){
            return savedFile;
        }
        else return (new File(dir, fileName));
    }

}
