package com.play.im.dao;


import com.play.base.dao.IBaseDao;
import com.play.im.model.Chatroom;

import java.util.List;

/**
 * Created by hushengmeng on 2017/7/4.
 */
public interface IChatroomDao extends IBaseDao<Chatroom> {
    /**
     * 修改房间开关状态
     * @param roomId
     * @param status 1：开启 2：关闭 3：冻结
     */
    void updateRoomStatus(Integer roomId,Integer status);
}
