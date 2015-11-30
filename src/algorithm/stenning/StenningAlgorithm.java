package algorithm.stenning;

import algorithm.protocol.Topology;
import algorithm.tarry.*;

import java.io.IOException;

/**
 * Created by cuongpham on 11/25/15.
 */
public class StenningAlgorithm {
    public static void main(String[] args) throws IOException {
        Topology topology = new Topology(4445, 2);
        algorithm.stenning.Node.setTopology(topology);

        algorithm.stenning.Node node1 = new algorithm.stenning.Node("4445");
        algorithm.stenning.Node node2 = new algorithm.stenning.Node("4446");

        node1.start();
        node2.start();

    }
}
