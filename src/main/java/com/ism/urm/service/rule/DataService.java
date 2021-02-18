package com.ism.urm.service.rule;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.springframework.web.multipart.MultipartFile;

import com.ism.urm.dao.rule.AppSystemDao;
import com.ism.urm.dao.rule.data.DataDao;
import com.ism.urm.dao.rule.data.FieldDao;
import com.ism.urm.util.SessionUtil;
import com.ism.urm.vo.rule.data.Data;
import com.ism.urm.vo.rule.data.Field;
import com.ism.urm.vo.rule.data.UsedData;
import com.ism.urm.vo.rule.system.AppSystem;


public class DataService extends RuleService<Data> {

    public static final String ORACLE = "1";
    public static final String SYBASE = "2";
    public static final String DB2 = "3";
    public static final String SQLSERVER = "4";
    public static final String INFORMIX = "5";
    public static final String ETC = "9";

    private AppSystemDao sysDao;

    public DataService() {
        super();
        dao = new DataDao();
        sysDao = new AppSystemDao();
    }

    public List<UsedData> getUsedList(String id) throws Exception {
        if(id == null || id.length() == 0) {
            throw new Exception("Exception: id is null or id length is 0.");
        }
        List<UsedData> rtn = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            rtn = ((DataDao) dao).usedList(session, id);
        } catch (Exception e) {
            logger.error("Failed to get Used data", e);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }

    public List<Field> getFields(String dataId) throws Exception {
        List<Field> rtn = null;
        Session session = null;
        try {
            FieldDao fieldDao = new FieldDao();
            session = sessionFactory.openSession();
            session.beginTransaction();
            
            rtn = fieldDao.listByDataId(session, dataId);
        } catch (Exception e) {
            logger.error("Failed to get fields from " + dataId);
            throw e;
        } finally {
            if (session != null) try { session.close(); } catch (Exception ignore) { }
        }
        return rtn;
    }
    
    public List<Field> parseExcel(String sheetName, MultipartFile item) throws Exception {
        List<Field> list = new ArrayList<>();
        String fileName = item.getOriginalFilename();

        String fileEncoding = System.getProperty("file.encoding");
        String localFileName = new String(fileName.getBytes(fileEncoding));
        logger.info("FILE ENCODING :" + fileEncoding + ", Local file name :" + localFileName);
        
//        File outFile = new File(System.getProperty("urm.file.repository"), localFileName);
        logger.info("document encryt check!!!" );
        // 암호화된 파일인지 확인한다.
        try {
            logger.info("document encryt check start.." );
//            WorkPackager packager = new WorkPackager();
//            logger.info("document encryt packager create." );
//            int res = packager.GetFileType(System.getProperty("urm.file.repository") + "/" + localFileName);
//            boolean encfile = false;
//            logger.info(" packager file type : " + res);
//            switch (res) {
//            case 20:
//                logger.info(" 20 : packager file : 파일을 찾을 수 없습니다." );
//                encfile = false;
//                break;
//            case 21:
//                logger.info(" 21 : packager file : 파일 크기가 0 입니다."  );
//                encfile = false;
//                break;
//            case 22:
//                logger.info(" 22 : packager file : 파일을 읽을 수 없습니다."  );
//                encfile = false;
//                break;
//            case 29:
//                logger.info(" 29 : packager file : 암호화 파일이 아닙니다."  );
//                encfile = false;
//                break;
//            default:
//                logger.info("암호화 된 파일일 꺼임.");
//                encfile = true;
//                break;
//            }
//            if (encfile) {
//                logger.info(" --> packager file." );
//                String docHome = System.getProperty("document.enc.home");
//                String docKey = System.getProperty("document.enc.key");
//                
//                logger.info("doc Home : " + docHome);
//                logger.info("doc key  : " + docKey);
//                
//                if (packager.DoExtract(docHome, docKey, System.getProperty("urm.file.repository") + "/" + localFileName,System.getProperty("urm.file.repository") + "/ENC_" + localFileName)) {
//                    logger.info("DoExtract success");
//                    outFile = new File(System.getProperty("urm.file.repository"), "ENC_" + localFileName);
//                }
//                else {
//                    logger.info("DoExtract fail");
//                }
//            }
//            else {
//                logger.info(" --> packager file not." );
//            }
        } catch (Throwable t) {
            logger.info(t.getMessage(), t);
        }
        
        logger.info("document encryt check end.." );
        Workbook workbook = null;
        try {
            if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(item.getInputStream());
            } else {
                workbook = new XSSFWorkbook(item.getInputStream());
            }
        } catch (Exception e) {
            throw new Exception("Exception : Excel File Read Fail.");
        }

        Sheet sheet = null;
        String faultMessage = "sheet name [" + sheetName + "] load failed.";
        sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            logger.error(faultMessage);
            if (workbook != null) try { workbook.close(); } catch (Exception ignore) { }
            throw new Exception(faultMessage);
        }

        int i=11; // field start index.
        int sno = 1;
        int rsno = 0;
        while(true){
            String krNm  = null;
            String enNm  = null;
            try {
                krNm = (sheet.getRow(i).getCell(2).getStringCellValue()).trim();
                enNm = (sheet.getRow(i).getCell(3).getStringCellValue()).trim();
                if (enNm == null || enNm.trim().length() == 0) {
                    break;
                }
            } catch(Exception e) {
                logger.error("Exception", e);
                break;
            }
            
            Field field = new Field();
            
            String len = (sheet.getRow(i).getCell(4).getStringCellValue()).trim();
            String attr = (sheet.getRow(i).getCell(7).getStringCellValue()).trim();
            
            field.setEngName(enNm);
            field.setName(krNm);
            field.setLength(Integer.parseInt(len));
            field.setType(attr);
            
            boolean isVerification = true;
            if (!attr.equalsIgnoreCase("C") && 
                    !attr.equalsIgnoreCase("N") && 
                    !attr.equalsIgnoreCase("D") && 
                    !attr.equalsIgnoreCase("B")) {
                isVerification = false;
            }

            try {
                if (Integer.parseInt(len) < 0) {
                    throw new Exception("Length is too short.");
                }
            } catch (Exception e) {
                isVerification = false;
            }

            // default value...
            field.setRefInfo("");
            field.setSno(sno + "");
            sno++;
            field.setRsno(rsno + "");
            field.setSubLength(0);
            field.setRepeatCnt(0);
            field.setRepeatYN(false);

            String dFormat = (sheet.getRow(i).getCell(8).getStringCellValue()).trim();
            String nullable = (sheet.getRow(i).getCell(11).getStringCellValue()).trim();
            String keyYN = (sheet.getRow(i).getCell(12).getStringCellValue()).trim();
            String sqlYN = (sheet.getRow(i).getCell(19).getStringCellValue()).trim();
            
            boolean isNull = (nullable.length() == 0) || !(nullable.equalsIgnoreCase("N"))?  true : false;
            boolean isKey = (keyYN.length() == 0) || !(keyYN.equalsIgnoreCase("Y"))? false : true;
            boolean isSql = (sqlYN.length() == 0) || !(sqlYN.equalsIgnoreCase("Y"))? false : true;
            
            if (field.getType().equals("D")) {
                if (dFormat != null && dFormat.length() != 0) {
                    field.setDateFormat(dFormat);
                }
                else {
                    field.setDateFormat("yyyy-MM-dd HH:mm:ss");
                }
                field.setLength(50); // Date Type 은 길이 50 으로 고정한다.
            }
            field.setNullable(isNull);
            field.setKeyYN(isKey);
            field.setSqlYN(isSql);  
            
            logger.debug(field.toString());
            
            list.add(field);
            if (isVerification == false) {
                break;
            }
            
            i++;
        }
        
        if (workbook != null) try { workbook.close(); } catch (Exception ignore) { }
        
        return list;
    }
    
    public List<Field> getTableInfo(int type, String systemId, String query) throws Exception {
        List<Field> rtn = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            String catalog = null;
            String owner = null;
            String tablename = null;
            switch (type) {
            case 0:
                if (query == null || query.trim().length() == 0) {
                    throw new IllegalStateException("테이블 명을 입력해주세요");
                }
                else {
                    tablename = query.trim();
                }
                
                if (tablename.contains(" ") || tablename.contains("\t")) {
                    throw new IllegalStateException("테이블 명만 입력해주세요");
                }
                else if ( tablename.trim().contains(".")) {
                    int lastIndex = tablename.lastIndexOf(".");
                    owner = tablename.substring(0, lastIndex).trim();
                    tablename = tablename.substring(lastIndex + 1).trim();
                    if (owner.contains(".")) {
                        lastIndex = owner.lastIndexOf(".");
                        catalog = owner.substring(0, lastIndex).trim();
                        owner = owner.substring(lastIndex + 1).trim();
                        
                    }
                }
                if (owner == null || owner.trim().length() == 0) {
                    AppSystem sys = sysDao.get(session, systemId);
                    owner = sys.getUserId();
                }

                rtn = getTableInfo(systemId, catalog, owner, tablename, false);
                break;
            case 1:
                if (query == null || query.trim().length() == 0) {
                    throw new IllegalStateException("SELECT 문을  입력해주세요");
                }
                if (!query.trim().toUpperCase().startsWith("SELECT")) {
                    throw new IllegalStateException("SELECT 문만 허용합니다.");
                }
                
                rtn = getTableInfo(systemId, query.trim());
                break;
            default:
                break;
            }
        } catch (Exception e) {
            logger.error("Failed to get Fields form Query.", e);
        } finally {
            if (session != null) session.close();
        }
        return rtn;
    }

    private List<Field> getTableInfo(String systemId, String catalog, String owner, String tablename, boolean isDboRetry) throws Exception {
        Connection conn = null;
        DatabaseMetaData dbmd = null;
        List<Field> list = new ArrayList<>();
        AppSystem sys = null;

        Session session = null;
        try {
            session = sessionFactory.openSession();
            sys = sysDao.get(session, systemId);
            
            conn = getDBConnection(sys);
        } catch (Exception e) {
            throw e;
        } finally {
            if (session != null) session.close();
        }
        
        try {
            dbmd = conn.getMetaData();
            
            ResultSet rsKeys = dbmd.getPrimaryKeys(catalog, owner, tablename);
            List<String> primaryKeyList = new ArrayList<String>();
            while (rsKeys.next()) {
                primaryKeyList.add(rsKeys.getString("COLUMN_NAME"));
            }
            if (rsKeys != null) {
                rsKeys.close();
            }
            ResultSet rsColumns = dbmd.getColumns(catalog, owner, tablename, null);
            
            int sno = 1;
            int rsno = 0;
            boolean isRead = false;
            while (rsColumns.next()) {
                isRead = true;
                String column = rsColumns.getString("COLUMN_NAME");
                String type = rsColumns.getString("TYPE_NAME");
                int iType = rsColumns.getInt("DATA_TYPE");
                int length = rsColumns.getInt("COLUMN_SIZE");
                boolean nullable = (rsColumns.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
                boolean isKey = false;
                for(int i=0; i<primaryKeyList.size(); i++) {
                    if (column.equalsIgnoreCase((String)primaryKeyList.get(i))) {
                        isKey = true;
                    }
                }
                logger.info("column: " + column + " type:" + type + "(" + iType + ")" + " length:" + length + " nullable:" + nullable + " iskey:" + isKey);

                Field field = new Field();
                field.setEngName(column);
                field.setName(column);
                field.setLength(length);
                
                String attr = "C";
                switch (iType) {
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER: 
                case Types.BIGINT:  
                case Types.FLOAT:   
                case Types.REAL:    
                case Types.DOUBLE:  
                case Types.NUMERIC: 
                case Types.DECIMAL: 
                    attr = "N";
                    break;
                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                    attr = "D";
                    field.setLength(50);
                    field.setDateFormat("yyyy-MM-dd HH:mm:ss");
                    break;
                default:
                    attr = "C";
                    break;
                }
                field.setType(attr);

                // default value...
                field.setRefInfo("");
                field.setSno(sno + "");
                sno++;
                field.setRsno(rsno + "");
                field.setSubLength(0);
                field.setRepeatCnt(0);
                field.setRepeatYN(false);
                field.setNullable(nullable);
                field.setKeyYN(isKey);
                field.setSqlYN(false);
                list.add(field);
            }
            if (rsColumns != null) {
                rsColumns.close();
            }
            
            if (!isRead) {
                if (owner != null && owner.length() != 0) {
                    return getTableInfo(systemId, null, null, tablename, true);
                }
                
                String userid = sys.getUserId();
                throw new IllegalStateException("EAI 접속 계정(" + userid + ") 에 대하여 select 권한 또는 존재여부를 확인해 주세요." );
            }
        } catch (SQLException sqle) {
            logger.error("SQLException : ErrorCode - " + sqle.getErrorCode() + "   " + sqle.getMessage() + "  isDBOretry:" + isDboRetry);
            if (sys.getDbType().equals(SYBASE) ) {
                if (!isDboRetry) {
                    try {
                        List<Field> list2 = getTableInfo(systemId, null, "dbo", tablename, true);
                        return list2;
                    } catch (SQLException sqle2) {
                        List<Field> list2 = getTableInfo(systemId, null, null, tablename, true);
                        return list2;
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        throw e;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            dbmd = null;
            if (conn != null) {
                try { conn.close();} catch(Exception e) {conn = null;}
            }
        }
        
        if (list.size() == 0) {
            throw new IllegalStateException("EAI 접속 계정(" + sys.getUserId() + ") 에 대하여 select 권한 또는 존재여부를 확인해 주세요." );
        }
        return list;
    }
    
    private List<Field> getTableInfo(String systemId, String query) throws Exception {
        if (!query.trim().toUpperCase().startsWith("SELECT")) {
            throw new IllegalStateException("SELECT 문만 허용합니다.");
        }
        if (query.trim().endsWith(";")) {
            query = query.substring(0, query.length() -1);
        }
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Field> list = new ArrayList<>();
        try {
            AppSystem sys = null;
            Session session = null;
            try {
                session = sessionFactory.openSession();
                sys = sysDao.get(session, systemId);
                
                conn = getDBConnection(sys);
            } catch (Exception e) {
                throw e;
            } finally {
                if (session != null) session.close();
            }
            
            stmt = conn.createStatement();
            stmt.setFetchSize(1);
            rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            
            int count = rsmd.getColumnCount();
            int i = 0;
            
            int sno = 1;
            int rsno = 0;
            
            while (++i <= count) {
                String column = rsmd.getColumnName(i);
                String type = rsmd.getColumnTypeName(i);
                int iType = rsmd.getColumnType(i);
                int length = rsmd.getColumnDisplaySize(i);
                boolean nullable = false;
                boolean isKey = false;
                logger.info("column: " + column + " type:" + type + "(" + iType + ") length:" + length + " nullable:" + nullable + " iskey:" + isKey);
                
                Field field = new Field();
                field.setEngName(column);
                field.setName(column);
                field.setLength(length);
                
                String attr = "C";
                switch (iType) {
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER: 
                case Types.BIGINT:  
                case Types.FLOAT:   
                case Types.REAL:    
                case Types.DOUBLE:  
                case Types.NUMERIC: 
                case Types.DECIMAL: 
                    attr = "N";
                    break;
                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                    attr = "D";
                    field.setLength(50);
                    field.setDateFormat("yyyy-MM-dd HH:mm:ss");
                    break;
                default:
                    attr = "C";
                    break;
                }
                field.setType(attr);

                // default value...
                field.setRefInfo("");
                field.setSno(sno + "");
                sno++;
                field.setRsno(rsno + "");
                field.setSubLength(0);
                field.setRepeatCnt(0);
                field.setRepeatYN(false);
                field.setNullable(nullable);
                field.setKeyYN(isKey);
                field.setSqlYN(false);
                list.add(field);
            }
                
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        } finally {
            if (rs != null) {
                try { rs.close();} catch(Exception e) {}
            }
            if (stmt != null) {
                try { stmt.close();} catch(Exception e) {}
            }
            if (conn != null) {
                try { conn.close();} catch(Exception e) {}
            }
        }
        
        return list;
    }

    private Connection getDBConnection(AppSystem sys) throws Exception {
        String connurl = getDBConnectionString(sys);
        
        String userid = sys.getUserId();
        String passwd = SessionUtil.getDecString(sys.getUserPasswd());
        logger.info("connection try : " + connurl + "   [" + userid + "][" + passwd + "]");
        if (sys.getDbType().equals(ORACLE)) {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        }
        else if (sys.getDbType().equals(SYBASE)) {
            Class.forName("com.sybase.jdbc4.jdbc.SybDriver").newInstance();
        }
        else if (sys.getDbType().equals(SQLSERVER)) {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
        }
        else {
            throw new IllegalStateException("허용하지 않는 DB 타입입니다.");
        }
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connurl, userid, passwd);
        } catch (Exception e) {
            logger.error("접속에 실패하였습니다.", e);
            throw e;
        }
        return conn;
    }

    private String getDBConnectionString(AppSystem sys) {
        if (sys == null) {
            throw new IllegalStateException("System 이 없습니다.");
        }
        String connString = null;
        String type = sys.getDbType();
        String ip = sys.getIp();
        String port = sys.getPort();
        String dbname = sys.getDbName();
        if (type == null) {
            throw new IllegalStateException("DB 타입의 시스템만 허용합니다.");
        }
        if (type.equals(ORACLE)) {
            connString = "jdbc:oracle:thin:@(description=(load_balance=off)(failover=on)(address=(protocol=tcp)(host=" + ip + ")(port=" + port + "))(connect_data=(service_name=" + dbname + ")))";
        }
        else if (type.equals(SYBASE)) {
            connString = "jdbc:sybase:Tds:" + ip + ":" + port + "/" + dbname;
        }
        else if (type.equals(SQLSERVER)) {
            connString = "jdbc:sqlserver://" + ip + ":" + port + ";databasename=" + dbname;
        }
        else if (type.equals(DB2)) {
            connString = "jdbc:db2://" + ip + ":" + port + "/" + dbname;
        }
        else {
            throw new IllegalStateException("허용하지 않는 DB 타입입니다.");
        }
        logger.info("DB Connection String : " + connString);
        return connString;
    }
    
    public void parseDataFieldExcel(HttpServletRequest req, HttpServletResponse res) throws Exception {
        Session session = null;
        Workbook wb = null;
        try {
            String dataId = req.getParameter("dataId");
            
            if (dataId == null || dataId.length() == 0) {
                throw new IllegalArgumentException("Exception:dataId is null or dataId size is 0.");
            }

            String fileName = "data_define_" + dataId + ".xls";
            session = sessionFactory.openSession();
            Data data = dao.get(session, dataId);
            
            wb = new HSSFWorkbook();
            makeDataSheet(wb, data, "데이터");
            
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
    
    public void makeDataSheet(Workbook workbook, Data info, String sheetName) throws Exception {
        
        Sheet dataSheet = workbook.createSheet(sheetName);

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

        for (int i = 0; i < 24; i++) {
            dataSheet.setColumnWidth(i, 3600);
        }

        dataSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
        Row row = dataSheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("데이터정의");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(1);
        cell.setCellStyle(headerFormat);
        cell = row.createCell(2);
        cell.setCellStyle(headerFormat);

        dataSheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6));
        cell = row.createCell(4);
        cell.setCellValue("작성자정보");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(5);
        cell.setCellStyle(headerFormat);
        cell = row.createCell(6);
        cell.setCellStyle(headerFormat);

        dataSheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 2));
        dataSheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 6));
        row = dataSheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("데이터정의ID");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(1);
        cell.setCellValue(info.getId());
        cell.setCellStyle(dataFormat);
        cell = row.createCell(2);
        cell.setCellStyle(dataFormat);

        cell = row.createCell(4);
        cell.setCellValue("작성자");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(5);
        cell.setCellValue(info.getChgId());
        cell.setCellStyle(dataFormat);
        cell = row.createCell(6);
        cell.setCellStyle(dataFormat);

        dataSheet.addMergedRegion(new CellRangeAddress(2, 2, 1, 2));
        dataSheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 6));
        row = dataSheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("데이터명");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(1);
        cell.setCellValue(info.getName());
        cell.setCellStyle(dataFormat);
        cell = row.createCell(2);
        cell.setCellStyle(dataFormat);

        cell = row.createCell(4);
        cell.setCellValue("소속");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(5);
        cell.setCellValue("");
        cell.setCellStyle(dataFormat);
        cell = row.createCell(6);
        cell.setCellStyle(dataFormat);

        dataSheet.addMergedRegion(new CellRangeAddress(3, 3, 1, 2));
        dataSheet.addMergedRegion(new CellRangeAddress(3, 3, 5, 6));
        row = dataSheet.createRow(3);
        cell = row.createCell(0);
        cell.setCellValue("처리순서");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(1);
        cell.setCellValue(info.getInOutGbn()); // to string
        cell.setCellStyle(dataFormat);
        cell = row.createCell(2);
        cell.setCellStyle(dataFormat);

        cell = row.createCell(4);
        cell.setCellValue("전화");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(5);
        cell.setCellValue("");
        cell.setCellStyle(dataFormat);
        cell = row.createCell(6);
        cell.setCellStyle(dataFormat);

        dataSheet.addMergedRegion(new CellRangeAddress(4, 4, 1, 2));
        dataSheet.addMergedRegion(new CellRangeAddress(4, 4, 5, 6));
        row = dataSheet.createRow(4);
        cell = row.createCell(0);
        cell.setCellValue("데이터유형");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(1);
        cell.setCellValue(info.getType()); // to string
        cell.setCellStyle(dataFormat);
        cell = row.createCell(2);
        cell.setCellStyle(dataFormat);

        cell = row.createCell(4);
        cell.setCellValue("핸드폰");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(5);
        cell.setCellValue("");
        cell.setCellStyle(dataFormat);
        cell = row.createCell(6);
        cell.setCellStyle(dataFormat);

        dataSheet.addMergedRegion(new CellRangeAddress(5, 5, 1, 2));
        row = dataSheet.createRow(5);
        cell = row.createCell(0);
        cell.setCellValue("레코드구분자");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(1);
        cell.setCellValue("");
        cell.setCellStyle(dataFormat);
        cell = row.createCell(2);
        cell.setCellStyle(dataFormat);

        dataSheet.addMergedRegion(new CellRangeAddress(6, 6, 1, 2));
        row = dataSheet.createRow(6);
        cell = row.createCell(0);
        cell.setCellValue("컬럼구분자");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(1);
        cell.setCellValue("");
        cell.setCellStyle(dataFormat);
        cell = row.createCell(2);
        cell.setCellStyle(dataFormat);

        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 0, 0));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 1, 1));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 2, 2));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 3, 3));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 4, 4));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 5, 5));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 6, 6));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 7, 7));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 8, 8));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 9, 9));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 10, 10));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 11, 11));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 12, 12));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 13, 13));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 14, 14));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 9, 15, 17));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 18, 18));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 19, 19));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 10, 20, 20));
        dataSheet.addMergedRegion(new CellRangeAddress(9, 9, 22, 23));
        row = dataSheet.createRow(9);
        cell = row.createCell(0);
        cell.setCellValue("구분");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(1);
        cell.setCellValue("필드ID");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(2);
        cell.setCellValue("한글필드명");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(3);
        cell.setCellValue("영문필드명");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(4);
        cell.setCellValue("길이");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(5);
        cell.setCellValue("필드위치");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(6);
        cell.setCellValue("길이필드타입");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(7);
        cell.setCellValue("필드유형");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(8);
        cell.setCellValue("필드포멧");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(9);
        cell.setCellValue("필드검증유형");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(10);
        cell.setCellValue("필드검증값");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(11);
        cell.setCellValue("널가능");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(12);
        cell.setCellValue("키값여부");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(13);
        cell.setCellValue("채움문자");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(14);
        cell.setCellValue("정렬타입");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(15);
        cell.setCellValue("가변길이정보");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(16);
        cell.setCellStyle(headerFormat);
        cell = row.createCell(17);
        cell.setCellStyle(headerFormat);

        cell = row.createCell(18);
        cell.setCellValue("차이값");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(19);
        cell.setCellValue("SQL함수여부");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(20);
        cell.setCellValue("반복여부");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(21);
        cell.setCellValue("반복횟수");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(22);
        cell.setCellValue("반복횟수(지정필드시)");
        cell.setCellStyle(headerFormat);
        cell = row.createCell(23);
        cell.setCellStyle(headerFormat);

        row = dataSheet.createRow(10);
        for (int i = 0; i < 15; i++) {
            row.createCell(i).setCellStyle(headerFormat);
        }
        cell = row.createCell(15);
        cell.setCellValue("마스터위치");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(16);
        cell.setCellValue("디테일위치");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(17);
        cell.setCellValue("필드위치");
        cell.setCellStyle(headerFormat);
        for (int i = 18; i < 21; i++) {
            row.createCell(i).setCellStyle(headerFormat);
        }
        cell = row.createCell(21);
        cell.setCellValue("지정된반복횟수");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(22);
        cell.setCellValue("마스터위치");
        cell.setCellStyle(headerFormat);

        cell = row.createCell(23);
        cell.setCellValue("필드위치");
        cell.setCellStyle(headerFormat);
        
        // 만약 필드가 등록되어 있지 않다면..
        if (info.getFields() == null || info.getFields().size() == 0) {
            return;
        }
        
        int i = 11;
        int mmIndex = 1;
        int mdIndex = 1;
        int dmIndex = 0; // master -> detail 로 넘어갈 경우 ++ 해주기 때문에 0으로..
        int ddIndex = 1;
        boolean isMaster = true;
        for (Field field : info.getFields()) {
            if (field.getType().equalsIgnoreCase("G")) {
                continue;
            }
            row = dataSheet.createRow(i);

            if (!field.isRepeatYN()) {
                if (!isMaster) {
                    mmIndex++;
                    mdIndex = 1;
                }
                cell = row.createCell(0);
                cell.setCellValue("M" + mmIndex);
                cell.setCellStyle(dataFormat);
                
                cell = row.createCell(1);
                cell.setCellValue("M" + getNumString(mdIndex, 3));
                cell.setCellStyle(dataFormat);
                mdIndex++;
                isMaster = true;
            } else {
                if (isMaster) {
                    dmIndex++;
                    ddIndex = 1;
                }
                cell = row.createCell(0);
                cell.setCellValue("D" + dmIndex);
                cell.setCellStyle(dataFormat);
                
                cell = row.createCell(1);
                cell.setCellValue("D" + getNumString(ddIndex, 3));
                cell.setCellStyle(dataFormat);
                ddIndex++;
                isMaster = false;
            }
            cell = row.createCell(2);
            cell.setCellValue(field.getEngName());
            cell.setCellStyle(dataFormat);
            
            cell = row.createCell(3);
            cell.setCellValue(field.getName());
            cell.setCellStyle(dataFormat);
            
            cell = row.createCell(4);
            cell.setCellValue("" + field.getLength());
            cell.setCellStyle(dataFormat);
            cell = row.createCell(5);
            cell.setCellValue("");
            cell.setCellStyle(dataFormat);
            cell = row.createCell(6);
            cell.setCellValue("");
            cell.setCellStyle(dataFormat);
            cell = row.createCell(7);
            cell.setCellValue(field.getType());
            cell.setCellStyle(dataFormat);
            cell = row.createCell(8);
            cell.setCellValue(field.getDateFormat());
            cell.setCellStyle(dataFormat);
            cell = row.createCell(9);
            cell.setCellValue("");
            cell.setCellStyle(dataFormat);
            cell = row.createCell(10);
            cell.setCellValue("");
            cell.setCellStyle(dataFormat);
            cell = row.createCell(11);
            cell.setCellValue(getBoolString(field.isNullable()));
            cell.setCellStyle(dataFormat);
            cell = row.createCell(12);
            cell.setCellValue(getBoolString(field.isKeyYN()));
            cell.setCellStyle(dataFormat);

            if (field.getType().equalsIgnoreCase("N")) {
                cell = row.createCell(13);
                cell.setCellValue("0");
                cell.setCellStyle(dataFormat);
                cell = row.createCell(14);
                cell.setCellValue("R");
                cell.setCellStyle(dataFormat);
            } else {
                cell = row.createCell(13);
                cell.setCellValue("");
                cell.setCellStyle(dataFormat);
                cell = row.createCell(14);
                cell.setCellValue("L");
                cell.setCellStyle(dataFormat);
            }

            cell = row.createCell(15);
            cell.setCellValue("");
            cell.setCellStyle(dataFormat);
            cell = row.createCell(16);
            cell.setCellValue("");
            cell.setCellStyle(dataFormat);
            cell = row.createCell(17);
            cell.setCellValue("");
            cell.setCellStyle(dataFormat);

            cell = row.createCell(18);
            cell.setCellValue("");
            cell.setCellStyle(dataFormat);
            cell = row.createCell(19);
            cell.setCellValue(getBoolString(field.isSqlYN()));
            cell.setCellStyle(dataFormat);
            cell = row.createCell(20);
            cell.setCellValue(getBoolString(field.isRepeatYN()));
            cell.setCellStyle(dataFormat);

            cell = row.createCell(21);
            cell.setCellValue("");
            cell.setCellStyle(dataFormat);

            cell = row.createCell(22);
            cell.setCellValue("");
            cell.setCellStyle(dataFormat);
            cell = row.createCell(23);
            cell.setCellValue("");
            cell.setCellStyle(dataFormat);

            i++;;
        }
    }
    
    @Override
    protected int getInfluence(String id) throws Exception {
        return getUsedList(id).size();
    }

    private String getNumString(int data, int len) {
        String rtnValue = "" + data;
        if(rtnValue.length() > len) {
            return rtnValue;
        }
        for(int i=rtnValue.length(); i<len; i++) {
            rtnValue = "0" + rtnValue;
        }
        return rtnValue;
    }
    
    private String getBoolString(boolean data) {
        String rtnValue = data ? "Y" : "N";
        return rtnValue;
    }
}
