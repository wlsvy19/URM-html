package com.ism.urm.service.rule;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import com.ism.urm.dao.rule.AppSystemDao;
import com.ism.urm.vo.rule.system.AppSystem;


public class AppSystemService extends RuleService<AppSystem> {

    private static int _seq = 0;
    
    private static Object _lock = new Object();
    
    public AppSystemService() {
        super();
        dao = new AppSystemDao();
    }

    public String requestFileUpload(String fileName, byte[] data) throws Exception {
        logger.info("RequestService.requestFileUpload() call.");
        
        logger.info("file name:" + fileName);
        
        int i = fileName.lastIndexOf('.');
        String extension = "";
        if (i > 0) {
            extension = fileName.substring(i);
        }
        String upFileName = null;
        
        synchronized (_lock) {
            upFileName = null;//Accessory.getFormatDate("yyyyMMdd_HHmmss") + "_" + _seq++ + extension;
            if (_seq > 999) {
                _seq = 1;
            }
        }
         
        logger.info("FILE ENCODING :" + System.getProperty("file.encoding"));
        
        String utfFileName = new String(upFileName.getBytes("UTF-8"));
        logger.info("UTF-8  file name :" + utfFileName);
        
        String euckrFileName = new String(upFileName.getBytes("euc-kr")); 
        logger.info("EUC-KR file name :" + euckrFileName);
        
        String localFileName = new String(upFileName.getBytes(System.getProperty("file.encoding"))); 
        logger.info("Local file name :" + localFileName);
        
        File outFile = new File(System.getProperty("urm.file.repository"), upFileName);
        
        BufferedOutputStream bos = null;
        
        try {
            bos = new BufferedOutputStream(new FileOutputStream(outFile));
            bos.write(data);
            bos.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                bos.close();
            } catch (Exception ee) {}
            bos = null;
        }
        
        return upFileName;
    }
    

}
