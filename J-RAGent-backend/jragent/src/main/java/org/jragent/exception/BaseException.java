package org.jragent.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

    private final int code;

    public BaseException() {
        this(400, "业务异常");
    }

    public BaseException(String msg) {
        this(400, msg);
    }

    public BaseException(int code, String msg) {
        super(msg);
        this.code = code;
    }

}
