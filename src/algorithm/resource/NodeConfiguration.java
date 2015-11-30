package algorithm.resource;

/**
 * Created by cuongpham on 11/30/15.
 * This class contains a node's information, including:
 * - its ip address
 * - its UDP listening port
 */
public class NodeConfiguration {
    private String myIPAddress;
    private int myPort;

    public NodeConfiguration(String myIPAddress, int myPort) {
        this.myIPAddress = myIPAddress;
        this.myPort = myPort;
    }

    public String getIpAddress() {
        return myIPAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.myIPAddress = ipAddress;
    }

    public int getPort() {
        return myPort;
    }

    public void setPort(int myPort) {
        this.myPort = myPort;
    }
}
