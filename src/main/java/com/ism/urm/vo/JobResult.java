package com.ism.urm.vo;

public class JobResult {

    public static final int SUCCESS = 0;
    public static final int ERROR = -1;

    private int code;
    private String message;

    private Object obj;

    public JobResult(int code) {
        this(code, "");
    }

    public JobResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }
    public void setObj(Object obj) {
        this.obj = obj;
    }

}
