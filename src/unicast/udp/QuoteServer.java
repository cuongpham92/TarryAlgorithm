package unicast.udp;

/**
 * Created by cuongpham on 11/21/15.
 */
import java.io.*;

public class QuoteServer {
    public static void main(String[] args) throws IOException {
        new QuoteServerThread().start();
    }
}
