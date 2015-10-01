package edu.mum.lms.commonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class EnvironmentUtil {
    
    public static String pathSep = System.getProperty("file.separator");
    
    private static String workDir = getWorkDir();
    
    private static Logger log = loggerForThisClass();
    
    public static void main(String[] args) {
        System.out.println(getPropertyFromFile(getPropertiesFilePath(), "jdbc.user"));
    }
    
    
    public static String getWorkDir(){
        if (System.getenv("LMS_CONF") != null) {
            workDir = System.getenv("LMS_CONF");
        } else if (System.getProperty("work_dir") != null) {
            workDir = System.getProperty("work_dir");
        } if(workDir == null){
            workDir = findWorkDir();
        }
        
        if(workDir == null){
            throw new RuntimeException("Please add .syconfig file in ");
        }
        System.setProperty("LMS_CONF", workDir);
        return workDir;     
    }
    
    public static Logger loggerForThisClass() {
        StackTraceElement myCaller = Thread.currentThread().getStackTrace()[2];
        return Logger.getLogger(myCaller.getClassName());
    }
    
    public static String getPropertiesFilePath(){
        try {
            Properties prop = new Properties();
            String confFile = getFilePath(getWorkDir(), "lms.properties");
            return confFile;
        } catch (Exception e) {
            throw new RuntimeException("Cannot read properties file");
        } 
    }
    
    private static String findWorkDir() {
        String homeDir = System.getenv("HOME");
        String path = getFilePath(homeDir, ".syconfig");
        String workDir = null;
        if(isFileExist(path)){
            workDir = getPropertyFromFile(path, "LMS_CONF");
        }
        return workDir;
    }
    
    private static String getFilePath(String... paths){
        StringBuilder path = new StringBuilder();
        for(int i=0; i < paths.length; i++){
            path.append(paths[i]);
            if(i != paths.length-1){
                path.append(File.separator);
            }
        }
        return path.toString();
    }
   
    
    public static String getPropertyFromFile(String filePath, String property) {
        Properties prop = readPropertiesFromFile(filePath);
        return prop.getProperty(property);
    }
    
    public static Properties readPropertiesFromFile(String filePath){
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(filePath));
            return prop;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private static boolean isFileExist(String path) {
        File f = new File(path);
        return f.exists();
    }
    
    

}
