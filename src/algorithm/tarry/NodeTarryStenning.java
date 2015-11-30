package algorithm.tarry;

import algorithm.protocol.StenningProtocol;
import algorithm.protocol.Topology;
import algorithm.protocol.UDPProtocol;
import algorithm.resource.TarryMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by cuongpham on 11/25/15. It is the implementation for Tarry algorithm with ring topology
 */
public class NodeTarryStenning extends Thread {
    private StenningProtocol stenningProtocol;
    private String myIPAddress;
    private int myPort;
    private static Topology topology;
    private int parent = 0;
    private Set<Integer> listNeighbors = new HashSet<Integer>();
    private Set<Integer> listChildren = new HashSet<Integer>();
    private Set<Integer> listVisited = new HashSet<Integer>();

    public static void setTopology(Topology topology) {
        NodeTarryStenning.topology = topology;
    }

    //process's name is initialized as its port
    public NodeTarryStenning(String myIPAddress , int myPort) throws IOException {
        this.myIPAddress = myIPAddress;
        this.myPort = myPort;
        stenningProtocol = new StenningProtocol(myPort);
        listNeighbors = topology.neighbors(myPort);
    }

    //Tarry algorithm
    public void run() {
        try {
            if (myPort == topology.getMinimumPort()) {
                parent = myPort;
                stenningProtocol.send(new TarryMessage("GO", ""), InetAddress.getByName(myIPAddress) ,myPort + 1);
            }

            while (true) {
                Map<String, Object> receiveInfo = stenningProtocol.receive();
                if (receiveInfo != null) {
                    TarryMessage message = (TarryMessage) receiveInfo.get("message");
                    int port = (Integer) receiveInfo.get("port");
                    if (message.getType().equalsIgnoreCase("GO")) {
                        System.out.println("Process" + myPort + " received message GO from process " + port);
                        if (parent == 0) {
                            parent = port;
                            listVisited.add(parent);
                            System.out.println("Process" + myPort + " has parent " + parent);

                            if (listVisited.size() == listNeighbors.size()) {
                                stenningProtocol.send(new TarryMessage("BACK", "yes"), InetAddress.getByName(myIPAddress), parent);
                            } else {
                                int sendingPort = myPort + 1;
                                if (myPort == (topology.getMinimumPort() + topology.getN() - 1)) {
                                    sendingPort = topology.getMinimumPort();
                                }
                                stenningProtocol.send(new TarryMessage("GO", ""), InetAddress.getByName(myIPAddress), sendingPort);
                            }
                        } else {
                            stenningProtocol.send(new TarryMessage("BACK", "no"), InetAddress.getByName(myIPAddress), port);
                        }
                    }
                    if (message.getType().equalsIgnoreCase("BACK")) {
                        System.out.println("Process" + myPort + " received message BACK from process " + port + " with the content " + message.getContent());
                        if (message.getContent().equalsIgnoreCase("yes")) {
                            listChildren.add(port);
                        }
                        listVisited.add(port);
                        System.out.print("Children of process" + myPort + ": ");
                        for (int i : listChildren) {
                            System.out.println(i);
                        }
                        if (parent != myPort) {
                            stenningProtocol.send(new TarryMessage("BACK", "yes"), InetAddress.getByName(myIPAddress), parent);
                        }
                        System.out.println("Process" + myPort + " is done");
                        break;
                    }
                }
            }
            stenningProtocol.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
