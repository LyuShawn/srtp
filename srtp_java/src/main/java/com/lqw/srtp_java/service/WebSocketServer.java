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
    private MachineDao machineDao;
    private boolean polling=true;
    private boolean isRun=false;
    static Log log= LogFactory.getLog(WebSocketServer.class);
    private static int onlineCount=0;
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet
            = new CopyOnWriteArraySet<WebSocketServer>();
    private Session session;

    private Thread thread=new Thread(this);


    @OnOpen
    public void onOpen(Session session){
        this.session=session;
        webSocketSet.add(this);
        addOnlineCount();
        if(!isRun){
            thread.start();
            log.info("线程开始");
            isRun=true;
        }else{
            thread.notify();
        }
        log.info("当前连接数:"+getOnlineCount());

    }

    @OnClose
    public void onClose() throws InterruptedException {
        webSocketSet.remove(this);
        subOnlineCount();
        log.info("有一连接关闭！当前连接数:"+getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message,Session session){
        log.info("来个客户端的信息："+message);
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
        log.info(",推送内容："+message);
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
        String info = null;//开线程时正常发送
        try {
            info = JSON.toJSONString(machineDao.getLatestList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            sendMessage("初始");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(polling){
            if(onlineCount==0){
                log.info("当前无连接，线程阻塞");
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String newInfo = null;
            try {
                newInfo = JSON.toJSONString(machineDao.getLatestList());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if(!info.equals(newInfo)){      //如果数据变动，发送最新的数据

                log.info("数据变动"+!info.equals(newInfo));
                info=newInfo;
                try {
                    sendMessage("数据更新了");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(1000); //每一秒轮询一次
            } catch (InterruptedException e) {
                e.printStackTrace();
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
