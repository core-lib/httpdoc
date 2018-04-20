package io.httpdoc.core.interpretation;

/**
 * 注释
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 16:55
 **/
public class Interpretation {
    protected final String content;
    protected final Note[] notes;
    protected final String text;

    protected Interpretation(String content, Note[] notes, String text) {
        this.content = content;
        this.notes = notes;
        this.text = text;
    }

    public String getContent() {
        return content;
    }

    public Note[] getNotes() {
        return notes;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return String.valueOf(text);
    }
}
