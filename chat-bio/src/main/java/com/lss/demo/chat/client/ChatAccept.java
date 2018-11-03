package main.java.com.lss.demo.chat.client;

public class ChatAccept implements Runnable {

    private ChatClient chatClient = null;

    public ChatAccept(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public void run() {
        chatClient.accept();
    }
}
