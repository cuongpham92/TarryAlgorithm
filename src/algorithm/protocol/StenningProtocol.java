package algorithm.protocol;

import algorithm.resource.StenningMessage;
import algorithm.resource.StenningReceivingCondition;
import algorithm.resource.StenningSendingCondition;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * Created by cuongpham on 11/28/15.
 * This class contains Stenning protocol.
 */
public class StenningProtocol<T> {

    private int myPort;
    private DatagramSocket unicastSocket;
    private Map<Integer, StenningSendingCondition> sendingconditions;
    private Map<Integer, StenningReceivingCondition> receivingconditions;


    /**
     * Constructor of StenningProtocol class
     * @param myPort    The port that this process is using for sending and receiving UDP messages
     */
    public StenningProtocol(int myPort) {
        try {
            this.myPort = myPort;
            unicastSocket = new DatagramSocket(myPort);
            unicastSocket.setSoTimeout(2000);
            sendingconditions = new TreeMap<>();
            receivingconditions = new TreeMap<>();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This function takes the message and the destination (defined by ipAddress and port) from Tarry algorithm's layer
     * and do the reliable send
     * @param message       the TarryMessage
     * @param ipAddress     the ipAddress of the sender
     * @param port          the port of the sender
     */
    public void send(T message, InetAddress ipAddress, int port) {
        if (unicastSocket != null) {
            while (true) {
                try {
                    StenningSendingCondition condition = null;
                    if (!sendingconditions.containsKey(port)) {
                        sendingconditions.put(port, new StenningSendingCondition(0, true, new ArrayList<T>()));
                    }
                    condition = sendingconditions.get(port);
                    condition.getMessageList().add(message);

                    if (condition.isOk() && condition.getSendNext() < condition.getMessageList().size()) {
                        try {
                            StenningMessage stenningMessage = new StenningMessage("incoming", condition.getSendNext(), condition.getMessageList().get(condition.getSendNext()));
                            UDPProtocol.sendUnicastObject(unicastSocket, stenningMessage, ipAddress, port);
                            condition.setOk(false);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    try {
                        byte[] receiveData = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        unicastSocket.receive(receivePacket);
                        ByteArrayInputStream bais = new ByteArrayInputStream(receiveData);
                        ObjectInputStream ois = new ObjectInputStream(bais);
                        StenningMessage receiveMessage = (StenningMessage) ois.readObject();

                        if (receiveMessage.getType().equals("ack")) {
                            if (condition.getSendNext() == receiveMessage.getIndex()) {
                                condition.setOk(true);
                                condition.setSendNext(condition.getSendNext() + 1);
                                System.out.println("Process" + myPort + " received ack from Process" + receivePacket.getPort());
                                if(condition.getSendNext() == condition.getMessageList().size())
                                    break;
                            }
                        }
                    } catch (SocketTimeoutException e) {
                        System.out.println("Process" + myPort + " : no response after 2s, sending again the " + condition.getSendNext() + "th message to Process" + port);
                        StenningMessage stenningMessage = new StenningMessage("", condition.getSendNext(), condition.getMessageList().get(condition.getSendNext()));
                        UDPProtocol.sendUnicastObject(unicastSocket, stenningMessage, ipAddress, port);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This function will receive the packet from network and send the ack back to the sender
     * to inform that the message has been delivered probably
     * @return  a Map contains two information: the sender's Tarry message and the sender's address
     */
    public Map<String, Object> receive() {
        try {
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            unicastSocket.receive(receivePacket);
            ByteArrayInputStream bais = new ByteArrayInputStream(receiveData);
            ObjectInputStream ois = new ObjectInputStream(bais);
            StenningMessage message = (StenningMessage) ois.readObject();

            if (message.getType().equals("incoming")) {
                StenningReceivingCondition condition = null;
                if (!receivingconditions.containsKey(receivePacket.getPort())) {
                    receivingconditions.put(receivePacket.getPort(), new StenningReceivingCondition(0));
                }
                condition = receivingconditions.get(receivePacket.getPort());
                if (message.getIndex() == condition.getReceiveNext()) {
                    UDPProtocol.sendUnicastObject(unicastSocket, new StenningMessage("ack", condition.getReceiveNext(), null), receivePacket.getAddress(), receivePacket.getPort());
                    condition.setReceiveNext(condition.getReceiveNext() + 1);
                    return new TreeMap<String, Object>(){{put("message", message.getMessage()); put("port", receivePacket.getPort());}};
                } else {
                    UDPProtocol.sendUnicastObject(unicastSocket, new StenningMessage("ack", condition.getReceiveNext() - 1, null), receivePacket.getAddress(), receivePacket.getPort());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This function will close the UPD connection when the algorithm terminates
     */
    public void close() {
        UDPProtocol.close(unicastSocket);
    }
}
