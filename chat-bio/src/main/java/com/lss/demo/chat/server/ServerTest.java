package main.java.com.lss.demo.chat.server;

public class ServerTest {

    public static void main(String[] args) {
        try {
            new Server().start();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        /**
         * 第1个异常是java.net.BindException:Address already in use: JVM_Bind。该异常发生在服务器端进行new ServerSocket(port)（port是一个0，65536的整型值）操作时。异常的原因是以为与port一样的一个端口已经被启动，并进行监听。此时用netstat –an命令，可以看到一个Listending状态的端口。只需要找一个没有被占用的端口就能解决这个问题。
         * 第2个异常是java.net.ConnectException: Connection refused: connect。该异常发生在客户端进行new Socket(ip, port)操作时，该异常发生的原因是或者具有ip地址的机器不能找到（也就是说从当前机器不存在到指定ip路由），或者是该ip存在，但找不到指定的端口进行监听。出现该问题，首先检查客户端的ip和port是否写错了，如果正确则从客户端ping一下服务器看是否能ping通，如果能ping通（服务服务器端把ping禁掉则需要另外的办法），则看在服务器端的监听指定端口的程序是否启动，这个肯定能解决这个问题。
         * 第3个异常是java.net.SocketException: Socket is closed，该异常在客户端和服务器均可能发生。异常的原因是己方主动关闭了连接后（调用了Socket的close方法）再对网络连接进行读写操作。
         * 第4个异常是java.net.SocketException: （Connection reset或者Connect reset by peer:Socket write error）。该异常在客户端和服务器端均有可能发生，引起该异常的原因有两个，第一个就是如果一端的Socket被关闭（或主动关闭或者因为异常退出而引起的关闭），另一端仍发送数据，发送的第一个数据包引发该异常(Connect reset by peer)。另一个是一端退出，但退出时并未关闭该连接，另一端如果在从连接中读数据则抛出该异常（Connection reset）。简单的说就是在连接断开后的读和写操作引起的。
         * 第5个异常是java.net.SocketException: Broken pipe。该异常在客户端和服务器均有可能发生。在第4个异常的第一种情况中（也就是抛出SocketExcepton:Connect reset by peer:Socket write error后），如果再继续写数据则抛出该异常。前两个异常的解决方法是首先确保程序退出前关闭所有的网络连接，其次是要检测对方的关闭连接操作，发现对方关闭连接后自己也要关闭该连接。
         */
    }
}
