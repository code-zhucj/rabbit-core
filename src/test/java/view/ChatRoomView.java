package view;

import core.view.ViewData;
import core.view.ViewHandler;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomView extends ViewData implements ViewHandler {

    private List<String> chat = new ArrayList<>();

    public List<String> getChat() {
        return super.collector.get("chat");
    }

    public void setChat(List<String> chat) {
        this.chat = chat;
    }

    @Override
    public void process() {

    }
}
