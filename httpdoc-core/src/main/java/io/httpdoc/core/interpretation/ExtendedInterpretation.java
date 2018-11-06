package io.httpdoc.core.interpretation;

import io.httpdoc.core.kit.StringKit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 拓展的注释
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/10/31
 */
public abstract class ExtendedInterpretation extends Interpretation {

    protected ExtendedInterpretation(String content, Note[] notes, String text) {
        super(content, notes, text);
    }

    public String getName() {
        for (int i = 0; notes != null && i < notes.length; i++) if ("@name".equals(notes[i].getKind())) return notes[i].getText();
        return null;
    }

    public String getPackage() {
        for (int i = 0; notes != null && i < notes.length; i++) if ("@package".equals(notes[i].getKind())) return notes[i].getText();
        return null;
    }

    public String getSummary() {
        for (int i = 0; notes != null && i < notes.length; i++) if ("@summary".equals(notes[i].getKind())) return notes[i].getText();
        return null;
    }

    public String getDeprecated() {
        for (int i = 0; notes != null && i < notes.length; i++) if ("@deprecated".equals(notes[i].getKind())) return StringKit.isBlank(notes[i].getText()) ? "deprecated" : notes[i].getText();
        return null;
    }

    public boolean isSkip() {
        for (int i = 0; notes != null && i < notes.length; i++) if ("@skip".equals(notes[i].getKind())) return true;
        return false;
    }

    public List<String> getTags() {
        List<String> tags = new ArrayList<>();
        for (int i = 0; notes != null && i < notes.length; i++) if ("@tag".equals(notes[i].getKind())) tags.add(notes[i].getText());
        return tags;
    }

    public List<String> getIgnores() {
        List<String> ignores = new ArrayList<>();
        for (int i = 0; notes != null && i < notes.length; i++) if ("@ignore".equals(notes[i].getKind())) ignores.add(notes[i].getText());
        return ignores;
    }

    public Map<String, String> getAliases() {
        Map<String, String> aliases = new LinkedHashMap<>();
        for (int i = 0; notes != null && i < notes.length; i++) {
            if ("@alias".equals(notes[i].getKind())) {
                String text = notes[i].getText();
                if (text == null) text = "";
                String[] texts = text.trim().split("\\s+");
                if (texts.length > 1) aliases.put(texts[0], texts[1]);
                else aliases.put("", texts[0]);
            }
        }
        return aliases;
    }

}
