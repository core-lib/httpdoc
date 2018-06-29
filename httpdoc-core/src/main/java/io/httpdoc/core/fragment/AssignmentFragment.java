package io.httpdoc.core.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class AssignmentFragment implements Fragment {
    private CharSequence sentence;
    private Set<String> imports = new LinkedHashSet<>();

    public AssignmentFragment() {
    }

    public AssignmentFragment(CharSequence sentence) {
        this.sentence = sentence;
    }

    public AssignmentFragment(CharSequence sentence, Set<String> imports) {
        this.sentence = sentence;
        this.imports = imports;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (sentence != null && sentence.length() > 0) appender.append(" = ").append(sentence);
    }

    @Override
    public Set<String> imports() {
        return imports != null ? imports : Collections.<String>emptySet();
    }

    public CharSequence getSentence() {
        return sentence;
    }

    public void setSentence(CharSequence sentence) {
        this.sentence = sentence;
    }

    public Set<String> getImports() {
        return imports;
    }

    public void setImports(Set<String> imports) {
        this.imports = imports;
    }
}
