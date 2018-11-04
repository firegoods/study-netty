package main.java.com.lss.demo.buffer;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @ClassNmame BufferDemo
 * @Description 基于buffer的编码与解码
 * @Author Administrator
 * @Date 2018/11/4 12:04
 * @Version 1.0
 **/
public class BufferDemo {

    /**
     * 解码
     * @param bytesUTF8
     */
    public  static char[]  decode(byte[] bytesUTF8){
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        Charset charsetUFT8 = Charset.forName("UTF-8");
        byteBuffer.put(bytesUTF8);
        //The limit is set to the current position and then the position is set to zero.
        byteBuffer.flip();//翻转

        /*对bytebuffer的字节进行解码*/
        CharBuffer charBuffer = charsetUFT8.decode(byteBuffer);

        char[] chars = Arrays.copyOf(charBuffer.array(), charBuffer.limit());
        System.out.println(chars);
        return chars;

    }

    /**
     * 编码
     * @param str
     */
    public static byte[]  encode(String str){
        /*分配一个heap内存大小为100字符*/
        CharBuffer charBuffer = CharBuffer.allocate(100);
        charBuffer.append(str);
        //The limit is set to the current position and then the position is set to zero.
        charBuffer.flip();

        //字符集
        Charset charsetUFT8 = Charset.forName("UTF-8");
        /*进行utf-8的编码*/
        ByteBuffer byteBuffer = charsetUFT8.encode(charBuffer);


        byte[] bytes = Arrays.copyOf(byteBuffer.array(),byteBuffer.limit());
        System.out.println(Arrays.toString(bytes));
        return bytes;


    }


    public static void main(String[] args) {
        //编码
        byte[] bytes = BufferDemo.encode("南京咕泡学员");
        //解码
        BufferDemo.decode(bytes);
    }
}
