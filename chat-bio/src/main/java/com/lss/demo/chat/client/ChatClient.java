package main.java.com.lss.demo.chat.client;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class ChatClient {


    private Socket socket = null;
    public ChatClient(Socket socket) {
            this.socket = socket;
    }

    /**
     * 客户端发送
     */
    public void send(){
        Scanner scanner = new Scanner(System.in);
        String meesage = null;
        PrintWriter out = null;

        try{
            out = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()),true);
            System.out.println("我是： "+Thread.currentThread().getName()+"发送消息： "+ meesage);
            while ((meesage = scanner.nextLine())!=null){
                out.println(meesage);
            }

        }catch (IOException e){
            e.getMessage();
        }finally {

        }

    }

    /**
     * 客户端接受
     */
    public void accept(){
        System.out.println("");
        BufferedReader in = null;
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = "";
            while ((line = in.readLine())!=null){
                System.out.println("接受到消息："+line);
            }
        }catch (IOException e){
            e.getMessage();
        }


    }
}
