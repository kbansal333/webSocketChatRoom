package com.example.chatRoom;

import com.example.chatRoom.model.Message;
import com.google.gson.Gson;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {
    private static Gson gson = new Gson();
    @Override
    public String encode(Message message){
        return gson.toJson(message);
    }

    @Override
    public void destroy(){}

    @Override
    public void init(EndpointConfig ec){}
}
