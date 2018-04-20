package io.httpdoc.core.interpretation;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法解释
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-20 17:14
 **/
public class MethodInterpretation extends Interpretation {

    public MethodInterpretation(String content, Note[] notes, String text) {
        super(content, notes, text);
    }

    public Note getReturnNote() {
        for (int i = 0; notes != null && i < notes.length; i++) if ("return".equals(notes[i].getKind())) return notes[i];
        return null;
    }

    public Note[] getParamNotes() {
        List<Note> list = new ArrayList<>();
        for (int i = 0; notes != null && i < notes.length; i++) {
            if ("param".equals(notes[i].getKind())) {
                list.add(notes[i]);
            }
        }
        return list.toArray(new Note[0]);
    }

    public Note[] getThrowsNotes() {
        List<Note> list = new ArrayList<>();
        for (int i = 0; notes != null && i < notes.length; i++) if ("throws".equals(notes[i].getKind())) list.add(notes[i]);
        return list.toArray(new Note[0]);
    }

}
