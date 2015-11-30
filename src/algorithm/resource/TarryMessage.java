package algorithm.resource;

import java.io.Serializable;

/**
 * Created by cuongpham on 11/26/15.
 */
public class TarryMessage implements Serializable{
    private String type;
    private String content;
    private int index;

    public TarryMessage(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public TarryMessage(String type, String content, int index) {
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

    @Override
    public String toString() {
        return "TarryMessage{" +
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
