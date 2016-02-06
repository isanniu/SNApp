package com.sannniu.ncore.db.exception;

/**
 * @description 数据库没有打开
 */
public class TADBNotOpenException extends Exception {
    private static final long serialVersionUID = 1L;

    public TADBNotOpenException() {
        super();
    }

    public TADBNotOpenException(String detailMessage) {
        super(detailMessage);
    }

}
