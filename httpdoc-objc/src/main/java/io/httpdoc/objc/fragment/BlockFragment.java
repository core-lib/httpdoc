package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.IndentAppender;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;

import java.io.IOException;
import java.util.*;

/**
 * 代码块碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:37
 **/
public class BlockFragment implements Fragment {
    private List<CharSequence> sentences = new ArrayList<>();
    private Set<String> imports = new TreeSet<>();

    @Override
    public Set<String> imports() {
        return imports;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        appender.enter().append("{").enter();
        IndentAppender apd = new IndentAppender(appender, preference.getIndent());
        for (CharSequence sentence : sentences) apd.append(sentence).enter();
        apd.close();
        appender.append("}").enter();
    }

    public BlockFragment addSentence(CharSequence sentence, String... imports) {
        this.sentences.add(sentence);
        this.imports.addAll(Arrays.asList(imports));
        return this;
    }

    public BlockFragment addSentence(int index, CharSequence sentence, String... imports) {
        this.sentences.add(index, sentence);
        this.imports.addAll(Arrays.asList(imports));
        return this;
    }

    public BlockFragment addImports(String... imports) {
        this.imports.addAll(Arrays.asList(imports));
        return this;
    }

    public List<CharSequence> getSentences() {
        return sentences;
    }

    public BlockFragment setSentences(List<CharSequence> sentences) {
        this.sentences = sentences;
        return this;
    }

    public Set<String> getImports() {
        return imports;
    }

    public BlockFragment setImports(Set<String> imports) {
        this.imports = imports;
        return this;
    }

}
