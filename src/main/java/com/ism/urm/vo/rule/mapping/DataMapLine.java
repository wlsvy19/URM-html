package com.ism.urm.vo.rule.mapping;

public class DataMapLine {
    String mapId;
    int lineSeq;
    String sourceFieldId;
    String targetFieldId;

    public String getMapId() {
        return mapId;
    }
    public void setMapId(String mapId) {
        this.mapId = mapId;
    }
    public int getLineSeq() {
        return lineSeq;
    }
    public void setLineSeq(int lineSeq) {
        this.lineSeq = lineSeq;
    }
    public String getSourceFieldId() {
        return sourceFieldId;
    }
    public void setSourceFieldId(String sourceFieldId) {
        this.sourceFieldId = sourceFieldId;
    }
    public String getTargetFieldId() {
        return targetFieldId;
    }
    public void setTargetFieldId(String targetFieldId) {
        this.targetFieldId = targetFieldId;
    }
}
