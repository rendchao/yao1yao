package com.tencent.wxcloudrun.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.model.Message;
import com.tencent.wxcloudrun.model.UserInfo;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@ServerEndpoint("/webSocket/{username}")
@Component
public class WebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineNum = new AtomicInteger();
    private static List<UserInfo> list = new ArrayList<>();
    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketServer对象。
    private static ConcurrentHashMap<String, Session> sessionPools = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, UserInfo> usrInfoMap = new ConcurrentHashMap<>();
    private static JSONObject jsonObject = new JSONObject();

    //发送消息
    public void sendMessage(Session session, String message) throws IOException {
        if (session != null) {
            synchronized (session) {
                System.out.println("发送数据：" + message);
                session.getBasicRemote().sendText(message);
            }
        }
    }

    //给指定用户发送信息
    public void sendInfo(String userName, String message) {
        Session session = sessionPools.get(userName);
        try {
            sendMessage(session, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 群发消息
    public void broadcast(String message) {
        for (Session session : sessionPools.values()) {
            try {
                sendMessage(session, message);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    //建立连接成功调用
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "username") String userName) {
        sessionPools.put(userName, session);
        if(!"erdfhsdyer".equals(userName)){

            Optional<UserInfo> stream =  list.stream().filter(user -> user.getNickName().equals(userName)).findFirst();
            if(stream !=null &&stream.isPresent() && stream.get() !=null){
                UserInfo userInfo = stream.get();
                userInfo.setTotalCount(12);

            }else{
                UserInfo userInfo = new UserInfo();
                userInfo.setTotalCount(12);

            }

        }else{
            addOnlineCount();
            // 广播上线消息
            Message msg = new Message();
            msg.setDate(new Date());
            msg.setTo("0");
            msg.setText(userName);
            broadcast(JSON.toJSONString(msg, true));
        }


        System.out.println(userName + "加入webSocket！当前人数为" + onlineNum);
    }

    //关闭连接时调用
    @OnClose
    public void onClose(@PathParam(value = "username") String userName) {

        sessionPools.remove(userName);
        subOnlineCount();
        System.out.println(userName + "断开   webSocket连接！当前人数为" + onlineNum+",剩余人数"+sessionPools.keySet().stream().collect(Collectors.toList()).toString()+"。");
        // 广播下线消息
        Message msg = new Message();
        msg.setDate(new Date());
        msg.setTo("-2");
        msg.setText(userName);
        broadcast(JSON.toJSONString(msg, true));
    }

    //收到客户端信息后，根据接收人的username把消息推下去或者群发
    // to=-1群发消息
    @OnMessage
    public void onMessage(String message) throws IOException {
        System.out.println("server get" + message);
        if (message.contains("nickName")) {
            UserInfo user = JSON.parseObject(message, UserInfo.class);
            if(user.getTotalCount()<12){
                user.setTotalCount(12);
            }
            UserInfo userInfo = usrInfoMap.get(user.getAvatarUrl());
            if (userInfo != null) {
                userInfo.setTotalCount(user.getTotalCount());
            } else {

                usrInfoMap.put(user.getAvatarUrl(),user);
                list.add(user);
            }
            String zhangsan = "{\"avatarUrl\":\"" +
                    "https://img-blog.csdnimg.cn/e9f21a8179be4e6db55535ccfa55d0e6.png\""+
                    ",\"nickName\":\"zhangsan\",\"totalCount\":" +
        ""+(user.getTotalCount()+2)+"}";
            user = JSON.parseObject(zhangsan, UserInfo.class);
            userInfo = usrInfoMap.get(user.getAvatarUrl());
            if (userInfo != null) {
                userInfo.setTotalCount(user.getTotalCount());
            } else {
                usrInfoMap.put(user.getAvatarUrl(),user);
                list.add(user);
            }









            list.sort(Comparator.comparing(UserInfo::getTotalCount).reversed());

            JSONObject json = new JSONObject();
            json.put("totalInfos",list);

            sendInfo("erdfhsdyer",JSON.toJSONString(json, true));
        } else {
            Message msg = JSON.parseObject(message, Message.class);
            msg.setDate(new Date());
            if (msg.getTo().equals("-1")) {
                broadcast(JSON.toJSONString(msg, true));
            } else {
                sendInfo(msg.getTo(), JSON.toJSONString(msg, true));
            }
        }

    }

    //错误时调用
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("发生错误");
        throwable.printStackTrace();
    }

    public static void addOnlineCount() {
        onlineNum.incrementAndGet();
    }

    public static void subOnlineCount() {
        onlineNum.decrementAndGet();
    }

    public static AtomicInteger getOnlineNumber() {
        return onlineNum;
    }

    public static ConcurrentHashMap<String, Session> getSessionPools() {
        return sessionPools;
    }


}
