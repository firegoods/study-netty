package main.java.com.lss.demo.buffer;

import java.nio.ByteBuffer;

/**
 * @ClassNmame Buffers
 * @Description 通道注册的时候的附件对象
 * @Author Administrator
 * @Date 2018/11/4 13:12
 * @Version 1.0
 **/
public class Buffers {

    ByteBuffer readBuffer;
    ByteBuffer writeBuffer;

    public Buffers(int  readCapacity, int writeCapacity) {
        this.readBuffer = ByteBuffer.allocate(readCapacity);
        this.writeBuffer = ByteBuffer.allocate(writeCapacity);
    }

    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }


    public ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }


}
