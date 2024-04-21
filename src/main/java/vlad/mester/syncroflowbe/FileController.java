package vlad.mester.syncroflowbe;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;

public class FileController {
    File root = new File("FileDirectory");

    public String addDirectory(String path, String name) {
        if (path==null)
            path = root.getPath();
        File file = new File(path + "/" + name);
        if (!file.exists()) {
            if (file.mkdir()){
                return "Directory created";
            }
            return "Directory could not be created";
        }
        return "Directory already exists" + file.getPath();
    }

    public String removeDirectory(String path) {
        File file = new File(path);
        if(!file.exists())
            return "Directory does not exist";
        if(!file.isDirectory()) {
            return "Path is not a directory";
        }
        if(file.list().length > 0) {
            return "Directory is not empty";
        }
        for (RuleController ruleController : RuleController.getAllInstances()) {
            if (ruleController.directoryIsUsed(file)) {
                return "Directory is used";
            }
        }
        file.delete();
        return "Directory deleted";
    }

    public String addFile(String path, MultipartFile file) {
        Path destination = Path.of(path);
        if(!destination.toFile().exists()) {
            return "Path does not exist";
        }
        try {
            file.transferTo(destination);
            return "File uploaded";
        } catch (Exception e) {
            return "File upload failed";
        }
    }

    public String removeFile(String path) {
        File file = new File(path);
        if(!file.exists())
            return "File does not exist";
        if(file.isDirectory()) {
            return "Path is a directory";
        }
        for (RuleController ruleController : RuleController.getAllInstances()) {
            if (ruleController.fileIsUsed(file)) {
                return "File is used";
            }
        }
        file.delete();
        return "File deleted";
    }
}
