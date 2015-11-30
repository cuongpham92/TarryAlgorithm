package algorithm.stenning;

import algorithm.protocol.Topology;
import algorithm.protocol.UDPProtocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by cuongpham on 11/25/15. It is the implementation for Stenning algorithm
 */
public class Node extends Thread {
    protected DatagramSocket unicastSocket = null;
    Set<String> listNodes = new TreeSet<String>();
    private int myPort;
    private static Topology topology;
    private int numOfMessage;
    private int next;
    private boolean ok;

    public static void setTopology(Topology topology) {
        Node.topology = topology;
    }

    //process's name is initialized as its port
    public Node(String name) throws IOException {
        super(name);
        myPort = Integer.parseInt(name);
        unicastSocket = new DatagramSocket(myPort);
        unicastSocket.setSoTimeout(2000);
        numOfMessage = 10;
        next = 1;
        ok = true;
    }

    //Stenning algorithm
    public void run() {
        try {
            while (true) {
                if (myPort == topology.getMinimumPort() && ok) {
                    System.out.println("Process1: sending " + next + "th message");
                    UDPProtocol.sendUnicastObject(unicastSocket, new Message("message", "message" + next, next), myPort + 1);
                    ok = false;
                }
                try {
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    unicastSocket.receive(receivePacket);
                    ByteArrayInputStream bais = new ByteArrayInputStream(receiveData);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    Message message = (Message) ois.readObject();
                    if (message.getType().equals("ack")) {
                        if (next == message.getIndex()) {
                            ok = true;
                            next++;
                        }
                    }

                    if (message.getType().equals("message")) {
                        /*if (message.getIndex() == next) {
                            System.out.println("Process2: Deliever message: " + message.getContent());
                            UDPProtocol.sendUnicastObject(unicastSocket, new Message("ack", "", next), receivePacket.getPort());
                            next++;
                        } else {
                            UDPProtocol.sendUnicastObject(unicastSocket, new Message("ack", "", next - 1), receivePacket.getPort());
                        }*/
                        System.out.println("Process2: receives message");
                    }

                    if (next == 10) break;
                } catch (SocketTimeoutException e) {
                    //System.out.print("");
                    //System.out.println("Process1: no response after 2s, sending again the " + next + "th message");
                    UDPProtocol.sendUnicastObject(unicastSocket, new Message("message", "message" + next, next), myPort + 1);
                }
            }
            UDPProtocol.close(unicastSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
