package com.ism.urm.service.rule;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.ism.urm.dao.rule.data.DataDao;
import com.ism.urm.vo.rule.data.Data;

 
public class DataService extends RuleService<Data> {

    private DataDao dataDao;

    private static int _seq = 0;
    
    private static Object _lock = new Object();
    
    public DataService() {
        super();
        dao = new DataDao();
        dataDao = (DataDao) dao;
    }

    public void deleteRequest(String reqId) throws Exception {
        
        logger.info("RequestService.deleteRequest() call.");

        Session session = null;
        Transaction tx = null;

//        if(reqId == null ) throw new NotFoundRequestException("Exception:Argment reqId is null");
        try {
            session = sessionFactory.getCurrentSession();
            tx = session.beginTransaction();
            
            dataDao.delete(session, reqId);
            tx.commit();
        } catch (Exception e) {
            logger.error("", e);
            if (tx != null)    tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

//    public List<Request> getRequestHistroy(String reqId) throws Exception {
//        
//        debug.info("RequestService.getRequest() call. - " + reqId);
//        
//        if(reqId == null ) throw new NotFoundRequestException("Exception:Argment reqId is null");
//        
//        try {
//            return requestDao.getHistory(reqId);
//        } catch (Exception e) {
//            debug.error("", e);
//            throw e;
//        }
//    }

//    public List<RequestExcel> getRequestList_Excel(Map params) throws Exception {
//        
//        debug.info("RequestService.getRequestList() call.");
//        
//        if(params == null ) throw new NotFoundRequestException("Exception:Argment params is null");
//        
//        try {
//            return requestDao.listExcel(params);
//        } catch (Exception e) {
//            debug.error("", e);
//            throw e;
//        }
//    }

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
    

    
//    public byte[] makeRequestExcel(Map params) throws Exception {
//        debug.info("RequestService.getRequestList() call.");
//        
//        if(params == null ) throw new NotFoundRequestException("Exception:Argment params is null");
//        
//        try {
//            List<RequestExcel> list = requestDao.listExcel(params);
//            
//            String fileName  = "request_list_"+ Accessory.getFormatDate("yyyyMMdd_HHmmss") + params.get("loginUserId") + ".xls";
//            File excelFile = new File(System.getProperty("urm.file.repository"), fileName);
//            WritableWorkbook    workbook    = Workbook.createWorkbook(excelFile);
//            WritableSheet        sheet        = workbook.createSheet("Interface List", 0);
//            
//            WritableCellFormat headerFormat    = new WritableCellFormat();
//            WritableCellFormat dataFormat    = new WritableCellFormat();
//
//
//            headerFormat.setAlignment(Alignment.CENTRE); 
//            headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
//            headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
//            headerFormat.setBackground(Colour.GREY_25_PERCENT);
//            
//            dataFormat.setAlignment(Alignment.LEFT); 
//            dataFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
//            dataFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
//            
//            int x=0;
//            sheet.setColumnView(x, 18);
//            sheet.addCell(new Label(x, 0, "�Ϸù�ȣ", headerFormat));
//            sheet.mergeCells(x, 0, x, 1);
//            x++;
//
//            sheet.setColumnView(x, 22);
//            sheet.addCell(new Label(x, 0, "��Ǹ�",   headerFormat));
//            sheet.mergeCells(x, 0, x, 1);
//            x++;
//            
//            sheet.setColumnView(x, 16);
//            sheet.addCell(new Label(x, 0, "��������", headerFormat));
//            sheet.mergeCells(x, 0, x, 1);
//            x++;
//            
//            sheet.setColumnView(x, 16);
//            sheet.addCell(new Label(x, 0, "�۽ž����ڵ�", headerFormat));
//            sheet.mergeCells(x, 0, x, 1);
//            x++;
//            
//            sheet.setColumnView(x, 16);
//            sheet.addCell(new Label(x, 0, "�۽ž�����", headerFormat));
//            sheet.mergeCells(x, 0, x, 1);
//            x++;
//            
//            sheet.setColumnView(x, 16);
//            sheet.addCell(new Label(x, 0, "���ž����ڵ�", headerFormat));
//            sheet.mergeCells(x, 0, x, 1);
//            x++;
//
//            sheet.setColumnView(x, 16);
//            sheet.addCell(new Label(x, 0, "���ž�����", headerFormat));
//            sheet.mergeCells(x, 0, x, 1);
//            x++;
//
//            
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 0, "�������",       headerFormat));
//            sheet.mergeCells(x, 0, x, 1);
//            x++;
//            
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 0, "�����û����",    headerFormat));
//            sheet.mergeCells(x, 0, x, 1);
//            x++;
//            
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 0, "��������",    headerFormat));
//            sheet.mergeCells(x, 0, x, 1);
//            x++;
//            
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 0, "��������",    headerFormat));
//            sheet.mergeCells(x, 0, x, 1);
//            x++;
//            
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 0, "������",    headerFormat));
//            sheet.mergeCells(x, 0, x, 1);
//            x++;
//            
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 0, "2PC����",    headerFormat));
//            sheet.mergeCells(x, 0, x, 1);
//            x++;
//                        
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 0, "INTERFACE ID",    headerFormat));
//            sheet.mergeCells(x, 0, x, 1);
//            x++;
//
//            sheet.addCell(new Label(x, 0, "�۽� ��������",   headerFormat));
//            sheet.mergeCells(x, 0, x+2, 0);
//            
//            sheet.setColumnView(x, 10);
//            sheet.addCell(new Label(x, 1, "�۽� ���",        headerFormat));
//            x++;
//            sheet.setColumnView(x, 10);
//            sheet.addCell(new Label( x, 1, "�۽� �ý���",        headerFormat));
//            x++;
//            sheet.setColumnView(x, 10);
//            sheet.addCell(new Label(x, 1, "�۽ż���",    headerFormat));
//            x++;
//
//
//            sheet.addCell(new Label(x, 0, "���� ��������",   headerFormat));
//            sheet.mergeCells(x, 0, x+2, 0);
//            sheet.setColumnView(x, 10);
//            sheet.addCell(new Label(x, 1, "���� ���",        headerFormat));
//            x++;
//            sheet.setColumnView(x, 10);
//            sheet.addCell(new Label(x, 1, "���� �ý���",        headerFormat));
//            x++;
//            sheet.setColumnView(x, 14);
//            sheet.addCell(new Label(x, 1, "���ż���",    headerFormat));            
//            x++;
//            
//            
//            sheet.addCell(new Label(x, 0, "��� ����� ����",        headerFormat));
//            sheet.mergeCells(x, 0, x+6, 0);
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "������ȣ/ID",    headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "����",            headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "�̸�",            headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "�μ�",            headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "�繫��",    headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "������ȭ��ȣ",    headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "�ڵ��� ��ȣ",        headerFormat));
//            x++;
//            
//            sheet.addCell(new Label(x, 0, "�۽� ����� ����",        headerFormat));
//            sheet.mergeCells(x, 0, x+6, 0);
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "������ȣ/ID",    headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "����",            headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "�̸�",            headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "�μ�",            headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "�繫��",    headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "������ȭ��ȣ",    headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "�ڵ��� ��ȣ",        headerFormat));
//            x++;
//            
//            sheet.addCell(new Label(x, 0, "���� ����� ����",        headerFormat));
//            sheet.mergeCells(x, 0, x+6, 0);
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "������ȣ/ID",    headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "����",            headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "�̸�",            headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "�μ�",            headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "�繫��",    headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "������ȭ��ȣ",    headerFormat));
//            x++;
//            sheet.setColumnView(x, 12);
//            sheet.addCell(new Label(x, 1, "�ڵ��� ��ȣ",        headerFormat));
//            x++;
//            
//
//
//            
//            int i = 2;
//            for(RequestExcel data : list) {
//                x=0;
//                sheet.addCell(new Label(x++, i, data.getReqId(),        dataFormat));
//                sheet.addCell(new Label(x++, i, data.getReqNm(),        dataFormat));
//                
//                sheet.addCell(new Label(x++, i, data.getJobType(),        dataFormat));
//                sheet.addCell(new Label(x++, i, data.getSndJobCode(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getSndJobCodeNm(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getRcvJobCode(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getRcvJobCodeNm(),    dataFormat));
//                
//                sheet.addCell(new Label(x++, i, data.getPrcStatNm(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getChgStatNm(),    dataFormat));
//                
//                sheet.addCell(new Label(x++, i, data.getIfTypeNm(),        dataFormat));
//                sheet.addCell(new Label(x++, i, data.getTrTypeNm(),        dataFormat));
//                sheet.addCell(new Label(x++, i, data.getSyncTypeNm(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getTpcYn(),        dataFormat));
//                
//                sheet.addCell(new Label(x++, i, data.getIfId(),            dataFormat));
//                
//                sheet.addCell(new Label(x++, i, data.getSndMethodNm(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getSndHostNm(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getSndSvrNm(),        dataFormat));
//                
//                sheet.addCell(new Label(x++, i, data.getRcvMethodNm(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getRcvHostNm(),    dataFormat));                
//                sheet.addCell(new Label(x++, i, data.getRcvSvrNm(),        dataFormat));
//                
//                sheet.addCell(new Label(x++, i, data.getRegId(),            dataFormat));
//                sheet.addCell(new Label(x++, i, data.getRegAdmPosition(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getRegAdmNm(),        dataFormat));
//                sheet.addCell(new Label(x++, i, data.getRegAdmDept(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getRegAdmOffice(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getRegAdmTel(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getRegAdmCel(),    dataFormat));
//                
//                sheet.addCell(new Label(x++, i, data.getSndAdmId(),            dataFormat));
//                sheet.addCell(new Label(x++, i, data.getSndAdmPosition(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getSndAdmNm(),        dataFormat));
//                sheet.addCell(new Label(x++, i, data.getSndAdmDept(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getSndAdmOffice(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getSndAdmTel(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getSndAdmCel(),    dataFormat));
//                
//                sheet.addCell(new Label(x++, i, data.getRcvAdmId(),            dataFormat));
//                sheet.addCell(new Label(x++, i, data.getRcvAdmPosition(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getRcvAdmNm(),        dataFormat));
//                sheet.addCell(new Label(x++, i, data.getRcvAdmDept(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getRcvAdmOffice(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getRcvAdmTel(),    dataFormat));
//                sheet.addCell(new Label(x++, i, data.getRcvAdmCel(),    dataFormat));
//                                
//                i++;
//                
//            }            
//            workbook.write();
//            workbook.close();
//            
//            return getBytesFromFile(excelFile);
//
//        } catch (Exception e) {
//            debug.error("", e);
//            throw e;
//        }
//    }
    
    private byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

}
