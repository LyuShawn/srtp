package com.lqw.srtp_java.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket/{sid}")
@Component
public class WebSocketServer {
    static Log log= LogFactory.getLog(WebSocketServer.class);
    private static int onlineCount=0;
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet
            = new CopyOnWriteArraySet<WebSocketServer>();
    private Session session;
    private String sid="";

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid){
        this.session=session;
        webSocketSet.add(this);
        addOnlineCount();
        log.info("有新窗口开始监听："+sid+",当前在线人数为"+getOnlineCount());
        this.sid=sid;
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        subOnlineCount();
        log.info("有一连接关闭！当前在线人数为"+getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message,Session session){
        log.info("收到来自窗口"+sid+"的信息："+message);
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Session session,Throwable error){
        log.error("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
    public static void sendInfo(String message,@PathParam("sid") String sid){
        log.info("推送消息到窗口"+sid+",推送内容："+message);
        for (WebSocketServer item : webSocketSet) {
            try{
                if(sid==null){
                    item.sendMessage(message);
                }else if(item.sid.equals(sid)){
                    item.sendMessage(message);
                }
            }catch(IOException e){
                continue;
            }
        }
    }
    public static synchronized int getOnlineCount(){
        return onlineCount;
    }
    public static synchronized void addOnlineCount(){
        WebSocketServer.onlineCount++;
    }
    public static synchronized void subOnlineCount(){
        WebSocketServer.onlineCount--;
    }

}
