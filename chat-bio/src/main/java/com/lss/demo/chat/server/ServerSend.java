package main.java.com.lss.demo.chat.server;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerSend implements Runnable {
    private List<Socket> sockets;
    private Socket socket;
    public ServerSend(List<Socket> sockets,Socket socket) {
        this.sockets = sockets;
        this.socket = socket;
    }

    @Override
    public void run() {

            BufferedReader in = null;
            PrintWriter out = null;
            try {
                in = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream())));
                String line = "";
                while((line = in.readLine())!=null){
                    System.out.println("服务端收到消息： "+ line);
                    for (Socket s:sockets){
                        if(socket==s){continue;}
                        out = new PrintWriter(new BufferedOutputStream(s.getOutputStream()),true);
                        System.out.println("服务端发送消息： "+ line);
                        out.println(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}
