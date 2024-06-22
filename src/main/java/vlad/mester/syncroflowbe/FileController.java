package vlad.mester.syncroflowbe;

import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileController {
    public static final File root = new File("FileDirectory");

    public String addDirectory(String path, String name) {
        if (!checkPath(path))
            return "Calea nu este validă!";
        File file = new File(path + "/" + name);
        if (!file.exists()) {
            if (file.mkdir()){
                return "Folderul a fost creat cu succes!";
            }
            return "Eroare la crearea folderului!";
        }
        return "Folderul deja există: " + file.getPath();
    }

    public String removeDirectory(String path) {
        if (!checkPath(path))
            return "Calea nu este validă!";
        File file = new File(path);
        if(!file.exists())
            return "Folderul nu există";
        if(!file.isDirectory()) {
            return "Calea nu este un folder!";
        }
        if(file.list().length > 0) {
            return "Folderul nu este gol!";
        }
        for (RuleController ruleController : RuleController.getAllInstances()) {
            if (ruleController.directoryIsUsed(file)) {
                return "Folderul este folosit!";
            }
        }
        if (file.getPath().equals(root.getPath()))
            return "Nu se poate șterge folderul principal!";
        file.delete();
        return "Folderul a fost șters cu succes!";
    }

    public String addFile(String path, MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            Path filePath = Paths.get(new File(path + "/" + file.getOriginalFilename()).getPath());

            if (Files.exists(filePath)) {
                return "Fișierul există deja!";
            }

            Files.write(filePath, bytes);

            return "Fișierul a fost încărcat cu succes!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Fișierul nu a putut fi încărcat!";
        }
    }

    public String removeFile(String path) {
        if (!checkPath(path))
            return "Calea nu este validă!";
        File file = new File(path);
        if(!file.exists())
            return "Fișierul nu există!";
        if(file.isDirectory()) {
            return "Calea nu este un fișier!";
        }
        for (RuleController ruleController : RuleController.getAllInstances()) {
            if (ruleController.fileIsUsed(file)) {
                return "Fișierul este folosit!";
            }
        }
        file.delete();
        return "Fișierul a fost șters cu succes!";
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
