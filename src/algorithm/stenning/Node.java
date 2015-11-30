package algorithm.stenning;

import algorithm.protocol.Topology;
import algorithm.protocol.UDPProtocol;
import algorithm.resource.TarryMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by cuongpham on 11/25/15. It is the implementation for Stenning algorithm
 */
public class Node extends Thread {
    private  String myIPAddress;
    private int myPort;
    protected DatagramSocket unicastSocket = null;
    Set<String> listNodes = new TreeSet<String>();
    private static Topology topology;
    private int next;
    private boolean ok;

    public static void setTopology(Topology topology) {
        Node.topology = topology;
    }

    //process's name is initialized as its port
    public Node(String myIPAddress, int myPort) throws IOException {
        this.myIPAddress = myIPAddress;
        this.myPort = myPort;
        unicastSocket = new DatagramSocket(myPort);
        unicastSocket.setSoTimeout(2000);
        next = 1;
        ok = true;
    }

    //Stenning algorithm
    public void run() {
        try {
            while (true) {
                if (myPort == topology.getMinimumPort() && ok) {
                    System.out.println("Process1: sending " + next + "th message");
                    UDPProtocol.sendUnicastObject(unicastSocket, new TarryMessage("message", "message" + next, next), (myIPAddress), myPort + 1);
                    ok = false;
                }
                try {
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    unicastSocket.receive(receivePacket);
                    ByteArrayInputStream bais = new ByteArrayInputStream(receiveData);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    TarryMessage message = (TarryMessage) ois.readObject();
                    if (message.getType().equals("ack")) {
                        if (next == message.getIndex()) {
                            ok = true;
                            next++;
                        }
                    }

                    if (message.getType().equals("message")) {
                        if (message.getIndex() == next) {
                            System.out.println("Process2: Deliever message: " + message.getContent());
                            UDPProtocol.sendUnicastObject(unicastSocket, new TarryMessage("ack", "", next), InetAddress.getByName(myIPAddress), receivePacket.getPort());
                            next++;
                        } else {
                            UDPProtocol.sendUnicastObject(unicastSocket, new TarryMessage("ack", "", next - 1), InetAddress.getByName(myIPAddress), receivePacket.getPort());
                        }
                        System.out.println("Process2: receives message");
                    }

                    if (next == 10) break;
                } catch (SocketTimeoutException e) {
                    //System.out.print("");
                    //System.out.println("Process1: no response after 2s, sending again the " + next + "th message");
                    UDPProtocol.sendUnicastObject(unicastSocket, new TarryMessage("message", "message" + next, next), InetAddress.getByName(myIPAddress), myPort + 1);
                }
            }
            UDPProtocol.close(unicastSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
