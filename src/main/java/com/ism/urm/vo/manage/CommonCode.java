package com.ism.urm.vo.manage;

import java.io.Serializable;

public class CommonCode implements Serializable {

    private static final long serialVersionUID = 1L;

    String kind;
    String code;
    String name; 

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

}
