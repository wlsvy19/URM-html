package com.ism.urm.vo.rule;

import java.util.Date;

public class RuleVo {
    
    String id;
    String name;
    
    Date regDate;
    String regId;
    Date chgDate;
    String chgId;

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
    public Date getRegDate() {
        return regDate;
    }
    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }
    public String getRegId() {
        return regId;
    }
    public void setRegId(String regId) {
        this.regId = regId;
    }
    public Date getChgDate() {
        return chgDate;
    }
    public void setChgDate(Date chgDate) {
        this.chgDate = chgDate;
    }
    public String getChgId() {
        return chgId;
    }
    public void setChgId(String chgId) {
        this.chgId = chgId;
    }

}
