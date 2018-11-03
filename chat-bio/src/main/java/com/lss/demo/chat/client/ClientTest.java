package main.java.com.lss.demo.chat.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientTest {
    private static  int DEFAULT_PORT = 7878;
    public static void main(String[] args) {
        try {
            ChatClient chatClient = new ChatClient(new Socket("127.0.0.1",DEFAULT_PORT));
            ChatSend chatSend = new ChatSend(chatClient);
            ChatAccept chatAccept = new ChatAccept(chatClient);
            new Thread(chatSend).start();
            new Thread(chatAccept).start();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }
}
