package unicast.udp;

/**
 * Created by cuongpham on 11/21/15.
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class QuoteServerThread extends Thread {
    protected DatagramSocket socket = null;
    protected BufferedReader in = null;
    protected int moreQuotes = 0;
    protected String input = null;

    public QuoteServerThread() throws IOException {
        this("MulticastServerThread");
    }

    public QuoteServerThread(String name) throws IOException {
        super(name);
        socket = new DatagramSocket(4445);
        input = "abcdef";
    }

    public void run() {
        while (moreQuotes <= 10) {
            try {
                byte[] buf = new byte[256];

                //receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                //figure out response
                String dString = new String(packet.getData()).trim();
                System.out.println(dString);
                buf = dString.getBytes();

                //send the response to the client at "address" and "port"
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);
                moreQuotes++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}
