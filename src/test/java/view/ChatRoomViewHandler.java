package view;


import core.view.AbstractViewHandler;

public class ChatRoomViewHandler extends AbstractViewHandler<ChatRoomView, ChatRoomView> {
    @Override
    public boolean process() {
        ChatRoomView viewParam = viewParam();
        ChatRoomView viewData = viewData();
        viewData.chat = viewParam.chat;
        return true;
    }
}
