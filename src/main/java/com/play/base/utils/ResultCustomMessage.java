package com.play.base.utils;

import java.io.Serializable;

public class ResultCustomMessage extends ResultMessage{
    private static final long serialVersionUID = 1L;

    protected ResultCustomMessage(int code, String message) {
        super(code, message);
    }

    public static final ResultCustomMessage F1001 = new ResultCustomMessage(1001, "验证码发送失败， %s ");

    public static final ResultCustomMessage F1002 = new ResultCustomMessage(1001, "验证码校验失败， %s ");
}
