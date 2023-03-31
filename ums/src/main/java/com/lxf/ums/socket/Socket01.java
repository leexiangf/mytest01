package com.lxf.ums.socket;

import com.alibaba.fastjson.JSONObject;
import com.lxf.ums.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Classname Demo01
 * @Description
 * @Date 2022/7/11 9:47
 * @Author lxf
 */
@Slf4j
@ServerEndpoint("/demo/{userId}")
@Component
public class Socket01 {

    private static UserService userService;
    //在线人数
    private static AtomicInteger onlineCount=new AtomicInteger(0);
    private static ConcurrentHashMap<String,Socket01> socket01Map = new ConcurrentHashMap<>();
    private  Session session;
    private  String userId;

    /**
     * 注：spring注入是单例模式，而webSocket是多例 相冲突
     *    spring不去创建管理 webSocket，自然也就无法依赖注入
     *    多个用户连接，就会创建新的webSocket实例，就无法注入
     *    这里使用方法注入
     * @param userService
     */
    @Autowired
    public void setUserService(UserService userService){
        Socket01.userService=userService;
    }


    /**
     * 建立链接时调用
     * @param session
     * @param id
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId")String userId){
        log.info("用户：{} ，已经连接",userId);
        this.userId=userId;
        this.session=session;
        if(socket01Map.containsKey(userId)){
            socket01Map.remove(userId);
            socket01Map.put(userId,this);
        }else {
            socket01Map.put(userId,this);
            addOnlineCount();
        }
        log.info("用户{}连接，当前在线人数：{}",userId,getOnlineCount());
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.info("用户{}连接失败",userId);
            e.printStackTrace();
        }

    }

    /**
     * 关闭链接时调用
     * @param id
     */
    @OnClose
    public void onClose(@PathParam("userId")String userId){
        socket01Map.remove(userId);
        subOnlineCount();
        log.info("用户：{} ，已经断开连接",userId);
    }

    /**
     * 接收消息时调用
     * @param session
     * @param message
     */
    @OnMessage
    public void onMessage(Session session,String message)  {
        log.info("接收用户{}，发送的消息：{}",userId,message);
        try {
            JSONObject jsonObject = JSONObject.parseObject(message);
            //获取发送者
            jsonObject.put("fromUser",userId);
            //获取接受者
            String toUser = (String) jsonObject.get("toUser");
            //给接受者发送消息
            socket01Map.get(toUser).sendMessage(jsonObject.toJSONString());
            sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 错误时调用
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session,Throwable throwable){
        log.error("用户错误:" + this.userId + ",原因:" + throwable.getMessage());
        throwable.printStackTrace();
    }


    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 实现服务器主动推送
     */
    public void sendAsyncMessage(String message) throws IOException {
        this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized AtomicInteger getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        Socket01.onlineCount.getAndIncrement();
    }

    public static synchronized void subOnlineCount() {
        Socket01.onlineCount.getAndDecrement();
    }
}
