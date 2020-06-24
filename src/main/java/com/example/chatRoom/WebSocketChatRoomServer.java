package com.example.chatRoom;

import com.example.chatRoom.model.Message;
import com.google.gson.Gson;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/chat/{username}",encoders = {MessageEncoder.class})
public class WebSocketChatRoomServer {
    private static Gson gson = new Gson();
    private static Map<String, Session> sessionIdSessionMap = new ConcurrentHashMap<>();
    private static Map<String, String> sessionIdUsernameMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        sessionIdSessionMap.put(session.getId(),session);
        sessionIdUsernameMap.put(session.getId(),username);
        sendMessage(new Message("Connected!",username,sessionIdSessionMap.size(),Constants.VIEWABLE_MESSAGE));

    }

    @OnMessage
    public void onMessage(Session session, String jsonStr) {
        Message message = gson.fromJson(jsonStr,Message.class);
        message.setSender(sessionIdUsernameMap.get(session.getId()));
        message.setOnlineCount(sessionIdSessionMap.size());
        message.setType(Constants.VIEWABLE_MESSAGE);
        sendMessage(message);
    }

    @OnClose
    public void onClose(Session session) {
        sendMessage(new Message("Disconnected!",sessionIdUsernameMap.get(session.getId()),sessionIdSessionMap.size()-1,Constants.VIEWABLE_MESSAGE));
        sessionIdUsernameMap.remove(session.getId());
        sessionIdSessionMap.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }


    private static void sendMessage(Message message)  {
        for(Session session : sessionIdSessionMap.values()){
            synchronized (session){
                try{
                    if(session.isOpen()){
                        session.getBasicRemote().sendObject(message);
                    }
                    else{
                        sessionIdUsernameMap.remove(session.getId());
                        sessionIdSessionMap.remove(session.getId());
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

   /* @Scheduled(fixedDelay = 5000)
    private static void removeClosedSessions(){
        Map<String,Session> tempSessionIdSessionMap = new ConcurrentHashMap<>();
        Map<String,String> tempSessionIdUsernameMap = new ConcurrentHashMap<>();
        for(String sessionId : sessionIdSessionMap.keySet()){
            if(sessionIdSessionMap.get(sessionId).isOpen()){
                tempSessionIdSessionMap.put(sessionId,sessionIdSessionMap.get(sessionId));
                tempSessionIdUsernameMap.put(sessionId,sessionIdUsernameMap.get(sessionId));
            }
        }
        sessionIdSessionMap = tempSessionIdSessionMap;
        sessionIdUsernameMap = tempSessionIdUsernameMap;
        sendMessage(new Message("","",sessionIdSessionMap.size(),2));
    }*/
}
