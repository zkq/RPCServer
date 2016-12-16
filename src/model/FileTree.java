package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class FileNode {
    private String fileName;
    private long lastModified;
    private List<FileNode> childFiles;
    private boolean isDirectory = false;

    public FileNode(String fileName, long lastModified) {
        this.fileName = fileName;
        this.lastModified = lastModified;
        this.childFiles = new ArrayList<FileNode>();
    }

    public FileNode(String fileName, long lastModified, boolean isDirectory) {
        this(fileName, lastModified);
        this.isDirectory = isDirectory;
    }

    public String getFileName() {
        return fileName;
    }

    public List<FileNode> getChildFiles() {
        return childFiles;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void addChildFile(FileNode childFile) {
        if (childFile == null)
            throw new NullPointerException();
        childFiles.add(childFile);
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
}

public class FileTree {
    private FileNode root;

    public FileTree(File file) {
        boolean isDirectory = file.isDirectory();
        root = new FileNode(file.getName(), file.lastModified(), isDirectory);

        create(file, root);
    }

    public void create(File file, FileNode fileNode) {
        for (File subFile : file.listFiles()) {
            boolean isDirectory = subFile.isDirectory();
            FileNode node = new FileNode(subFile.getName(), subFile.lastModified(), isDirectory);
            fileNode.getChildFiles().add(node);
            if (isDirectory)
                create(subFile, node);
        }
    }


    /**
     * { name: x, lastModified: x, dir:[]}
     *
     * @param fileNode
     * @param directoryJO
     */
    public void toJson(FileNode fileNode, JSONObject directoryJO) {


        JSONArray fileJA = new JSONArray();
        for (FileNode childFile : fileNode.getChildFiles()) {
            if (childFile.isDirectory()) { //文件夹
                JSONObject dirJO = new JSONObject();
                toJson(childFile, dirJO);
                fileJA.put(dirJO);
            } else { //文件
                fileJA.put(toJsonHelp(childFile, new JSONObject()));
            }
        }

        toJsonHelp(fileNode, directoryJO);
        directoryJO.put("dir", fileJA);
    }

    public JSONObject getJson(){
        JSONObject jsonObject = new JSONObject();
        try {
            toJson(root, jsonObject);
            return jsonObject;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 文件夹结构(json格式: {root:[{a:[]},b]})
     *
     * @return json格式字符串
     */
    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            toJson(root, jsonObject);
            return jsonObject.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public FileNode getRoot() {
        return root;
    }

    public void setRoot(FileNode root) {
        this.root = root;
    }

    private JSONObject toJsonHelp(FileNode fileNode, JSONObject jsonObject) {
        jsonObject.put("name", fileNode.getFileName());
        jsonObject.put("lastModified", fileNode.getLastModified());
        return jsonObject;
    }

}

