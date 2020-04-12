package com.play.base.contants;

/**
 * @author hushengmeng
 * @date 2017/8/4.
 */
public class RedisKeyConstants {

     //根据用户id获取用户key
     public final static String CACHE_USER_ID_KEY = "user_%d";
    //在线用户token key
    public final static String CACHE_USER_ONLINE_TOKEN_KEY = "user_online_token_%s";
    //在线用户id key
    public final static String CACHE_USER_ONLINE_ID_KEY = "user_online_id_%d";
    //用户关注key
    public final static String CACHE_USER_FOLLOW_KEY = "user_follow_%d";
    //用户粉丝key
    public final static String CACHE_USER_FANS_KEY = "user_fans_%d";
    //用户好友key
    public final static String CACHE_USER_FRIEND_KEY = "user_friend_%d";
    //用户访客key
    public final static String CACHE_USER_VISIT_KEY = "user_visit_%d";
    //用户所在聊天室key
    public final static String CACHE_USER_CHATROOM_KEY = "user_chatroom";
    //用户收藏的聊天室key
    public final static String CACHE_USER_COLLECT_CHATROOM_KEY = "user_collect_chatroom_%d";
    //聊天室key
    public final static String CACHE_CHATROOM_ID_KEY = "chatroom_%d";
    //聊天室黑名单key
    public final static String CACHE_CHATROOM_BLACK_KEY = "chatroom_black_%d";
    //聊天室工作人员key
    public final static String CACHE_CHATROOM_STAFF_KEY = "chatroom_staff_%d";
    //聊天室排麦队列key
    public final static String CACHE_CHATROOM_MIC_QUEUE_KEY = "chatroom_mic_queue_%d";
    //聊天室所有用户key
    public final static String CACHE_CHATROOM_USER_KEY = "chatroom_user_%d";
    //聊天室麦位信息key
    public final static String CACHE_CHATROOM_MIC_KEY = "chatroom_mic_%d";
    //禁言用户key
    public final static String CACHE_CHATROOM_USER_NOSPEAK_KEY = "chatroom_user_nospeak_%d";
    //聊天室工作人员key
    public final static String CACHE_CHATROOM_STAFF_TYPE_KEY = "chatroom_staff_type_%d_%d";
    //聊天室麦位倒计时key
    public final static String CACHE_CHATROOM_TIMER_POSITION_KEY = "chatroom_timer_position_%d_%d";
    //聊天室锁密码key
    public final static String CACHE_CHATROOM_LOCK_KEY = "chatroom_lock";

}
