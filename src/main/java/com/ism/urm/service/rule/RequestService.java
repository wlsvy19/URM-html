package com.ism.urm.service.rule;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.web.multipart.MultipartFile;

import com.ism.urm.config.URMProperties;
import com.ism.urm.dao.manage.BusinessCodeDao;
import com.ism.urm.dao.rule.AppSystemDao;
import com.ism.urm.dao.rule.RequestDao;
import com.ism.urm.vo.PagingResult;
import com.ism.urm.vo.RelationOp;
import com.ism.urm.vo.RelationOp.OpType;
import com.ism.urm.vo.RelationOp.ValueType;
import com.ism.urm.vo.rule.request.Request;
import com.ism.urm.vo.rule.request.RequestHistory;


public class RequestService extends RuleService<Request> {

    private static int _seq = 0;
    private static Object _lock = new Object();

    private AppSystemDao sysDao;

    public RequestService() {
        super();
        dao = new RequestDao();
        sysDao = new AppSystemDao();
    }

    public PagingResult<Request> search(int page, int size, List<RelationOp> filter) throws Exception {
        PagingResult<Request> rtn = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            // 삭제 안된 목록만 조회
            if(filter != null) {
                filter.add(RelationOp.get("delYN", OpType.EQ, false, ValueType.BOOLEAN));
            }
            rtn = dao.searchPage(session, page, size, filter);
            
            // set child
            session = sessionFactory.openSession();
            session.beginTransaction();
            if (rtn.getList() != null) {
                for (Request r : rtn.getList()) {
                    r.setSendSystem(sysDao.get(session, r.getSendSystemId()));
                    r.setRcvSystem(sysDao.get(session, r.getRcvSystemId()));
                }
            }
            
        } catch (Exception e) {
            logger.error("Failed to search Request. ", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    public List<Request> listByManager(String userId) throws Exception {
        List<Request> rtn = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            
            rtn = ((RequestDao) dao).listByManager(session, userId);
            
            // set child
            session = sessionFactory.openSession();
            session.beginTransaction();
            if (rtn != null) {
                for (Request r : rtn) {
                    r.setSendSystem(sysDao.get(session, r.getSendSystemId()));
                    r.setRcvSystem(sysDao.get(session, r.getRcvSystemId()));
                }
            }
            
        } catch (Exception e) {
            logger.error("Failed to search Request. ", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    public List<RequestHistory> getHistroy(String reqId) throws Exception {
        List<RequestHistory> rtn = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            
            rtn = ((RequestDao) dao).getHistory(session, reqId);
            
            BusinessCodeDao bizDao = new BusinessCodeDao();
            session = sessionFactory.openSession();
            session.beginTransaction();
            if (rtn != null) {
                for (Request r : rtn) {
                    r.setSendSystem(sysDao.get(session, r.getSendSystemId()));
                    r.setRcvSystem(sysDao.get(session, r.getRcvSystemId()));
                    
                    r.setSendJobCode(bizDao.get(session, r.getSendJobCodeId()));
                    r.setRcvJobCode(bizDao.get(session, r.getRcvJobCodeId()));
                }
            }
            
        } catch (Exception e) {
            logger.error("Failed to get history. " + reqId, e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    public int changeAdmin(List<Request> requests) throws Exception {
        int count = 0;
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            Date now = new Date();
            for (Request r : requests) {
                r.setChgDate(now);
                dao.save(session, r);
                count++;
            }
            
            tx.commit();
        } catch (Exception e) {
            logger.error("Failed to change Admin", e);
            if (tx != null) try { tx.rollback(); } catch (Exception ignore) { }
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return count;
    }

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

    public String requestFileUpload(MultipartFile item) throws Exception {
        String fileName = item.getOriginalFilename();
        
        int i = fileName.lastIndexOf('.');
        String extension = "";
        if (i > 0) {
            extension = fileName.substring(i);
        }
        String upFileName = null;
        
        synchronized (_lock) {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
            upFileName = format.format(new Date()) + "_" + _seq++ + extension;
            if (_seq > 999) {
                _seq = 1;
            }
        }
        
        File outFile = new File(URMProperties.get("file.repository"), upFileName);
        logger.debug("### file path : " + outFile.getAbsolutePath());
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(outFile);
            bos = new BufferedOutputStream(fos);
            bos.write(item.getBytes());
            bos.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (fos != null) try { fos.close(); } catch (Exception ignore) {}
            if (bos != null) try { bos.close(); } catch (Exception ignore) {}
        }
        
        return upFileName;
    }

    public void requestFileDownload(HttpServletRequest req, HttpServletResponse res) throws Exception {
        OutputStream out = res.getOutputStream();
        FileInputStream fis = null;
        try {
            String fileName = req.getParameter("fileName");

            if (fileName == null || fileName.length() == 0) {
                throw new IllegalArgumentException("Exception:fileName is null or fileName size is 0.");
            }
            
            File outFile = new File(URMProperties.get("file.repository"), fileName);
            
            res.setContentType("application/octet-stream");
            res.setHeader("Content-disposition", "attachment;filename=" + fileName);
            
            fis = new FileInputStream(outFile);
            byte[] readBuffer = new byte[1024];
            while (fis.read(readBuffer, 0, readBuffer.length) != -1) {
                out.write(readBuffer);
            }
        } catch (Exception e) {
            logger.error("", e);
            throw e;
        } finally {
            if (fis != null) try { fis.close(); } catch (Exception ignore) {}
            out.flush();
        }
    }

    public void parseRequestExcel(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Session session = null;
        Workbook wb = null;
        try {
            String requestId = req.getParameter("requestId");
            
            if (requestId == null || requestId.length() == 0) {
                throw new IllegalArgumentException("Exception:requestId is null or requestId size is 0.");
            }

            String fileName = "request_define_" + requestId + ".xls";
            session = sessionFactory.openSession();
            Request vo = dao.get(session, requestId);
            
            wb = new HSSFWorkbook();
            makeRequestSheet(wb, vo);
            
            wb.write(res.getOutputStream());
            
            res.setContentType("application/octet-stream");
            res.setHeader("Content-disposition", "attachment;filename=" + fileName);
        } catch (Exception e) {
            logger.error("", e);
            throw e;
        } finally {
            if (wb != null) {
                wb.close();
            }
            if (session != null) {
                try { session.close(); } catch (Exception ignore) { }
            }
        }
    }
    
    private void makeRequestSheet(Workbook workbook, Request info) throws Exception {
        
        Sheet dataSheet = workbook.createSheet("요건정보");

        CellStyle headerFormat = workbook.createCellStyle();
        CellStyle dataFormat = workbook.createCellStyle();

        headerFormat.setAlignment(HorizontalAlignment.CENTER);
        headerFormat.setVerticalAlignment(VerticalAlignment.CENTER);
        headerFormat.setBorderBottom(BorderStyle.THIN);
        headerFormat.setBorderLeft(BorderStyle.THIN);
        headerFormat.setBorderRight(BorderStyle.THIN);
        headerFormat.setBorderTop(BorderStyle.THIN);
        headerFormat.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerFormat.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        dataFormat.setAlignment(HorizontalAlignment.LEFT);
        dataFormat.setVerticalAlignment(VerticalAlignment.CENTER);
        dataFormat.setBorderBottom(BorderStyle.THIN);
        dataFormat.setBorderLeft(BorderStyle.THIN);
        dataFormat.setBorderRight(BorderStyle.THIN);
        dataFormat.setBorderTop(BorderStyle.THIN);

        for (int i = 0; i < 6; i++) {
            dataSheet.setColumnWidth(i, 3860);
        }

        dataSheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 3));
        Row row = dataSheet.createRow(2);
        Cell cell = row.createCell(0);
        cell.setCellValue("요건 ID");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(1);
        cell.setCellValue(info.getId());
        cell.setCellStyle(dataFormat);
        cell = row.createCell(2);
        cell.setCellStyle(dataFormat);
        cell = row.createCell(3);
        cell.setCellStyle(dataFormat);

        dataSheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 3));
        row = dataSheet.createRow(3);
        cell = row.createCell(0);
        cell.setCellValue("요건 명");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(1);
        cell.setCellValue(info.getName());
        cell.setCellStyle(dataFormat);
        cell = row.createCell(2);
        cell.setCellStyle(dataFormat);
        cell = row.createCell(3);
        cell.setCellStyle(dataFormat);

        dataSheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 3));
        row = dataSheet.createRow(4);
        cell = row.createCell(0);
        cell.setCellValue("업무구분");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(1);
        cell.setCellValue(info.getJobType());
        cell.setCellStyle(dataFormat);
        cell = row.createCell(2);
        cell.setCellStyle(dataFormat);
        cell = row.createCell(3);
        cell.setCellStyle(dataFormat);

        dataSheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 3));
        row = dataSheet.createRow(5);
        cell = row.createCell(0);
        cell.setCellValue("INTERFACE ID");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(1);
        cell.setCellValue(info.getInterfaceId());
        cell.setCellStyle(dataFormat);
        cell = row.createCell(2);
        cell.setCellStyle(dataFormat);
        cell = row.createCell(3);
        cell.setCellStyle(dataFormat);

        row = dataSheet.createRow(7);
        cell = row.createCell(0);
        cell.setCellValue("유형");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(1);
        cell.setCellValue("Sync 타입");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(2);
        cell.setCellValue("송신 방식");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(3);
        cell.setCellValue("수신 방식");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(4);
        cell.setCellValue("2PC 여부");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(5);
        cell.setCellValue("전송 유형");
        cell.setCellStyle(headerFormat);
        
        // TODO get code name
        row = dataSheet.createRow(8);
        cell = row.createCell(0);
        cell.setCellValue(info.getInterfaceType());
        cell.setCellStyle(dataFormat);
        cell = row.createCell(1);
        cell.setCellValue(info.getSyncType());
        cell.setCellStyle(dataFormat);
        cell = row.createCell(2);
        cell.setCellValue(info.getSendSystemType());
        cell.setCellStyle(dataFormat);
        cell = row.createCell(3);
        cell.setCellValue(info.getRcvSystemType());
        cell.setCellStyle(dataFormat);
        cell = row.createCell(4);
        cell.setCellValue(getBoolString(info.isTpcYN()));
        cell.setCellStyle(dataFormat);
        cell = row.createCell(5);
        cell.setCellValue(info.getTrType());
        cell.setCellStyle(dataFormat);

        row = dataSheet.createRow(10);
        cell = row.createCell(0);
        cell.setCellValue("요건등록자");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(1);
        cell.setCellValue("송신담당자");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(2);
        cell.setCellValue("수신담당자");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(3);
        cell.setCellValue("송신시스템");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(4);
        cell.setCellValue("수신시스템");
        cell.setCellStyle(headerFormat);
        
        row = dataSheet.createRow(11);
        cell = row.createCell(0);
        cell.setCellValue(info.getRegId());
        cell.setCellStyle(dataFormat);
        cell = row.createCell(1);
        cell.setCellValue(info.getSendAdmin() != null ? info.getSendAdmin().getName() : info.getSendAdminId());
        cell.setCellStyle(dataFormat);
        cell = row.createCell(2);
        cell.setCellValue(info.getRcvAdmin() != null ? info.getRcvAdmin().getName() : info.getRcvAdminId());
        cell.setCellStyle(dataFormat);
        cell = row.createCell(3);
        cell.setCellValue(info.getSendSystem() != null ? info.getSendSystem().getName() : info.getSendSystemId());
        cell.setCellStyle(dataFormat);
        cell = row.createCell(4);
        cell.setCellValue(info.getRcvSystem() != null ? info.getRcvSystem().getName() : info.getRcvSystemId());
        cell.setCellStyle(dataFormat);
    }
    
    @Override
    protected int getInfluence(String id) throws Exception {
        return 0;
    }

    private String getBoolString(boolean data) {
        String rtnValue = data ? "Y" : "N";
        return rtnValue;
    }
}
