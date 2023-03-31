package com.lxf.ums.socket;


import com.lxf.ums.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/{param}")
@Component
public class WebSocketServer {
    private static Logger log = LoggerFactory.getLogger(WebSocketServer.class);
    private static Map<String,Session> sessionToSessionIdMap= new HashMap();

    private static MessageHanlder messageHanlder;

    @PostConstruct
    public void init() {
        log.info("websocket 加载");
        try {
            messageHanlder = (MessageHanlder) SpringUtil.getBean("messageHanlder");
        }catch (Exception e)
        {
            log.info(e.getMessage());
        }
    }


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("param") String param) {
        //TODO: 这里做IP白名单限制
        InetSocketAddress addr= WebsocketUtil.getRemoteAddress(session);
        if(!checkIp(addr)) {
            try {
                session.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            return;
        }
        //进行登陆消息鉴权操作
        if(!messageHanlder.deLogin(param,session.getId())) {
            try {
                    session.close();
            }catch (IOException e) {
                    log.error(e.getMessage());
            }
            return;
        }
            sessionToSessionIdMap.put(session.getId(),session);
            sendMessage(session, "连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        sessionToSessionIdMap.remove(session.getId());
        log.info("移除地面站客户端连接");
        //TODO：怎么告知上层,当前设备的连接已经中断了.去更改设备的当前状态
        messageHanlder.sessionClose(session.getId());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息：{}",message);
        //TODO:引入一个消息处理handler用于处理接收到消息数据(设备登陆消息是否需要在这里进行处理，因为涉及到设备绑定session的操作)
        messageHanlder.process(message,session.getId());
    }

    /**
     * 出现错误
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}",error.getMessage(),session.getId());
        error.printStackTrace();
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     * @param session
     * @param message
     */
    public static void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 指定Session发送消息
     * @param sessionId
     * @param message
     * @throws IOException
     */
    public static void sendMsg(String message,String sessionId) throws IOException {
        Session session = sessionToSessionIdMap.get(sessionId);
        if(session!=null){
            log.info("发送消息数据: "+message);
            sendMessage(session, message);
        }
        else{
            log.warn("没有找到你指定ID的会话：{}",session.getId());
        }
    }

    //私有函數
    private boolean checkIp(InetSocketAddress addr)
    {
        log.info("请求方 IP:"+addr.getAddress().getHostAddress());
        return true;
    }
}
