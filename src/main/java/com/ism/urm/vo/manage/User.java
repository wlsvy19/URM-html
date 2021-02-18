package com.ism.urm.vo.manage;

public class User {
    String id;
    String name;
    String password;
    String dept;
    String deptName;
    String position;
    String positionName;
    String grade;
    String gradeName;
    String officeTelNo;
    String generalTelNo;
    String celNo;
    String ip;
    boolean yn = true;
    boolean ssoYn = false;
    String authId;

//    String frsEnrTs;
//    String lstAmnTs;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getDept() {
        return dept;
    }
    public void setDept(String dept) {
        this.dept = dept;
    }
    public String getDeptName() {
        return deptName;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getPositionName() {
        return positionName;
    }
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getGradeName() {
        return gradeName;
    }
    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }
    public String getOfficeTelNo() {
        return officeTelNo;
    }
    public void setOfficeTelNo(String officeTelNo) {
        this.officeTelNo = officeTelNo;
    }
    public String getGeneralTelNo() {
        return generalTelNo;
    }
    public void setGeneralTelNo(String generalTelNo) {
        this.generalTelNo = generalTelNo;
    }
    public String getCelNo() {
        return celNo;
    }
    public void setCelNo(String celNo) {
        this.celNo = celNo;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public boolean isYn() {
        return yn;
    }
    public void setYn(boolean yn) {
        this.yn = yn;
    }
    public boolean isSsoYn() {
        return ssoYn;
    }
    public void setSsoYn(boolean ssoYn) {
        this.ssoYn = ssoYn;
    }
    public String getAuthId() {
        return authId;
    }
    public void setAuthId(String authId) {
        this.authId = authId;
    }

}
