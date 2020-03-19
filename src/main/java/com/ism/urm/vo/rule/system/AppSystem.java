package com.ism.urm.vo.rule.system;

import com.ism.urm.vo.rule.RuleVo;

public class AppSystem extends RuleVo {

    String code;
    String type;
    String devType;
    String hostId;
    String ip;
    String port;
    String userId;
    String userPasswd;
    String dbType;
    String jdbcType;
    String dbName;
    String dbParams;
    String remark;
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getDevType() {
        return devType;
    }
    public void setDevType(String devType) {
        this.devType = devType;
    }
    public String getHostId() {
        return hostId;
    }
    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getPort() {
        return port;
    }
    public void setPort(String port) {
        this.port = port;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserPasswd() {
        return userPasswd;
    }
    public void setUserPasswd(String userPasswd) {
        this.userPasswd = userPasswd;
    }
    public String getDbType() {
        return dbType;
    }
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
    public String getJdbcType() {
        return jdbcType;
    }
    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }
    public String getDbName() {
        return dbName;
    }
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
    public String getDbParams() {
        return dbParams;
    }
    public void setDbParams(String dbParams) {
        this.dbParams = dbParams;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

}
