package com.play.base.utils;

public class ResultCustomMessage extends ResultMessage{
    private static final long serialVersionUID = 1L;

    protected ResultCustomMessage(int code, String message) {
        super(code, message);
    }

    public static final ResultCustomMessage F1001 = new ResultCustomMessage(1001, "验证码发送失败， %s");

    public static final ResultCustomMessage F1002 = new ResultCustomMessage(1002, "验证码校验失败， %s");

    public static final ResultCustomMessage F1003 = new ResultCustomMessage(1003, "融云获取token异常");

    public static final ResultCustomMessage F1004 = new ResultCustomMessage(1004, "账号已冻结");

    public static final ResultCustomMessage F1005 = new ResultCustomMessage(1005, "已拥有个人聊天室");

    public static final ResultCustomMessage F1006 = new ResultCustomMessage(1006, "你已被请出房间");

    public static final ResultCustomMessage F1007 = new ResultCustomMessage(1007, "聊天室已被冻结");

    public static final ResultCustomMessage F1008 = new ResultCustomMessage(1008, "聊天室已关闭");

    public static final ResultCustomMessage F1009 = new ResultCustomMessage(1009, "加入聊天室失败，密码错误");

    public static final ResultCustomMessage F1010 = new ResultCustomMessage(1010, "当前麦位已有人");

    public static final ResultCustomMessage F1011 = new ResultCustomMessage(1011, "礼物已下架");

    public static final ResultCustomMessage F1012 = new ResultCustomMessage(1012, "余额不足");

    public static final ResultCustomMessage F1013 = new ResultCustomMessage(1013, "礼物数量不足");

    public static final ResultCustomMessage F1014 = new ResultCustomMessage(1014, "编号池足够大");

    public static final ResultCustomMessage F1015 = new ResultCustomMessage(1015, "权限不足");

    public static final ResultCustomMessage F1016 = new ResultCustomMessage(1016, "目标用户不存在");

    public static final ResultCustomMessage F1017 = new ResultCustomMessage(1017, "非法操作");

    public static final ResultCustomMessage F1018 = new ResultCustomMessage(1018, "支付失败，请稍后重试");
}
