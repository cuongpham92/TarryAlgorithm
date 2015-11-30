package algorithm.tarry;

import algorithm.protocol.Topology;
import algorithm.protocol.UDPProtocol;
import algorithm.resource.TarryMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.*;

/**
 * Created by cuongpham on 11/25/15. It is the implementation for Tarry algorithm with ring topology
 */
public class NodeTarry extends Thread{
    protected DatagramSocket unicastSocket = null;
    private String myIPAddress;
    private int myPort;
    private static Topology topology;
    private int parent = 0;
    private Set<Integer> listNeighbors = new HashSet<Integer>();
    private Set<Integer> listChildren = new HashSet<Integer>();
    private Set<Integer> listVisited = new HashSet<Integer>();

    public static void setTopology(Topology topology) {
        NodeTarry.topology = topology;
    }

    //process's name is initialized as its port
    public NodeTarry(String myIPAddress, int myPort) throws IOException {
        this.myIPAddress = myIPAddress;
        this.myPort = myPort;
        unicastSocket = new DatagramSocket(myPort);
        listNeighbors = topology.neighbors(myPort);
    }

    //Tarry algorithm
    public void run() {
        try{
            if (myPort == topology.getMinimumPort()) {
                parent = myPort;
                UDPProtocol.sendUnicastObject(unicastSocket, new TarryMessage("GO", ""), InetAddress.getByName(myIPAddress), myPort + 1);
            }

            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                unicastSocket.receive(receivePacket);
                ByteArrayInputStream bais = new ByteArrayInputStream(receiveData);
                ObjectInputStream ois = new ObjectInputStream(bais);
                TarryMessage message = (TarryMessage) ois.readObject();
                if(message.getType().equalsIgnoreCase("GO")) {
                    System.out.println("Process" + myPort + " received message GO from process " + receivePacket.getPort());
                    if (parent == 0) {
                        parent = receivePacket.getPort();
                        listVisited.add(parent);
                        System.out.println("Process" + myPort + " has parent " + parent);

                        if(listVisited.size() == listNeighbors.size()) {
                            UDPProtocol.sendUnicastObject(unicastSocket, new TarryMessage("BACK", "yes"), InetAddress.getByName(myIPAddress), parent);
                        } else {
                            int sendingPort = myPort + 1;
                            if (myPort == (topology.getMinimumPort() + topology.getN() - 1)) {
                                sendingPort = topology.getMinimumPort();
                            }
                            UDPProtocol.sendUnicastObject(unicastSocket, new TarryMessage("GO", ""), InetAddress.getByName(myIPAddress), sendingPort);
                        }
                    } else {
                        UDPProtocol.sendUnicastObject(unicastSocket, new TarryMessage("BACK", "no"), InetAddress.getByName(myIPAddress), receivePacket.getPort());
                    }
                }
                if(message.getType().equalsIgnoreCase("BACK")) {
                    System.out.println("Process" + myPort + " received message BACK from process " + receivePacket.getPort() + " with the content " + message.getContent());
                    if (message.getContent().equalsIgnoreCase("yes")) {
                        listChildren.add(receivePacket.getPort());
                    }
                    listVisited.add(receivePacket.getPort());
                    System.out.print("Children of process" + myPort + ": ");
                    for (int i : listChildren) {
                        System.out.println(i);
                    }
                    if (parent != myPort) {
                        UDPProtocol.sendUnicastObject(unicastSocket, new TarryMessage("BACK", "yes"), InetAddress.getByName(myIPAddress), parent);
                    }
                    System.out.println("Process" + myPort + " is done");
                    break;
                }
            }
            UDPProtocol.close(unicastSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
