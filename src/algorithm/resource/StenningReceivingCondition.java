package algorithm.resource;

import java.util.List;

/**
 * Created by cuongpham on 11/26/15.
 */
public class StenningReceivingCondition<T> {
    private int receiveNext;

    public StenningReceivingCondition(int receiveNext) {
        this.receiveNext = receiveNext;
    }

    public int getReceiveNext() {
        return receiveNext;
    }

    public void setReceiveNext(int receiveNext) {
        this.receiveNext = receiveNext;
    }
}
