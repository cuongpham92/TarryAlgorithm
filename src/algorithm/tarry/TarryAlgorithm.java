package algorithm.tarry;

import algorithm.protocol.Topology;

import java.io.IOException;

/**
 * Created by cuongpham on 11/25/15.
 */
public class TarryAlgorithm {
    public static void main(String[] args) throws IOException {
        Topology topology = new Topology(4445, 4);
        Node.setTopology(topology);

        Node node1 = new Node("4445");
        Node node2 = new Node("4446");
        Node node3 = new Node("4447");
        Node node4 = new Node("4448");

        node1.start();
        node2.start();
        node3.start();
        node4.start();

    }
}
