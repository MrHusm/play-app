package com.play.ucenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.play.base.contants.RedisKeyConstants;
import com.play.base.dao.IBaseDao;
import com.play.base.exception.ServiceException;
import com.play.base.service.impl.BaseServiceImpl;
import com.play.base.utils.*;
import com.play.ucenter.dao.IUserDao;
import com.play.ucenter.model.User;
import com.play.ucenter.model.UserAccount;
import com.play.ucenter.model.UserAuditInfo;
import com.play.ucenter.service.IUserAccountService;
import com.play.ucenter.service.IUserAuditInfoService;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by hushengmeng on 2020/3/30.
 */
@Service(value="userService")
public class UserServiceImpl extends BaseServiceImpl<User, Long> implements IUserService {
    @Resource
    private IUserDao userDao;
    @Resource
    private IUserAccountService userAccountService;
    @Resource
    private IUserAuditInfoService userAuditInfoService;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, User> userRedisTemplate;
    @Resource(name = "redisTemplate")
    private RedisTemplate<String, Long> userRelRedisTemplate;

    @Override
    public IBaseDao<User> getBaseDao() {
        return userDao;
    }

    @Override
    public UserView getByUserId(Long userId, Long targetUserId) {
        String key = String.format(RedisKeyConstants.CACHE_USER_ID_KEY, targetUserId);
        User user = userRedisTemplate.opsForValue().get(key);
        if (user == null) {
            user = this.findUniqueByParams("userId", userId);
            if (user != null) {
                userRedisTemplate.opsForValue().set(key, user, 8, TimeUnit.HOURS);
            }
        }
        if (userId.longValue() == targetUserId.longValue() && StringUtils.isNotBlank(user.getPendHeadUrl())) {
            user.setHeadUrl(user.getPendHeadUrl());
        }
        UserView userView = BeanUtils.copyProperties(UserView.class, user);
        return userView;
    }

    @Override
    public void deleteUserCache(Long userId) {
        String key = String.format(RedisKeyConstants.CACHE_USER_ID_KEY, userId);
        userRedisTemplate.delete(key);
    }

    @Override
    public void logout(String token) {

    }

    @Override
    public void sendMobileCode(String mobile) throws ServiceException {
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
        if(StringUtils.isBlank(result)){
            throw new ServiceException(ResultCustomMessage.F4080);
        }
        logger.info("发送短信返回：" + result);
        Integer code = JSON.parseObject(result).getInteger("code");
        if (code == 200) {
//            message.put("msg", "发送成功");
        } else if (code == 315) {
            throw new ServiceException(ResultCustomMessage.F1001,"IP限制");
        } else if (code == 403) {
            throw new ServiceException(ResultCustomMessage.F1001,"非法操作或没有权限");
        } else if (code == 414) {
            throw new ServiceException(ResultCustomMessage.F1001,"参数错误");
        } else if (code == 416) {
            throw new ServiceException(ResultCustomMessage.F1001,"今日验证码获取已达上限，明日再试");
        } else if (code == 500) {
            throw new ServiceException(ResultCustomMessage.F1001,"服务器内部错误");
        } else {
            throw new ServiceException(ResultCustomMessage.F1001,"其他错误");
        }
    }

    @Override
    public Map<String,Object> loginByMobile(String mobile, String code, User loginUser) throws ServiceException{
        verifyCode(mobile,code);
        Map<String,Object> data = new HashMap<String, Object>();
        User user =  this.findUniqueByParams("mobile", mobile);
        if(user == null){
            //注册账号 TODO
            data.put("new",1);
            loginUser.setUserId(1L);
            loginUser.setPrettyId(1L);
            loginUser.setPwd("1");

            loginUser.setCreateDate(new Date());
            loginUser.setUpdateDate(new Date());

            UserAccount userAccount = new UserAccount();
            userAccount.setUserId(1L);
            userAccount.setCreateDate(new Date());
            userAccount.setUpdateDate(new Date());
            this.save(loginUser);
            userAccountService.save(userAccount);
        }else{
            data.put("user",user);
            data.put("new",0);
        }
        return data;
    }

    /**
     * 校验验证码
     * @param mobile
     * @param verifyCode
     * @throws ServiceException
     */
    public void verifyCode(String mobile,String verifyCode)throws ServiceException{
        String url = ConfigPropertieUtils.getString("tel_verify_code_url");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        String appKey = ConfigPropertieUtils.getString("tel_appKey");
        String appSecret = ConfigPropertieUtils.getString("tel_appSecret");
        String nonce =  CommonUtil.createNoncestr();
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
        nvps.add(new BasicNameValuePair("mobile", mobile));
        nvps.add(new BasicNameValuePair("code", verifyCode));
        String result = "";
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            // 执行请求
            HttpResponse httpResponse = httpClient.execute(httpPost);
            // 打印执行结果
            result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("校验验证码返回：" + result);
        if(StringUtils.isBlank(result)){
            throw new ServiceException(ResultCustomMessage.F4080);
        }
        Integer code = JSON.parseObject(result).getInteger("code");
        if(code == 200){

        }else if(code == 301){
            throw new ServiceException(ResultCustomMessage.F1002,"被封禁");
        }else if(code == 315){
            throw new ServiceException(ResultCustomMessage.F1002,"IP限制");
        }else if(code == 403){
            throw new ServiceException(ResultCustomMessage.F1002,"非法操作或没有权限");
        }else if(code == 404){
            throw new ServiceException(ResultCustomMessage.F1002,"对象不存在");
        }else if(code == 413){
            throw new ServiceException(ResultCustomMessage.F1002,"验证码错误");
        }else if(code == 414){
            throw new ServiceException(ResultCustomMessage.F1002,"参数错误");
        }else if(code == 500){
            throw new ServiceException(ResultCustomMessage.F1002,"服务器内部错误");
        }else{
            throw new ServiceException(ResultCustomMessage.F1002,"其他错误");
        }
    }

    @Override
    public void updateUser(User user) {
        this.update(user);
        if (StringUtils.isNotBlank(user.getPendHeadUrl())) {
            //保存待审头像
            UserAuditInfo userAuditInfo = new UserAuditInfo();
            userAuditInfo.setUserId(user.getUserId());
            userAuditInfo.setAuditType(3);
            userAuditInfo.setContent(user.getPendHeadUrl());
            userAuditInfo.setCreator(user.getUserId());
            userAuditInfo.setCreatedDate(new Date());
            this.userAuditInfoService.save(userAuditInfo);
        }
        //清除缓存
        deleteUserCache(user.getUserId());
    }

    @Override
    public List<UserView> search(String keyword) {
        List<User> users = this.findListByParams("userId",keyword);
        if(CollectionUtils.isEmpty(users)){
            users = this.findListByParams("nickName","%"+ keyword + "%");
        }
        List<UserView> userViews = BeanUtils.copyProperties(List.class,users);
        return userViews;
    }

    @Override
    public Integer getRelType(Long userId, Long toUserId) {
        if (userId != null && userId.equals(toUserId)) {
            return 9; // 自己
        }
        String followKey = String.format(RedisKeyConstants.CACHE_USER_FOLLOW_KEY, userId);
        boolean isFollow = userRelRedisTemplate.opsForZSet().score(followKey, toUserId) == null;
        String beFollowKey = String.format(RedisKeyConstants.CACHE_USER_FOLLOW_KEY, toUserId);
        boolean beFollow = userRelRedisTemplate.opsForZSet().score(beFollowKey, userId) == null;
        if (isFollow && beFollow) {
            return 2; // 好友
        }
        if (isFollow) {
            return 1; // 已关注
        }
        if (beFollow) {//粉丝
            return 0;
        }
        return 0; // 没关系
    }

    @Override
    public Integer getRelationNum(Integer type, Long userId) {
        String key = String.format(RedisKeyConstants.CACHE_USER_FOLLOW_KEY, userId);
        switch (type) {
            case 1:
                key = String.format(RedisKeyConstants.CACHE_USER_FOLLOW_KEY, userId);
                break;
            case 2:
                key = String.format(RedisKeyConstants.CACHE_USER_FANS_KEY, userId);
                break;
            case 3:
                key = String.format(RedisKeyConstants.CACHE_USER_FRIEND_KEY, userId);
                break;
            case 4:
                key = String.format(RedisKeyConstants.CACHE_USER_VISIT_KEY, userId);
                break;
            default:
                break;
        }
        ZSetOperations<String, Long> operations = userRelRedisTemplate.opsForZSet();
        return operations.size(key + userId).intValue();
    }

    @Override
    public void addVisit(Long mId, Long userId) {
        String key = String.format(RedisKeyConstants.CACHE_USER_VISIT_KEY, userId);
        ZSetOperations<String, Long> operations = userRelRedisTemplate.opsForZSet();
        long nowTimestamp = System.currentTimeMillis();
        operations.add(key, mId, nowTimestamp);
    }

    @Override
    public void addFollow(Long mId, Long userId) {
        String followKey = String.format(RedisKeyConstants.CACHE_USER_FOLLOW_KEY, mId);
        String fansKey = String.format(RedisKeyConstants.CACHE_USER_FANS_KEY, userId);
        ZSetOperations<String, Long> operations = userRelRedisTemplate.opsForZSet();
        long nowTimestamp = System.currentTimeMillis();
        operations.add(followKey, userId, nowTimestamp);
        operations.add(fansKey, mId, nowTimestamp);
        String key = String.format(RedisKeyConstants.CACHE_USER_FOLLOW_KEY, userId);
        if (operations.score(key, mId) != null) {
            //添加好友关系
            String friendKeyOne = String.format(RedisKeyConstants.CACHE_USER_FRIEND_KEY, mId);
            String friendKeyTwo = String.format(RedisKeyConstants.CACHE_USER_FRIEND_KEY, userId);
            operations.add(friendKeyOne, userId, nowTimestamp);
            operations.add(friendKeyTwo, mId, nowTimestamp);
        }
    }

    @Override
    public void unFollow(Long mId, Long userId) {
        String followKey = String.format(RedisKeyConstants.CACHE_USER_FOLLOW_KEY, mId);
        String fansKey = String.format(RedisKeyConstants.CACHE_USER_FANS_KEY, userId);
        String friendKeyOne = String.format(RedisKeyConstants.CACHE_USER_FRIEND_KEY, mId);
        String friendKeyTwo = String.format(RedisKeyConstants.CACHE_USER_FRIEND_KEY, userId);
        ZSetOperations<String, Long> operations = userRelRedisTemplate.opsForZSet();
        operations.remove(followKey, userId);
        operations.remove(fansKey, mId);
        operations.remove(friendKeyOne, userId);
        operations.remove(friendKeyTwo, mId);
    }

    @Override
    public PageFinder getRelationListByPager(Integer type, Long userId, Query query) {
        String key = String.format(RedisKeyConstants.CACHE_USER_FOLLOW_KEY, userId);
        switch (type) {
            case 1:
                key = String.format(RedisKeyConstants.CACHE_USER_FOLLOW_KEY, userId);
                break;
            case 2:
                key = String.format(RedisKeyConstants.CACHE_USER_FANS_KEY, userId);
                break;
            case 3:
                key = String.format(RedisKeyConstants.CACHE_USER_FRIEND_KEY, userId);
                break;
            case 4:
                key = String.format(RedisKeyConstants.CACHE_USER_VISIT_KEY, userId);
                break;
            default:
                break;
        }
        ZSetOperations<String, Long> operations = userRelRedisTemplate.opsForZSet();
        int total = operations.size(key + userId).intValue();
        Set<ZSetOperations.TypedTuple<Long>> userIds = operations.reverseRangeWithScores(key, query.getOffset(), query.getOffset() + query.getPageSize() - 1);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (ZSetOperations.TypedTuple<Long> id : userIds) {
            Map<String, Object> data = new HashMap<String, Object>();
            UserView user = this.getByUserId(userId, id.getValue());
            data.put("user", user);
            data.put("date", id.getScore());
        }
        return new PageFinder(query.getPage(), query.getPageSize(), total, new ArrayList(list));
    }
}
