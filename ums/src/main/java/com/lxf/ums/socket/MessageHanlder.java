package com.lxf.ums.socket;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chunhao.guo
 * @since 2020/11/27
 */
@Service
public class MessageHanlder {

    private static Logger log = LoggerFactory.getLogger(MessageHanlder.class);
    private static Map<String, String> deviceToSessionMap = new HashMap<>();
    private static Map<String, String> sessionIdToDeviceMap = new HashMap<>();

    private static Map<String,String> resultCache=new HashMap<>();
    //接收消息处理方法
    public void process(String msg,String sessionId) {
        try {
                log.info("接收到设备 :" + sessionIdToDeviceMap.get(sessionId) + " 的消息：" + msg);
                String deviceId = sessionIdToDeviceMap.get(sessionId);
                JSONObject jsonObject=JSONObject.parseObject(msg);

                //做心跳和应答
                Integer opcode = jsonObject.getInteger("opcode");
                String msgId = jsonObject.getString("msgId");
                JSONObject result = new JSONObject();

                //是应答报文不针对应答包再做应答
                if (opcode.equals(500106)) return;

                JSONObject ackJson = new JSONObject();
                ackJson.put("opcode", 500106);
                ackJson.put("msgId", jsonObject.getString("msgId"));
                JSONObject ackData = new JSONObject();
                ackData.put("opcode", jsonObject.getInteger("opcode"));
                ackJson.put("data", ackData);
                ackData.put("time",System.currentTimeMillis());
                sendMsg(deviceId,ackJson.toJSONString());

                if(opcode.equals(500105)) return;
                //进行消息转发
                if (deviceId.equals("9527")) {
                    //获取9528的seesionId
                    String dstSessionId=deviceToSessionMap.get("2c8ac20fa02f4912aa06f9535cddfa2caas");
                    log.info("2c8ac20fa02f4912aa06f9535cddfa2caas 的连接号 :"+dstSessionId);
                    //发送到9528去
                    WebSocketServer.sendMsg(jsonObject.toJSONString(),dstSessionId);
                } else {
                    //发送到9527去
                    sendMsg("9527", msg);
                }
        }catch (Exception e)
        {
            log.info(e.getMessage());
        }
    }

    //处理登陆流程做设备注册
    public boolean deLogin(String msg,String sessionId) {
        log.info("接收到的登陆消息 :" + msg);
        String deviceId=msg;

        String srcSessionId=deviceToSessionMap.get(deviceId);
        //清除原来的数据
        deviceToSessionMap.remove(deviceId);
        sessionIdToDeviceMap.remove(srcSessionId);

        //重新绑定关系
        deviceToSessionMap.put(deviceId,sessionId);
        sessionIdToDeviceMap.put(sessionId,deviceId);
        return true;
    }

    //处理连接断开的回收数据处理
    public void sessionClose(String sessionId) {
        String deviceId = sessionIdToDeviceMap.get(sessionId);
        deviceToSessionMap.remove(deviceId);
        sessionIdToDeviceMap.remove(sessionId);
        //TODO: 通知上层进行设备状态变更
    }

    //进行消息发送
    public String SySendMsg(String deviceId,String msg)
    {
        Integer time=0;
        String sessionId=deviceToSessionMap.get(deviceId);
        try {
            WebSocketServer.sendMsg(msg,sessionId);
            //需要接收到返回数据才进行返回
            while (true) {
                Thread.sleep(1000);
                //获取缓存消息
                String result=resultCache.get(deviceId);
                if(result!=null) {
                    resultCache.remove(deviceId);
                    return result;
                }else {
                    //尝试获取3次
                    if(time>3)
                    {
                        return null;
                    }
                    time=time+1;
                }
            }
        }catch (InterruptedException e)
        {
            log.info("等待超时 ");
        }
        catch (IOException e)
        {
            log.info("发送消息失败 :"+msg+",设备id:"+deviceId);
        }
        return null;
    }

    //进行消息发送
    public void sendMsg(String deviceId,String msg)
    {
        String sessionId=deviceToSessionMap.get(deviceId);
        try {
            WebSocketServer.sendMsg(msg,sessionId);
            log.info("发送消息成功 :"+msg+",设备id:"+deviceId);
        }catch (IOException e)
        {
            log.info("发送消息失败 :"+msg+",设备id:"+deviceId);
        }
    }
}
