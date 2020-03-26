package com.play.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.play.base.dao.IBaseDao;
import com.play.base.exception.ServiceException;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.base.utils.*;
import com.play.ucenter.dao.IUserAccountDao;
import com.play.ucenter.dao.IUserDao;
import com.play.ucenter.model.User;
import com.play.ucenter.model.UserAccount;
import com.play.ucenter.service.IUserAccountService;
import com.play.ucenter.service.IUserService;
import com.play.ucenter.view.UserView;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
 * Created by hushengmeng on 2020/3/30.
 */
@Service(value="userAccountService")
public class UserAccountServiceImpl extends BaseServiceImpl<UserAccount, Long> implements IUserAccountService {

    @Resource
    private IUserAccountDao userAccountDao;

    @Override
    public IBaseDao<UserAccount> getBaseDao() {
        return userAccountDao;
    }

    @Override
    public UserAccount getByUserId(Long userId) {
        return this.findUniqueByParams("userId",userId);
    }
}
