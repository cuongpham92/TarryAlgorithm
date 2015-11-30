package algorithm.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by cuongpham on 11/25/15.
 */
public class UDPProtocol {

    public static void connection(DatagramSocket unicastSocket, int port) {
        try {
            unicastSocket = new DatagramSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendUnicastString(DatagramSocket unicastSocket, String message, int port) {
        if (unicastSocket != null) {
            try {
                InetAddress address = InetAddress.getByName("localhost");
                byte[] buf = new byte[256];
                buf = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                unicastSocket.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static DatagramPacket receiveUnicast(DatagramSocket unicastSocket) {
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            unicastSocket.receive(receivePacket);
            return receivePacket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sendUnicastObject(DatagramSocket unicastSocket, Object obj, InetAddress ipAddress, int port) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            byte[] sendData = baos.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
            unicastSocket.send(sendPacket);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void close(DatagramSocket unicastSocket) {
        if (unicastSocket != null) {
            try {
                unicastSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
