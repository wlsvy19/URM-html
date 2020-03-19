package com.ism.urm.vo.rule.data;

import java.util.List;

import com.ism.urm.vo.rule.RuleVo;

public class Data extends RuleVo {
    String subId;
    String systemCode = "EAI";
    String channelCode;
    String type = "1";

    String inOutGbn = "1";
    boolean stdUseYN;

    String excelFileName;

    List<Field> fields;

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInOutGbn() {
        return inOutGbn;
    }

    public void setInOutGbn(String inOutGbn) {
        this.inOutGbn = inOutGbn;
    }

    public boolean isStdUseYN() {
        return stdUseYN;
    }

    public void setStdUseYN(boolean stdUseYN) {
        this.stdUseYN = stdUseYN;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

}