package com.ism.urm.vo.rule.data;

import java.io.Serializable;

public class Field implements Serializable {

    private static final long serialVersionUID = 1L;

    String dataId;
    String fieldId;
    String name;
    String engName;
    String type;
    int length;
    int subLength;
    String bigo;
    String dateFormat;
    String sno;
    String rsno;
    boolean nullable;
    boolean keyYN;
    boolean sqlYN;
    boolean repeatYN;
    int repeatCnt;
    String refInfo;

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getSubLength() {
        return subLength;
    }

    public void setSubLength(int subLength) {
        this.subLength = subLength;
    }

    public String getBigo() {
        return bigo;
    }

    public void setBigo(String bigo) {
        this.bigo = bigo;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getRsno() {
        return rsno;
    }

    public void setRsno(String rsno) {
        this.rsno = rsno;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isKeyYN() {
        return keyYN;
    }

    public void setKeyYN(boolean keyYN) {
        this.keyYN = keyYN;
    }

    public boolean isSqlYN() {
        return sqlYN;
    }

    public void setSqlYN(boolean sqlYN) {
        this.sqlYN = sqlYN;
    }

    public boolean isRepeatYN() {
        return repeatYN;
    }

    public void setRepeatYN(boolean repeatYN) {
        this.repeatYN = repeatYN;
    }

    public int getRepeatCnt() {
        return repeatCnt;
    }

    public void setRepeatCnt(int repeatCnt) {
        this.repeatCnt = repeatCnt;
    }

    public String getRefInfo() {
        return refInfo;
    }

    public void setRefInfo(String refInfo) {
        this.refInfo = refInfo;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        
        buf .append("-----------------------------------------\n")
            .append("dataId    : ").append(dataId).append("\n")
            .append("fieldId   : ").append(fieldId).append("\n")
            .append("nm        : ").append(name).append("\n")
            .append("id        : ").append(engName).append("\n")
            .append("type      : ").append(type).append("\n")
            .append("len1      : ").append(length).append("\n")
            .append("len2      : ").append(subLength).append("\n")
            .append("bigo      : ").append(bigo).append("\n")
            .append("dFormat   : ").append(dateFormat).append("\n")
            .append("sno       : ").append(sno).append("\n")
            .append("rsno      : ").append(rsno).append("\n")
            .append("nullable  : ").append(nullable).append("\n")
            .append("pkYn      : ").append(keyYN).append("\n")
            .append("isSql     : ").append(sqlYN).append("\n")
            .append("repeatYn  : ").append(repeatYN).append("\n")
            .append("repeatNo  : ").append(repeatCnt).append("\n")
            .append("refInfo   : ").append(refInfo).append("\n");

        return buf.toString();
    }
    
}
