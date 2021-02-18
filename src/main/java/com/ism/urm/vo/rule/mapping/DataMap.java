package com.ism.urm.vo.rule.mapping;

import java.util.List;

import com.ism.urm.vo.rule.RuleVo;
import com.ism.urm.vo.rule.data.Data;

public class DataMap extends RuleVo {
    String sourceDataId;
    String targetDataId;

    List<DataMapLine> mapLines;
    List<DataMapValue> mapValues;

    Data sourceData;
    Data targetData;

    String requestId;
    String requestName;
    String interfaceId;
    
    public String getSourceDataId() {
        return sourceDataId;
    }

    public void setSourceDataId(String sourceDataId) {
        this.sourceDataId = sourceDataId;
    }

    public String getTargetDataId() {
        return targetDataId;
    }

    public void setTargetDataId(String targetDataId) {
        this.targetDataId = targetDataId;
    }

    public List<DataMapLine> getMapLines() {
        return mapLines;
    }

    public void setMapLines(List<DataMapLine> mapLines) {
        this.mapLines = mapLines;
    }

    public List<DataMapValue> getMapValues() {
        return mapValues;
    }

    public void setMapValues(List<DataMapValue> mapValues) {
        this.mapValues = mapValues;
    }

    public Data getSourceData() {
        return sourceData;
    }

    public void setSourceData(Data sourceData) {
        this.sourceData = sourceData;
    }

    public Data getTargetData() {
        return targetData;
    }

    public void setTargetData(Data targetData) {
        this.targetData = targetData;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

}