package unicast.udp;

import java.io.IOException;
import java.net.*;

/**
 * Created by cuongpham on 11/21/15.
 */
public class QuoteClient {
    DatagramSocket mySocket = null;
    int port = 4447;

    public void connection() {
        try {
            mySocket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.err.println(e);
        }
    }

    public void send(String message) {
        if (mySocket != null) {
            byte[] buffer = new byte[256];
            try {
                InetAddress address = InetAddress.getByName("localhost");
                buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 4445);
                mySocket.send(packet);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String receive() {
        if (mySocket != null) {
            byte[] receiveData = new byte[1024]; // bo dem nhan dl
            try {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                mySocket.receive(receivePacket);
                return new String(receivePacket.getData()).trim();
            } catch (SocketException e) {
                System.err.println(e);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        return null;
    }

    public void close() {
        if (mySocket != null) {
            try {
                mySocket.close();
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        // get a datagram socket
        QuoteClient client = new QuoteClient();
        client.connection();
        client.send("abc");
        String result = client.receive();
        System.out.println(result);
        client.close();
    }

}
