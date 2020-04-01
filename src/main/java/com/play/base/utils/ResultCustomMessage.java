package com.play.base.utils;

import java.io.Serializable;

public class ResultCustomMessage extends ResultMessage{
    private static final long serialVersionUID = 1L;

    protected ResultCustomMessage(int code, String message) {
        super(code, message);
    }

    public static final ResultCustomMessage F1001 = new ResultCustomMessage(1001, "验证码发送失败， %s");

    public static final ResultCustomMessage F1002 = new ResultCustomMessage(1001, "验证码校验失败， %s");

    public static final ResultCustomMessage F1003 = new ResultCustomMessage(1003, "融云获取token异常");

    public static final ResultCustomMessage F1004 = new ResultCustomMessage(1004, "账号已冻结");

    public static final ResultCustomMessage F1005 = new ResultCustomMessage(1005, "已拥有个人聊天室");
}
