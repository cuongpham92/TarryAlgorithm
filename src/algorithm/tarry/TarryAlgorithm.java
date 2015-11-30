package algorithm.tarry;

import algorithm.protocol.Topology;

import java.io.IOException;

/**
 * Created by cuongpham on 11/25/15.
 */
public class TarryAlgorithm {
    public static void main(String[] args) throws IOException {
        Topology topology = new Topology(4445, 4);
        NodeTarry.setTopology(topology);

        NodeTarry node1 = new NodeTarry("localhost", 4445);
        NodeTarry node2 = new NodeTarry("localhost", 4446);
        NodeTarry node3 = new NodeTarry("localhost", 4447);
        NodeTarry node4 = new NodeTarry("localhost", 4448);

        node1.start();
        node2.start();
        node3.start();
        node4.start();

    }
}
