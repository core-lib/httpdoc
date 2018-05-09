package io.httpdoc.core.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AssignmentFragment implements Fragment {
    private CharSequence sentence;
    private List<String> imports = new ArrayList<>();

    public AssignmentFragment() {
    }

    public AssignmentFragment(CharSequence sentence) {
        this.sentence = sentence;
    }

    public AssignmentFragment(CharSequence sentence, List<String> imports) {
        this.sentence = sentence;
        this.imports = imports;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (sentence != null && sentence.length() > 0) appender.append(" = ").append(sentence);
    }

    @Override
    public List<String> imports() {
        return imports != null ? imports : Collections.<String>emptyList();
    }

    public CharSequence getSentence() {
        return sentence;
    }

    public void setSentence(CharSequence sentence) {
        this.sentence = sentence;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }
}
