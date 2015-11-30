package algorithm.resource;

import java.io.Serializable;

/**
 * Created by cuongpham on 11/28/15.
 * This class describes Stenning wrapper message, each will include:
 * - type of the message (incomming or ack message)
 * - index of the message (according to stenning algorithm)
 * - the main content
 *
 * This wrapper is used for other algorithms (e.g., Tarry algorithm) to
 * - put their messages in here and make Stenning do the reliable sending
 * - Stenning will receive the message from the network and deliver the ack message to the sender
 */
public class StenningMessage<T> implements Serializable {
    private String type;
    private int index;
    private T message;

    public StenningMessage(String type, int index, T message) {
        this.type = type;
        this.index = index;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
