package algorithm.protocol;

import algorithm.resource.NodeConfiguration;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by cuongpham on 11/25/15.
 * This class describes the ring topology
 */
public class Topology {
    //ring topology
    private int minimumPort;
    private int N;

    public Topology(int minimumPort, int n) {
        this.minimumPort = minimumPort;
        N = n;
    }

    public int getMinimumPort() {
        return minimumPort;
    }

    public Set<NodeConfiguration> neighbors(int id) {
        Set<NodeConfiguration> neighbors = new TreeSet<>();
        if (id == minimumPort) {
            neighbors.add(id + 1);
            neighbors.add(minimumPort + N - 1);
        } else if (id == minimumPort + N - 1) {
            neighbors.add(minimumPort);
            neighbors.add(id - 1);
        } else {
            neighbors.add(id + 1);
            neighbors.add(id - 1);
        }
        return neighbors;
    }

    public void setMinimumPort(int minimumPort) {
        this.minimumPort = minimumPort;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }
}
