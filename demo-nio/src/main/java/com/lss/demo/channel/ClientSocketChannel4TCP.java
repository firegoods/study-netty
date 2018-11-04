package main.java.com.lss.demo.channel;

import main.java.com.lss.demo.buffer.Buffers;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * @ClassNmame ClientSocketChannel4TCP
 * @Description 客户端发送消息
 * @Author Administrator
 * @Date 2018/11/4 13:53
 * @Version 1.0
 **/
public class ClientSocketChannel4TCP  implements  Runnable{

    private String threadName;
    private Random random = new Random();
    private InetSocketAddress remoteAddress;

    public ClientSocketChannel4TCP(String threadName, InetSocketAddress remoteAddress) {
        this.threadName = threadName;
        this.remoteAddress = remoteAddress;
    }

    @Override
    public void run() {

        Charset utf8 = Charset.forName("UTF-8");
        Selector selector = null;
        /*创建一个通道*/
        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);

             selector = Selector.open();

            int interestSet = SelectionKey.OP_READ| SelectionKey.OP_WRITE;

            socketChannel.register(selector, interestSet, new Buffers(100,100));

            /**发起连接*/
            socketChannel.connect(remoteAddress);

            /*等待3次握手*/
            while(!socketChannel.finishConnect()){
                ;
            }
            System.out.println(threadName + " " + "finished connection");




        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("client connect failed");
            return;
        }

        try{
            int i = 1;
            while(!Thread.currentThread().isInterrupted()){
                selector.select();

                Set<SelectionKey> keySet = selector.selectedKeys();
                Iterator<SelectionKey> keys = keySet.iterator();

                while(keys.hasNext()){
                    SelectionKey key = keys.next();
                    //删除下面需要处理的key
                    keys.remove();

                    /*获取缓冲区*/
                    Buffers buffers = (Buffers)key.attachment();
                    ByteBuffer readBuffer = buffers.getReadBuffer();
                    ByteBuffer writeBuffer = buffers.getWriteBuffer();

                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    //读取服务端的数据
                    if(key.isReadable()){
                        socketChannel.read(readBuffer);
                        readBuffer.flip();

                        CharBuffer charBuffer = utf8.decode(readBuffer);
                        System.out.println(charBuffer.array());
                        readBuffer.clear();

                    }
                    //发送数据
                    if(key.isWritable()){
                        writeBuffer.put((threadName + " " +i).getBytes(utf8));
                        writeBuffer.flip();
                        socketChannel.write(writeBuffer);
                        writeBuffer.clear();
                        i++;
                    }


                }
                /*随机休息N秒*/
                Thread.sleep(1000+random.nextInt(1000));

            }
        }catch (InterruptedException e){
            e.getStackTrace();
            System.out.println(threadName + " is interrupted");
        }catch (IOException e){
            e.getStackTrace();
            System.out.println(threadName + "selector a connector failed");
        }finally {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(threadName + "close selector failed");

            }finally {
                System.out.println(threadName + "selector  close");
            }
        }





    }

    public static void main(String[] args) throws InterruptedException {
        InetSocketAddress remoteAddress = new InetSocketAddress("127.0.0.1",8080);

        Thread t1 = new Thread(new ClientSocketChannel4TCP("thread001",remoteAddress));
        Thread t2 = new Thread(new ClientSocketChannel4TCP("thread002",remoteAddress));
        Thread t3 = new Thread(new ClientSocketChannel4TCP("thread003",remoteAddress));
        Thread t4 = new Thread(new ClientSocketChannel4TCP("thread004",remoteAddress));

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        Thread.sleep(5000);
        t1.interrupt();
        t2.interrupt();
        t3.interrupt();
        t4.interrupt();



    }
}
