package com.play.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.play.base.dao.IBaseDao;
import com.play.base.exception.ServiceException;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.base.utils.CheckSumBuilder;
import com.play.base.utils.CommonUtil;
import com.play.base.utils.ConfigPropertieUtils;
import com.play.ucenter.dao.IUserDao;
import com.play.ucenter.model.User;
import com.play.ucenter.service.IUserService;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by hushengmeng on 2017/7/4.
 */
@Service(value="userCmsService")
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements IUserService {

    @Resource
    private IUserDao userCmsDao;

    @Override
    public IBaseDao<User> getBaseDao() {
        return userCmsDao;
    }

    @Override
    public void logout(String token) {

    }

    @Override
    public void sendMobileCode(String mobile) throws ServiceException {
        Map<String, String> message = new HashMap<String, String>();

        String url = ConfigPropertieUtils.getString("tel_url");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        String appKey = ConfigPropertieUtils.getString("tel_appKey");
        String appSecret = ConfigPropertieUtils.getString("tel_appSecret");
        String nonce = CommonUtil.createNoncestr();
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce, curTime);//参考 计算CheckSum的java代码

        // 设置请求的header
        httpPost.addHeader("AppKey", appKey);
        httpPost.addHeader("Nonce", nonce);
        httpPost.addHeader("CurTime", curTime);
        httpPost.addHeader("CheckSum", checkSum);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        // 设置请求的参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("templateid", "3144195"));
        nvps.add(new BasicNameValuePair("mobile", mobile));
        String result = "";
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("发送短信返回：" + result);
        Integer code = JSON.parseObject(result).getInteger("code");
        message.put("code", String.valueOf(code));
        if (code == 200) {
            message.put("msg", "发送成功");
        } else if (code == 315) {
            message.put("msg", "IP限制");
        } else if (code == 403) {
            message.put("msg", "非法操作或没有权限");
        } else if (code == 414) {
            message.put("msg", "参数错误");
        } else if (code == 416) {
            message.put("msg", "今日验证码获取已达上限，明日再试");
        } else if (code == 500) {
            message.put("msg", "服务器内部错误");
        } else {
            message.put("msg", "其他错误");
        }

    }
}
