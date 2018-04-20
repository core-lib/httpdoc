package io.httpdoc.core.interpretation;

/**
 * 注释标签
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 16:55
 **/
public class Note {
    private String kind;
    private String name;
    private String text;

    public Note() {
    }

    public Note(String kind, String name, String text) {
        this.kind = kind;
        this.name = name;
        this.text = text;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return String.valueOf(text);
    }
}
