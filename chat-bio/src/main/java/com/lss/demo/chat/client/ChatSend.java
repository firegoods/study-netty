package main.java.com.lss.demo.chat.client;

public class ChatSend implements  Runnable{

    private ChatClient chatClient = null;

    public ChatSend(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void run() {
        chatClient.send();
    }
}
