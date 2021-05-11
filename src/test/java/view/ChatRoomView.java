package view;

import core.view.ViewData;
import core.view.ViewParam;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomView implements ViewData, ViewParam {

    public List<String> chat = new ArrayList<>();

    public List<String> getChat(){
        return chat;
    }

    public void add(String msg){
        chat.add(msg);
    }
}
