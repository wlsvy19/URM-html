package com.ism.urm.vo.manage;

import com.ism.urm.vo.rule.RuleVo;

public class User extends RuleVo {
	String id;
	String name;
	String passwd;
	String dept;
	String dept_nm;
	String position;
	String positionNm;
	String grade;
	String gradeNm;
	String officeTelNo;  
	String generalTelNo;
	String celNo;
	String ip;
	String yn;
	String frsEnrTs;
	String lstAmnTs;
	String ssoYn;
	String authId; 

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getDept_nm() {
		return dept_nm;
	}

	public void setDept_nm(String dept_nm) {
		this.dept_nm = dept_nm;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPositionNm() {
		return positionNm;
	}

	public void setPositionNm(String positionNm) {
		this.positionNm = positionNm;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getGradeNm() {
		return gradeNm;
	}

	public void setGradeNm(String gradeNm) {
		this.gradeNm = gradeNm;
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

	public String getYn() {
		return yn;
	}

	public void setYn(String yn) {
		this.yn = yn;
	}

	public String getFrsEnrTs() {
		return frsEnrTs;
	}

	public void setFrsEnrTs(String frsEnrTs) {
		this.frsEnrTs = frsEnrTs;
	}

	public String getLstAmnTs() {
		return lstAmnTs;
	}

	public void setLstAmnTs(String lstAmnTs) {
		this.lstAmnTs = lstAmnTs;
	}

	public String getSsoYn() {
		return ssoYn;
	}

	public void setSsoYn(String ssoYn) {
		this.ssoYn = ssoYn;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

}
