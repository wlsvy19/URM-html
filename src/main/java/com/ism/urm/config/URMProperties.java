package com.ism.urm.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class URMProperties {

    private static Properties urmProps = new Properties();
    
    public URMProperties() {
        // TODO Auto-generated constructor stub
    }
    
    public static void init() {
//        String ismHomeDir = System.getenv("ISM_HOME");
//        if (ismHomeDir == null || ismHomeDir.trim().length() == 0) {
//            ismHomeDir = ".";
//        }

        String confFileName = System.getProperty("urm.conf");
        try {
            if (confFileName == null || confFileName.trim().length() == 0 ) {
                confFileName = "/urm.properties";
            }
            File file = new File(confFileName);
            if (!file.exists()) {
                throw new IllegalStateException("urm.conf file[" + confFileName + "] does not exist.");
            }
            System.out.println("URMProperties init - " + file.getAbsolutePath());
            
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                urmProps.load(fis);
            } catch (Exception e) {
                throw e;
            } finally {
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static String get(String key) {
        String value = urmProps.getProperty("urm." + key);
        if (value == null) {
            value = "";
        }
        return value;
    }
}
