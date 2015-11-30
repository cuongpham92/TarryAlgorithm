package algorithm.resource;

import java.util.List;

/**
 * Created by cuongpham on 11/26/15.
 */
public class StenningSendingCondition<T> {
    private int sendNext;
    private boolean ok;
    private List<T> messageList;

    public StenningSendingCondition(int sendNext, boolean ok, List<T> messageList) {
        this.sendNext = sendNext;
        this.ok = ok;
        this.messageList = messageList;
    }

    public int getSendNext() {
        return sendNext;
    }

    public void setSendNext(int sendNext) {
        this.sendNext = sendNext;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<T> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<T> messageList) {
        this.messageList = messageList;
    }
}
