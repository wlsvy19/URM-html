package com.ism.urm.service.manage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ism.urm.dao.manage.CommonCodeDao;
//import com.mocomsys.urm.dto.Request;
//import com.mocomsys.urm.dto.SystemCode;
//import com.mocomsys.urm.exception.NotFoundRequestException;
//import com.mocomsys.util.Accessory;
import com.ism.urm.vo.HiberUtill;
import com.ism.urm.vo.manage.CommonCode;

public class CodeService {

	private Logger logger = LoggerFactory.getLogger("debug");
	private SessionFactory sessionFactory;

	private CommonCodeDao commonCodeDao;
//	BusinessCodeDao businessCodeDao;
	
	static Map<String, List> commonCodeMap = new Hashtable<String, List>();
	static List<CommonCode> commonCodeList = new ArrayList<CommonCode>();
	
//	static List<BusinessCode> businessCodeList = new ArrayList<BusinessCode>();
//
//	static List<AuthCode> authCodeList = new ArrayList<AuthCode>();
	
	public CodeService() {
		super();
		commonCodeDao = new CommonCodeDao();
		sessionFactory = HiberUtill.getSessionFactory();
	}

	static String noti = null;

	/**
	 * @return the businessCodeDao
	 */
//	public BusinessCodeDao getBusinessCodeDao() {
//		return businessCodeDao;
//	}
//
//	/**
//	 * @param businessCodeDao the businessCodeDao to set
//	 */
//	public void setBusinessCodeDao(BusinessCodeDao businessCodeDao) {
//		this.businessCodeDao = businessCodeDao;
//	}
	
	
	public List<CommonCode> getCommonCodeList(String knd) throws Exception {
		logger.info("CommonService.getCommonCodeList(" + knd + ") call.");

//		if(knd == null) throw new NotFoundRequestException("Exception:Argment knd is null");
		
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			if(commonCodeMap == null || commonCodeMap.isEmpty()) {
				////////////////////////////////////////////
				//�����ڵ� ��ü ��ȸ �� �ؽ����̺� ��� Ư�� �����ڵ� 
				//��û�� �ؽÿ��� �������ֵ����� 
				List<String> kndList = commonCodeDao.getKnd(session);
				for (String key : kndList) {
					List<CommonCode> codeList = commonCodeDao.list(session, key);
					commonCodeMap.put(key, codeList);
				}
			}
			return commonCodeMap.get(knd);
		} catch(Exception e) {
			logger.error("", e);
			throw e;
		}
	}
	

	/**
	 * �����ڵ� ��ü ��ȸ 
	 * @return
	 * @throws Exception
	 */
	public List<CommonCode> getCommonCodeList() throws Exception {
		logger.info("CommonService.getCommonCodeList() call.");
		try {
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			if(commonCodeList == null || commonCodeList.isEmpty()) {
				commonCodeList = commonCodeDao.list(session);
			}
			logger.debug("RequestServiceImpl.getCommonCodeList return");
			return commonCodeList;
		} catch(Exception e) {
			logger.error("", e);
			throw e;
		}
	}
	

	/**
	 * ���������ڵ� ����Ʈ ��ȸ
	 * @return
	 * @throws Exception
	 */
//	public List<BusinessCode> getBusinessCodeList() throws Exception {
//		logger.info("CommonService.getBusinessCodeList() call.");
//		try {
//		    logger.debug("CommonService.getBusinessCodeList call");
//			if(businessCodeList == null || businessCodeList.isEmpty()) {
//				businessCodeList = businessCodeDao.list();
//			} 
//			logger.debug("CommonService.getBusinessCodeList return");
//			return businessCodeList;
//		} catch(Exception e) {
//			logger.error("", e);
//			throw e;
//		}
//	}
	
	/**
	 * ���� �ڵ� ����Ʈ ��ȸ
	 * @return
	 * @throws Exception
	 */
//	public List<AuthCode> getAuthCodeList() throws Exception {
//		logger.info("CommonService.getAuthCodeList() call.");
//		try {
//		    logger.debug("CommonService.getAuthCodeList call");
//			if(authCodeList == null || authCodeList.isEmpty()) {
//				authCodeList = commonCodeDao.listAuthCode();
//			} 
//			logger.debug("CommonService.getAuthCodeList return");
//			return authCodeList;
//		} catch(Exception e) {
//			logger.error("", e);
//			throw e;
//		}
//	}
	
	
	/**
	 * ���� �ڵ� ����Ʈ �߰�
	 * @return
	 * @throws Exception
	 */
//	public int addBusinessCodeList(List<BusinessCode> lists) throws Exception {
//		logger.info("CommonService.addBusinessCode() call.");
//		try {
//			int res = 0;
//			for(int i=0; i<lists.size(); i++) {
//				businessCodeDao.insert(lists.get(i));
//			}
//			
//			businessCodeList = businessCodeDao.list();
//			
//			return res;
//		} catch(Exception e) {
//			logger.error("", e);
//			throw e;
//		}
//	}
	
	/**
	 * ���� �ڵ� ����Ʈ �߰�
	 * @return
	 * @throws Exception
	 */
//	public int addBusinessCode(BusinessCode code) throws Exception {
//		logger.info("CommonService.addBusinessCode() call.");
//		try {
//			int res = 0;
//			if(code.getBizId() == null || code.getBizId().length() == 0) {
//				res = businessCodeDao.insert(code);
//			}
//			else {
//				res = businessCodeDao.update(code);
//			}
//			
//			businessCodeList = businessCodeDao.list();
//			
//			return res;
//		} catch(Exception e) {
//			logger.error("", e);
//			throw e;
//		}
//	}
	
	
//	public List<BusinessCode> bizFileUpload(String fileName, String sheetName, byte[] data) throws Exception {
//		logger.info("DataService.bizFileUpload() call.");
//		
//		String updownPath = System.getProperty("urm.file.repository");
//		
//		logger.info("file name:" + fileName + "   sheet name : " + sheetName);
//		
//		String upFileName = null;
//		
//		upFileName = Accessory.getFormatDate("yyyyMMdd_HHmmss") + "_" + fileName;
//		 
//		logger.info("FILE ENCODING :" + System.getProperty("file.encoding"));
//		
//		String utfFileName = new String(upFileName.getBytes(), "UTF-8");
//		logger.info("UTF-8  file name :" + utfFileName);
//		
//		String euckrFileName = new String(upFileName.getBytes("euc-kr")); 
//		logger.info("EUC-KR file name :" + euckrFileName);
//		
//		String localFileName = new String(upFileName.getBytes(System.getProperty("file.encoding"))); 
//		logger.info("Local file name :" + localFileName);
//		
//		String utfSheetName = new String(sheetName.getBytes(), "UTF-8");
//		logger.info("UTF-8  sheet name :" + utfSheetName);
//		
//		String euckrSheetName = new String(sheetName.getBytes("euc-kr")); 
//		logger.info("EUC-KR sheet name :" + euckrSheetName);
//		
//		String localSheetName = new String(sheetName.getBytes(System.getProperty("file.encoding"))); 
//		logger.info("Local sheet name :" + localSheetName);
//		
//		
//		File outFile = new File(updownPath, localFileName);
//		BufferedOutputStream bos = null;
//		
//		try {
//			bos = new BufferedOutputStream(new FileOutputStream(outFile));
//			bos.write(data);
//			bos.flush();
//		} catch(Exception e) {
//			throw e;
//		} finally {
//			try {
//				bos.close();
//			} catch(Exception ee) {}
//			bos = null;
//		}
//		
//		Workbook workbook = null;
//		try {
//			workbook = Workbook.getWorkbook(outFile);
//			if(workbook == null) {
//				logger.info("workbook is null.");
//			}
//			else {
//				logger.info("workbook is not null.");
//			}
//		} catch(Exception e) {
//			throw new Exception("Exception : Excel File Read Fail.");
//		}
//		
//		Sheet sheet = null;
//		String faultMessage = "sheet name [" + sheetName + "] load failed.";
//		sheet = workbook.getSheet(sheetName);
//		if(sheet == null) {
//			logger.fatal(faultMessage);
//			throw new Exception(faultMessage);
//		}
//		
//		List<BusinessCode> list = new ArrayList<BusinessCode>();
//		int i=2;
//		while(true) {
//			try {
//				String bizNm		= (sheet.getCell( 1, i).getContents()).trim();
//				String p1Id			= (sheet.getCell( 2, i).getContents()).trim();
//				String p1Nm			= (sheet.getCell( 3, i).getContents()).trim();
//				String p2Id			= (sheet.getCell( 4, i).getContents()).trim();
//				String p2Nm			= (sheet.getCell( 5, i).getContents()).trim();
//				String p3Id			= (sheet.getCell( 6, i).getContents()).trim();
//				String p3Nm			= (sheet.getCell( 7, i).getContents()).trim();
//				
//				if(bizNm == null || bizNm.trim().length() == 0) {
//					break;
//				}
//				
//				logger.debug(i + "  business Name : " + bizNm);
//	
//				BusinessCode bc = new BusinessCode();
//				bc.setBizNm(bizNm);
//				bc.setPart1Id(p1Id);
//				bc.setPart1Nm(p1Nm);
//				bc.setPart2Id(p2Id);
//				bc.setPart2Nm(p2Nm);
//				bc.setPart3Id(p3Id);
//				bc.setPart3Nm(p3Nm);
//				
//				list.add(bc);
//				
//				i++;
//			} catch (ArrayIndexOutOfBoundsException e) {
//				logger.error("ArrayIndexOutOfBoundsException", e);
//				break;
//			}
//		}
//		workbook.close();
////		debug.info("File Upload Complate... [" + upFileName + "]");
//		return list;
//	}
	
	
//	public List<Request> reqFileUpload(String fileName, String sheetName, byte[] data) throws Exception {
//		logger.info("RequestService.reqFileUpload() call.");
//
//		logger.info("file name:" + fileName + "   sheet name : " + sheetName);
//		
//		String upFileName = null;
//		
//		upFileName = Accessory.getFormatDate("yyyyMMdd_HHmmss") + "_" + fileName;
//		 
//		logger.info("FILE ENCODING :" + System.getProperty("file.encoding"));
//		
//		String utfFileName = new String(upFileName.getBytes(), "UTF-8");
//		logger.info("UTF-8  file name :" + utfFileName);
//		
//		String euckrFileName = new String(upFileName.getBytes("euc-kr")); 
//		logger.info("EUC-KR file name :" + euckrFileName);
//		
//		String localFileName = new String(upFileName.getBytes(System.getProperty("file.encoding"))); 
//		logger.info("Local file name :" + localFileName);
//		
//		String utfSheetName = new String(sheetName.getBytes(), "UTF-8");
//		logger.info("UTF-8  sheet name :" + utfSheetName);
//		
//		String euckrSheetName = new String(sheetName.getBytes("euc-kr")); 
//		logger.info("EUC-KR sheet name :" + euckrSheetName);
//		
//		String localSheetName = new String(sheetName.getBytes(System.getProperty("file.encoding"))); 
//		logger.info("Local sheet name :" + localSheetName);
//		
//		
//		File outFile = new File(System.getProperty("urm.file.repository"), localFileName);
//		BufferedOutputStream bos = null;
//		
//		try {
//			bos = new BufferedOutputStream(new FileOutputStream(outFile));
//			bos.write(data);
//			bos.flush();
//		} catch(Exception e) {
//			throw e;
//		} finally {
//			try {
//				bos.close();
//			} catch(Exception ee) {}
//			bos = null;
//		}
//		
//		Workbook workbook = null;
//		try {
//			workbook = Workbook.getWorkbook(outFile);
//			if(workbook == null) {
//				logger.info("workbook is null.");
//			}
//			else {
//				logger.info("workbook is not null.");
//			}
//		} catch(Exception e) {
//			throw new Exception("Exception : Excel File Read Fail.");
//		}
//		
//		Sheet sheet = null;
//		String faultMessage = "sheet name [" + sheetName + "] load failed.";
//		sheet = workbook.getSheet(sheetName);
//		if(sheet == null) {
//			logger.fatal(faultMessage);
//			throw new Exception(faultMessage);
//		}
//		
//		List<Request> list = new ArrayList<Request>();
//
//		int i=2;
//		while(true) {
//			try {
//				String jobType		= (sheet.getCell( 1, i).getContents()).trim();
//				String reqNm		= (sheet.getCell( 2, i).getContents()).trim();
//				
//				String sndJobCode	= (sheet.getCell( 3, i).getContents()).trim();
//				String sndMethod	= (sheet.getCell( 4, i).getContents()).trim();
//				String sndSystem	= (sheet.getCell( 5, i).getContents()).trim();
//				String rcvJobCode	= (sheet.getCell( 6, i).getContents()).trim();
//				String rcvMethod	= (sheet.getCell( 7, i).getContents()).trim();
//				String rcvSystem	= (sheet.getCell( 8, i).getContents()).trim();
//				
//				String ifType		= (sheet.getCell( 9, i).getContents()).trim();
//				
//				String trType		= (sheet.getCell(10, i).getContents()).trim();
//				
//				String syncType		= (sheet.getCell(11, i).getContents()).trim();
//				String tpcYn		= (sheet.getCell(12, i).getContents()).trim();
//				
//				String reqAdmId		= (sheet.getCell(13, i).getContents()).trim();
//				String sndAdmId		= (sheet.getCell(14, i).getContents()).trim();
//				String rcvAdmId		= (sheet.getCell(15, i).getContents()).trim();
//				String testStartYmd	= "20101001";
//				String testEndYmd	= "20101231";
//				
//				
//				if(reqNm == null || reqNm.length() == 0) {
//					break;
//				}
//				
//				logger.debug(i + "  Request Name : " + reqNm);
//	
//				Request req = new Request();
//				req.setJobType(jobType);
//				req.setReqNm(reqNm);
//				req.setSndJobCodeId(sndJobCode);
//				req.setRcvJobCodeId(rcvJobCode);
//				req.setSndMethod(sndMethod);
//				req.setSndHostId(sndSystem);
//				req.setRcvMethod(rcvMethod);
//				req.setRcvHostId(rcvSystem);
//				req.setIfType(ifType);
//				req.setTrType(trType);
//				req.setSyncType(syncType);
//				req.setTpcYn(tpcYn);
//				req.setSndSvrNm("");
//				req.setRcvSvrNm("");
//				req.setRegId(reqAdmId);
//				req.setChgId(reqAdmId);
//				req.setSndAdmId(sndAdmId);
//				req.setRcvAdmId(rcvAdmId);
//				req.setTestStartYmd(testStartYmd);
//				req.setTestEndYmd(testEndYmd);
//				req.setChgStat("1"); // ��� ��û
//				req.setPrcStat("1"); // ���� ��û
//				req.setEaiYn("Y");   // EAI ����
//				req.setDataMapYn("N"); // Mapping ��뿩��
//				req.setDbCrudType("1"); // DB CRUD
//				req.setFileCrudType("1"); // FILE CRUD
//				req.setOpenYmd(Accessory.getFormatDate("yyyyMMdd"));
//				
//				list.add(req);
//				
//				i++;
//			} catch (ArrayIndexOutOfBoundsException e) {
//				logger.error("ArrayIndexOutOfBoundsException", e);
//				break;
//			}
//		}
//		workbook.close();
////		debug.info("File Upload Complate... [" + upFileName + "]");
//		return list;
//	}
	
	
	
//	public List<SystemCode> sysFileUpload(String fileName, String sheetName, byte[] data) throws Exception {
//		logger.info("RequestService.sysFileUpload() call.");
//
//		logger.info("file name:" + fileName + "   sheet name : " + sheetName);
//		
//		String upFileName = null;
//		
//		upFileName = Accessory.getFormatDate("yyyyMMdd_HHmmss") + "_" + fileName;
//		 
//		logger.info("LOCAL FILE ENCODING :" + System.getProperty("file.encoding"));
//		
//		String utfFileName = new String(upFileName.getBytes(), "UTF-8");
//		logger.info("UTF-8  file name :" + utfFileName);
//		
//		String euckrFileName = new String(upFileName.getBytes("euc-kr")); 
//		logger.info("EUC-KR file name :" + euckrFileName);
//
//		String localFileName = new String(upFileName.getBytes(System.getProperty("file.encoding"))); 
//		logger.info("Local file name :" + localFileName);
//		
//		String utfSheetName = new String(sheetName.getBytes(), "UTF-8");
//		logger.info("UTF-8  sheet name :" + utfSheetName);
//		
//		String euckrSheetName = new String(sheetName.getBytes("euc-kr")); 
//		logger.info("EUC-KR sheet name :" + euckrSheetName);
//				
//		String localSheetName = new String(sheetName.getBytes(System.getProperty("file.encoding"))); 
//		logger.info("Local sheet name :" + localSheetName);
//		
//		
//		File outFile = new File(System.getProperty("urm.file.repository"), localFileName);
//		BufferedOutputStream bos = null;
//		
//		try {
//			bos = new BufferedOutputStream(new FileOutputStream(outFile));
//			bos.write(data);
//			bos.flush();
//		} catch(Exception e) {
//			throw e;
//		} finally {
//			try {
//				bos.close();
//			} catch(Exception ee) {}
//			bos = null;
//		}
//		
//		Workbook workbook = null;
//		try {
//			workbook = Workbook.getWorkbook(outFile);
//			if(workbook == null) {
//				logger.info("workbook is null.");
//			}
//			else {
//				logger.info("workbook is not null.");
//			}
//		} catch(Exception e) {
//			throw new Exception("Exception : Excel File Read Fail.");
//		}
//		
//		Sheet sheet = null;
//		String faultMessage = "sheet name [" + sheetName + "] load failed.";
//		sheet = workbook.getSheet(sheetName);
//		if(sheet == null) {
//			logger.fatal(faultMessage);
//			throw new Exception(faultMessage);
//		}
//		
//		List<SystemCode> list = new ArrayList<SystemCode>();
//
//		int i=12;
//		while(true) {
//			try {
//				String sysNm		= (sheet.getCell( 0, i).getContents()).trim();
//				String sysCd		= (sheet.getCell( 1, i).getContents()).trim();
//				String sysTypeNm	= (sheet.getCell( 2, i).getContents()).trim();
//				String sysType		= (sheet.getCell( 3, i).getContents()).trim();
//				String devTypeNm	= (sheet.getCell( 4, i).getContents()).trim();
//				String devType		= (sheet.getCell( 5, i).getContents()).trim();
//				String host			= (sheet.getCell( 6, i).getContents()).trim();
//				String ip			= (sheet.getCell( 7, i).getContents()).trim();
//				String port			= (sheet.getCell( 8, i).getContents()).trim();
//				String dbName		= (sheet.getCell( 9, i).getContents()).trim();
//				String dbKndNm		= (sheet.getCell(10, i).getContents()).trim();
//				String dbKnd		= (sheet.getCell(11, i).getContents()).trim();
//				String userId		= (sheet.getCell(12, i).getContents()).trim();
//				String passwd		= (sheet.getCell(13, i).getContents()).trim();
//				String jdbcType		= (sheet.getCell(14, i).getContents()).trim();
//				
//				
//				if(sysNm == null || sysNm.length() == 0) {
//					break;
//				}
//				
//				logger.debug(i + "  System Name : " + sysNm);
//	
//				SystemCode sys = new SystemCode();
//				sys.setSysNm(sysNm);
//				sys.setSysCd(sysCd);
//				sys.setSysTypeNm(sysTypeNm);
//				sys.setSysType(sysType);
//				sys.setDevTypeNm(devTypeNm);
//				sys.setDevType(devType);
//				sys.setHostId(host);
//				sys.setIp(ip);
//				sys.setPort(port);
//				sys.setDbNm(dbName);
//				sys.setDbKndNm(dbKndNm);
//				sys.setDbKnd(dbKnd);
//				sys.setUserId(userId);
//				sys.setPassword(passwd);
//				sys.setJdbcType(jdbcType);
//				
//				list.add(sys);
//				
//				i++;
//			} catch (ArrayIndexOutOfBoundsException e) {
//				logger.error("ArrayIndexOutOfBoundsException", e);
//				break;
//			}
//		}
//		workbook.close();
////		debug.info("File Upload Complate... [" + upFileName + "]");
//		return list;
//	}
	
	public void setNoti(String noti) {
		CodeService.noti = noti;
		logger.info(noti);
	}
	
	public String getNoti() {
		return noti;
	}
}
