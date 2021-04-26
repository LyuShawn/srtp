package com.lqw.srtp_java.service;

import com.alibaba.fastjson.JSON;
import com.lqw.srtp_java.dao.MachineDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/websocket/machine_info")
@Component
public class WebSocketServer implements Runnable{
    private static String info;
    private MachineDao machineDao;
    private static boolean polling=true;
    private static boolean isRun=false;
    static Log log= LogFactory.getLog(WebSocketServer.class);
    private static int onlineCount=0;
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet
            = new CopyOnWriteArraySet<WebSocketServer>();
    private Session session;

    private Thread thread=new Thread(this);


    @OnOpen
    synchronized public void onOpen(Session session) throws IOException {
        this.session=session;
        webSocketSet.add(this);
        addOnlineCount();
        if(!isRun){
            thread.start();
            isRun=true;
            log.info("开始新连接，线程开始");
        }else
            sendMessage(info);//连接开始，发送初始数据
        log.info("当前连接数:"+getOnlineCount());
    }

    @OnClose
    public void onClose() throws InterruptedException {
        webSocketSet.remove(this);
        subOnlineCount();
        log.info("有一连接关闭！当前连接数:"+getOnlineCount());
        //polling=false;
    }

    @OnMessage
    public void onMessage(String message,Session session){
//        log.info("来个客户端的信息："+message);
//        for (WebSocketServer item : webSocketSet) {
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @OnError
    public void onError(Session session,Throwable error){
        log.error("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }
    public static void sendInfo(String message){
        log.info("推送内容："+message);
        for (WebSocketServer item : webSocketSet) {
            try{
                item.sendMessage(message);
            }catch(IOException e){
                continue;
            }
        }
    }

    @Override
    public synchronized void run() {
        machineDao=new MachineDao();
        //String info = null;//开线程时正常发送
        try {
            info = JSON.toJSONString(machineDao.getLatestList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            sendMessage(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(polling){

            String newInfo = null;
            try {
                newInfo = JSON.toJSONString(machineDao.getLatestList());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if(!info.equals(newInfo)){      //如果数据变动，发送最新的数据

                log.info("数据变动");
                info=newInfo;
                for (WebSocketServer item : webSocketSet) {
                    try {
                        item.sendMessage(newInfo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                Thread.sleep(1000); //每一秒轮询一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        log.info("线程结束");
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
