package com.play.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.play.base.exception.ServiceException;
import com.play.base.utils.ConfigPropertieUtils;
import com.play.base.utils.HttpClientHandler;
import com.play.base.utils.ResultCustomMessage;
import com.play.base.utils.SHA1Util;
import com.play.im.model.RongyunToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RongyunService {
    private static Logger logger = LoggerFactory.getLogger(RongyunService.class);

    private static String appSecret = ConfigPropertieUtils.getString("rongyun_app_key");

    private static String appKey = ConfigPropertieUtils.getString("rongyun_app_secret");

    private static String host = ConfigPropertieUtils.getString("rongyun_host");


    /**
     * 获取融云token
     * @param userId
     * @param name 用户名称
     * @param portraitUri 用户头像 URI
     * @return
     * @throws ServiceException
     */
    public static String getToken(Long userId, String name, String portraitUri) throws ServiceException {
        RongyunToken rongyun = null;
        try {
            Map<String, String> headers = getHeaders();
            Map<String, String> params = new HashMap<String, String>();
            params.put("userId", String.valueOf(userId));
            params.put("name", name);
            params.put("portraitUri", portraitUri);

            String resultStr = HttpClientHandler.post(host + "/user/getToken.json", headers, params);
            logger.info("getToken融云返回: 用户id " + userId + ",resultToken " + resultStr);
            rongyun = JSON.parseObject(resultStr,RongyunToken.class);
            if (rongyun == null || rongyun.getCode() != 200) {
                throw new ServiceException(ResultCustomMessage.F1003);
            }
            return rongyun.getToken();
        } catch (Exception e) {
            throw new ServiceException(ResultCustomMessage.F1003);
        }
    }

    public static Map<String, String> getHeaders() {
        String nonce = String.valueOf(Math.random() * 1000000);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        StringBuilder toSign = new StringBuilder(appSecret).append(nonce).append(timestamp);
        String sign = SHA1Util.getSHA1(toSign.toString());
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("RC-App-Key", appKey);
        headers.put("RC-Nonce", nonce);
        headers.put("RC-Timestamp", timestamp);
        headers.put("RC-Signature", sign);
        return headers;
    }

}
