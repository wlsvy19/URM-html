//package com.ism.web.vo;
//
//import java.io.Serializable;
//
//import org.hibernate.EmptyInterceptor;
//import org.hibernate.type.Type;
//
//import com.ism.common.logger.DefaultLogger;
//import com.ism.common.util.SeedManager;
//import com.ism.web.vo.admin.UserVo;
//import com.ism.web.vo.system.ApplicationVo;
//import com.ism.web.vo.system.FileApplicationVo;
//import com.ism.web.vo.system.WebApplicationVo;
//
//public class HiberInterceptor extends EmptyInterceptor {
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	
//	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
//		if (entity instanceof ApplicationVo) {
//			for (int i = 0; i < propertyNames.length; i++) {
//				if(state[i] == null || (state[i]) instanceof String && ((String)state[i]).isEmpty()) {
//					continue;
//				}
//				if ("appUserPwd".equals(propertyNames[i])) {
//					try {
//						state[i] = SeedManager.applPassword("e",(String) state[i]);
//					} catch (Exception e) {
//						DefaultLogger.logE(e, e.getMessage());
//					}
//				}
//				return true;
//			}
//		} else if (entity instanceof UserVo) {
//			for (int i = 0; i < propertyNames.length; i++) {
//				if(state[i] == null || (state[i]) instanceof String && ((String)state[i]).isEmpty()) {
//					continue;
//				}
//				
//				//PASS RESET
//				if(state[i].equals("##RESETPWD##")) {
//					state[i] = "";
//				}
//				
//				if ("userPassword".equals(propertyNames[i])) {
//					try {
//						state[i] = SeedManager.applPassword("e",(String) state[i]);
//					} catch (Exception e) {
//						DefaultLogger.logE(e, e.getMessage());
//					}
//					return true;
//				}
//			}
//		} else if (entity instanceof FileApplicationVo) {
//			for (int i = 0; i < propertyNames.length; i++) {
//				if(state[i] == null || (state[i]) instanceof String && ((String)state[i]).isEmpty()) {
//					continue;
//				}
//				if ("passphrase".equals(propertyNames[i])) {
//					try {
//						state[i] = SeedManager.applPassword("e",(String) state[i]);
//					} catch (Exception e) {
//						DefaultLogger.logE(e, e.getMessage());
//					}
//					return true;
//				}
//			}
//		} else if (entity instanceof WebApplicationVo) {
//			for (int i = 0; i < propertyNames.length; i++) {
//				if(state[i] == null || (state[i]) instanceof String && ((String)state[i]).isEmpty()) {
//					continue;
//				}
//				if ("passphrase".equals(propertyNames[i])) {
//					try {
//						state[i] = SeedManager.applPassword("e",(String) state[i]);
//					} catch (Exception e) {
//						DefaultLogger.logE(e, e.getMessage());
//					}
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//
//	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
//		if (entity instanceof ApplicationVo) {
//			for (int i = 0; i < propertyNames.length; i++) {
//				if(state[i] == null || (state[i]) instanceof String && ((String)state[i]).isEmpty()) {
//					continue;
//				}
//				if ("appUserPwd".equals(propertyNames[i])) {
//					try {
//						state[i] = SeedManager.applPassword("d",(String) state[i]);
//					} catch (Exception e) {
//						DefaultLogger.logE(e, e.getMessage());
//					}
//					return true;
//				}
//			}
//		} else if (entity instanceof UserVo) {
//			for (int i = 0; i < propertyNames.length; i++) {
//				if(state[i] == null || (state[i]) instanceof String && ((String)state[i]).isEmpty()) {
//					continue;
//				}
//				
//				if ("userPassword".equals(propertyNames[i])) {
//					try {
//						state[i] = SeedManager.applPassword("d",(String) state[i]);
//					} catch (Exception e) {
//						DefaultLogger.logE(e, e.getMessage());
//					}
//					return true;
//				}
//			}
//		} else if (entity instanceof FileApplicationVo) {
//			for (int i = 0; i < propertyNames.length; i++) {
//				if(state[i] == null || (state[i]) instanceof String && ((String)state[i]).isEmpty()) {
//					continue;
//				}
//				if ("passphrase".equals(propertyNames[i])) {
//					try {
//						state[i] = SeedManager.applPassword("d",(String) state[i]);
//					} catch (Exception e) {
//						DefaultLogger.logE(e, e.getMessage());
//					}
//					return true;
//				}
//			}
//		} else if (entity instanceof WebApplicationVo) {
//			for (int i = 0; i < propertyNames.length; i++) {
//				if(state[i] == null || (state[i]) instanceof String && ((String)state[i]).isEmpty()) {
//					continue;
//				}
//				if ("passphrase".equals(propertyNames[i])) {
//					try {
//						state[i] = SeedManager.applPassword("d",(String) state[i]);
//					} catch (Exception e) {
//						DefaultLogger.logE(e, e.getMessage());
//					}
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//
//	public boolean onFlushDirty(Object entity, Serializable id,	Object[] state, Object[] previousState, String[] propertyNames, Type[] types) {
//		if (entity instanceof ApplicationVo) {
//			for (int i = 0; i < propertyNames.length; i++) {
//				if(state[i] == null || (state[i]) instanceof String && ((String)state[i]).isEmpty()) {
//					continue;
//				}
//				if ("appUserPwd".equals(propertyNames[i])) {
//					try {
//						state[i] = SeedManager.applPassword("e",(String) state[i]);
//					} catch (Exception e) {
//						DefaultLogger.logE(e, e.getMessage());
//					}
//					return true;
//				}
//			}
//		} else if (entity instanceof UserVo) {
//			for (int i = 0; i < propertyNames.length; i++) {
//				if(state[i] == null || (state[i]) instanceof String && ((String)state[i]).isEmpty()) {
//					continue;
//				}
//				
//				//PASS RESET
//				if(state[i].equals("##RESETPWD##")) {
//					state[i] = "";
//				}
//				
//				if ("userPassword".equals(propertyNames[i])) {
//					try {
//						state[i] = SeedManager.applPassword("e",(String) state[i]);
//					} catch (Exception e) {
//						DefaultLogger.logE(e, e.getMessage());
//					}
//					return true;
//				}
//			}
//		} else if (entity instanceof FileApplicationVo) {
//			for (int i = 0; i < propertyNames.length; i++) {
//				if(state[i] == null || (state[i]) instanceof String && ((String)state[i]).isEmpty()) {
//					continue;
//				}
//				if ("passphrase".equals(propertyNames[i])) {
//					try {
//						state[i] = SeedManager.applPassword("e",(String) state[i]);
//					} catch (Exception e) {
//						DefaultLogger.logE(e, e.getMessage());
//					}
//					return true;
//				}
//			}
//		} else if (entity instanceof WebApplicationVo) {
//			for (int i = 0; i < propertyNames.length; i++) {
//				if(state[i] == null || (state[i]) instanceof String && ((String)state[i]).isEmpty()) {
//					continue;
//				}
//				if ("passphrase".equals(propertyNames[i])) {
//					try {
//						state[i] = SeedManager.applPassword("e",(String) state[i]);
//					} catch (Exception e) {
//						DefaultLogger.logE(e, e.getMessage());
//					}
//					return true;
//				}
//			}
//		}
//		return false;
//	}
//}
