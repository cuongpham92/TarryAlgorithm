package algorithm.protocol;

/**
 * Created by cuongpham on 11/21/15.
 */

import java.io.IOException;
import java.net.*;
import java.util.Set;
import java.util.TreeSet;

public class NodeThread extends Thread {
    protected DatagramSocket unicastSocket = null;
    private MulticastSocket multicastSocket = null;
    private InetAddress group = null;
    private boolean initialize = false;
    Set<String> listNodes = new TreeSet<String>();




    public void connection() {
        try {
            unicastSocket = new DatagramSocket();
            group = InetAddress.getByName("224.0.0.1");
            multicastSocket = new MulticastSocket(4446);
            multicastSocket.joinGroup(group);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message) {
        if (unicastSocket != null) {
            byte[] buffer = new byte[256];
            try {
                buffer = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 4446);
                unicastSocket.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void unicast(String message, InetAddress senderAddress, int senderPort) {
        if (unicastSocket != null) {
            try {
                byte[] buf = new byte[256];
                buf = message.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, senderAddress, senderPort);
                unicastSocket.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public DatagramPacket receiveBroadcast() {
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            multicastSocket.receive(receivePacket);
            return receivePacket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DatagramPacket receiveUnicast() {
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

    public void close() {
        if (unicastSocket != null) {
            try {
                unicastSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stenning() {

    }
    //Tarry algorithm
    public void run() {
        if (initialize) {
            broadcast("start");
            listNodes.add(getName());
        }
        while (true) {

            //if not the initialized node
            if (!initialize) {
                DatagramPacket receivedPacketBroadcast = receiveBroadcast();
                String result = new String(receivedPacketBroadcast.getData()).trim();

                if (result.equalsIgnoreCase("start") && !initialize) {
                    unicast(getName(), receivedPacketBroadcast.getAddress(), receivedPacketBroadcast.getPort());
                    System.out.println(getName() + ": sent");
                    break;
                }
            }

            //if it is the initialized node
            if (initialize) {
                DatagramPacket receivedPacketUnicast = receiveUnicast();
                String result2 = new String(receivedPacketUnicast.getData()).trim();
                listNodes.add(result2);
                System.out.println(result2);

                if (listNodes.size() == 4) {
                    for (String s : listNodes)
                        System.out.print(s + " ");
                    break;
                }
            }
        }
        close();
    }
}
