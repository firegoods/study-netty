package main.java.com.lss.demo.chat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 基于java的bio实现的群聊
 */
public class Server {

    public static int DEFAULT_PORT=7878;

    private static ServerSocket serverSocket = null;

    private static List<Socket> sockets;



    public  void start(){
        start(DEFAULT_PORT);
    }

    private  synchronized void  start(int defaultPort) {

        try{
            if(serverSocket==null){
                serverSocket = new ServerSocket(defaultPort);

            }
            if(sockets ==null){
                sockets = new ArrayList<>();
            }
            System.out.println("服务端启动完成");
            while (true){

                Socket socket = serverSocket.accept();
                sockets.add(socket);
                //处理业务
                new Thread(new ServerSend(sockets,socket)).start();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());

        }finally {


        }

    }

}
