package algorithm.stenning;

import java.io.Serializable;

/**
 * Created by cuongpham on 11/25/15.
 */
public class Message implements Serializable{
    private String type;
    private String content;
    private int index;

    public Message(String type, String content, int index) {
        this.type = type;
        this.content = content;
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
