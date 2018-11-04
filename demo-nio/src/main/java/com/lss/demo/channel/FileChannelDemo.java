package main.java.com.lss.demo.channel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @ClassNmame FileChannelDemo
 * @Description  文件通道
 * @Author Administrator
 * @Date 2018/11/4 14:39
 * @Version 1.0
 **/
public class FileChannelDemo {

    public static void main(String[] args) {
        Charset utf8 = Charset.forName("UTF-8");
        try{
            File file = new File("c:/nio.data");
            if(file.exists()){
                file.createNewFile();
            }

            /*文件流与文件通道关联*/
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            FileChannel fileChannel = fileOutputStream.getChannel();

            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            byteBuffer.put("hello world my love\r\n".getBytes(utf8));
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
            byteBuffer.clear();

            byteBuffer.put("你好\r\n".getBytes(utf8));
            byteBuffer.flip();

            fileChannel.write(byteBuffer);
            byteBuffer.clear();

            fileChannel.close();
            fileOutputStream.close();


        }catch (IOException e){
            System.out.println(e);
        }finally {

        }


        try{
            Path path = Paths.get("c:/nio.data");
            //建立文件通道
            FileChannel fileChannel = FileChannel.open(path);

            ByteBuffer byteBuffer = ByteBuffer.allocate((int)fileChannel.size()+1);

            fileChannel.read(byteBuffer);
            byteBuffer.flip();
            System.out.println(utf8.decode(byteBuffer));
            byteBuffer.clear();
            fileChannel.close();


        }catch (IOException e){

        }finally {

        }

    }

}
