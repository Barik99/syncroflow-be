package vlad.mester.syncroflowbe;

import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;

public class FileController {
    public static final File root = new File("FileDirectory");

    public String addDirectory(String path, String name) {
        if (!checkPath(path))
            return "Invalid path";
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
        if (!checkPath(path))
            return "Invalid path";
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
        if (file.getPath().equals(root.getPath()))
            return "Cannot delete root directory";
        file.delete();
        return "Directory deleted";
    }

    public String addFile(String path, MultipartFile file) {
        if (!checkPath(path))
            return "Invalid path";
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
        if (!checkPath(path))
            return "Invalid path";
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

    public boolean checkPath(String path) {
        return path.contains(root.getPath());
    }

    public JSONObject getDirectoryStructure(String path) {
        if (!checkPath(path))
            return null;
        File file = new File(path);
        if(!file.exists())
            return null;
        JSONObject directory = new JSONObject();
        directory.put("name", file.getName());
        directory.put("path", file.getPath());
        directory.put("isDirectory", file.isDirectory());
        if (file.isDirectory()) {
            JSONObject[] children = new JSONObject[file.list().length];
            for (int i = 0; i < file.list().length; i++) {
                children[i] = getDirectoryStructure(file.getPath() + "/" + file.list()[i]);
            }
            directory.put("children", children);
        }
        return directory;
    }
}
