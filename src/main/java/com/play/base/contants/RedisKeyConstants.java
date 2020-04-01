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
    //聊天室key
    public final static String CACHE_CHATROOM_ID_KEY = "chatroom_%d";


    //图书目录缓存key
    public final static String CACHE_BOOK_CATALOG_KEY = "book_catalog_";

    //图书驱动key
    public final static String CACHE_DRIVE_BOOK_KEY = "drive_book_type_%s_status_%s";

    //图书驱动具体图书key
    public final static String CACHE_DRIVE_BOOK_ONE_KEY = "drive_book_type_%s_bid_%s_status_%s";

    //章节+内容key
    public final static String CACHE_CHAPTER_TYPE_KEY = "chapter_content_cid_%s_type_%s";

    //图书信息key
    public final static String CACHE_BOOK_KEY = "book_id_";

    //充值赠返最大金额key
    public final static String CACHE_RECHARGE_MAX_VIRTUAL_KEY = "recharge_max_virtual";

    //版本信息key
    public final static String CACHE_VERSION_INFO_CHANNEL_KEY = "version_info_channel_";

    //父id获取子分类key
    public final static String CACHE_CATEGORY_LIST_PID_KEY = "category_list_pid_";

    //新用户福利类型key
    public final static String CACHE_NEW_USER_WELFARE_TYPE_KEY = "new_user_welfare_type";

    //点击量最高图书key
    public final static String CACHE_MAX_CLICK_BOOK_KEY = "max_click_book";
}
