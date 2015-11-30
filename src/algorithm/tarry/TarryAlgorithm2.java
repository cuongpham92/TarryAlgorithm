package algorithm.tarry;

import algorithm.protocol.Topology;

import java.io.IOException;

/**
 * Created by cuongpham on 11/25/15.
 */
public class TarryAlgorithm2 {
    public static void main(String[] args) throws IOException {
        Topology topology = new Topology(4445, 4);
        NodeTarryStenning.setTopology(topology);

        NodeTarryStenning node1 = new NodeTarryStenning("4445");
        NodeTarryStenning node2 = new NodeTarryStenning("4446");
        NodeTarryStenning node3 = new NodeTarryStenning("4447");
        NodeTarryStenning node4 = new NodeTarryStenning("4448");



        node1.start();
        node2.start();
        node3.start();
        node4.start();

    }
}
