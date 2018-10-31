package io.httpdoc.core.interpretation;

/**
 * 概要注释
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/10/31
 */
public abstract class SummaryInterpretation extends Interpretation {

    protected SummaryInterpretation(String content, Note[] notes, String text) {
        super(content, notes, text);
    }

    public String getSummary() {
        for (int i = 0; notes != null && i < notes.length; i++) if ("summary".equals(notes[i].getKind())) return notes[i].getText();
        return null;
    }

}
