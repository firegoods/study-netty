package main.java.com.lss.demo.channel;

import main.java.com.lss.demo.buffer.Buffers;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @ClassNmame ServiceSocketChannel4TCP
 * @Description  this is serversocketchannel for tcp 服务端接受到消息并且打印
 * @Author Administrator
 * @Date 2018/11/4 12:39
 * @Version 1.0
 **/
public class ServerSocketChannel4TCP implements Runnable{

    private InetSocketAddress socketAddress;

    public ServerSocketChannel4TCP(int  port) {
        this.socketAddress = new InetSocketAddress(port);
    }

    @Override
    public void run() {
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        try {
            //创建服务端通道
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);

            //创建选择器
            selector = Selector.open();
            //The maximum number of pending connections is 50
            serverSocketChannel.bind(this.socketAddress,50);
            //注册socket-accept事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);



        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("server start failed!");
            return;
        }

        System.out.println("server start with address " + socketAddress);
        try {
            while(!Thread.currentThread().isInterrupted()){
                //Selects a set of keys whose corresponding channels are ready for I/O operations.


                    int op = selector.select();

                    if(op == 0){
                        continue;
                    }

                    Set<SelectionKey> keys =  selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    Charset utf8 = Charset.forName("UTF-8");
                    SelectionKey key = null;

                    while(iterator.hasNext()){
                        key = iterator.next();
                        iterator.remove();
                        try{
                            if(key.isAcceptable()){
                                SocketChannel socketChannel = serverSocketChannel.accept();
                                socketChannel.configureBlocking(false);

                                int interestKey = SelectionKey.OP_READ;
                                socketChannel.register(selector,interestKey,new Buffers(100,100));
                                System.out.println("accept from "+socketChannel.getRemoteAddress());
                            }

                            /*Tests whether this key's channel is ready for reading.*/
                            if(key.isReadable()){
                                Buffers buffers = (Buffers) key.attachment();
                                ByteBuffer readBuffer = buffers.getReadBuffer();
                                ByteBuffer writeBuffer = buffers.getWriteBuffer();

                                //获取通道
                                SocketChannel socketChannel = (SocketChannel) key.channel();
                                socketChannel.read(readBuffer);
                                readBuffer.flip();
                                CharBuffer charBuffer = utf8.decode(readBuffer);
                                System.out.println(charBuffer.array());

                                /*Rewinds this buffer.  The position is set to zero and the mark is  discarded.*/
                                readBuffer.rewind();//倒回

                                writeBuffer.put("echo from service".getBytes(utf8));
                                writeBuffer.put(readBuffer);

                                readBuffer.clear();


                                /*设置通道写事件*/
                                key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                                //socketChannel.register(selector,SelectionKey.OP_WRITE);

                            }
                            if(key.isWritable()) {
                                Buffers buffers = (Buffers) key.attachment();

                                ByteBuffer writeBuffer = buffers.getWriteBuffer();
                                writeBuffer.flip();

                                SocketChannel socketChannel = (SocketChannel) key.channel();
                                int len = 0;
                                while (writeBuffer.hasRemaining()) {
                                    len = socketChannel.write(writeBuffer);

                                    if (len == 0) {
                                        break;
                                    }
                                }
                                writeBuffer.compact();
                                /*说明数据全部写入，取消write事件*/
                                if (len != 0) {
                                    key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));


                                }
                            }
                        }catch (IOException e){
                            e.getStackTrace();
                            /*如果客户端发生异常 送selector中移除这个key*/
                            key.cancel();
                            key.channel().close();
                        }


                        }
                    }



         }  catch (InterruptedIOException e){
            System.out.println("server thread is interrupted");
         }catch (IOException e) {
            e.printStackTrace();
            System.out.println("server thread selector error");

         }finally {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("selector close failed");
            }finally {
                System.out.println("server close");
            }

        }

    }

    public static void main(String[] args) throws InterruptedException {
        ServerSocketChannel4TCP serverSocketChannel4TCP = new ServerSocketChannel4TCP(8080);
        Thread thread = new Thread(serverSocketChannel4TCP);

        thread.start();

        Thread.sleep(50000);
        thread.interrupt();
    }
}
