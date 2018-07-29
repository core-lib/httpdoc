package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.IndentAppender;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.objc.ObjC;
import io.httpdoc.objc.type.ObjCClass;
import io.httpdoc.objc.type.ObjCType;

import java.io.IOException;
import java.util.*;

/**
 * 代码块碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:37
 **/
public class ObjCBlockFragment implements Fragment {
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
        appender.append("}");
    }

    public ObjCBlockFragment addSentence(CharSequence sentence) {
        this.sentences.add(sentence);
        return this;
    }

    public ObjCBlockFragment addSentence(int index, CharSequence sentence) {
        this.sentences.add(index, sentence);
        return this;
    }

    public ObjCBlockFragment addSentence(CharSequence sentence, String... imports) {
        this.sentences.add(sentence);
        this.imports.addAll(Arrays.asList(imports));
        return this;
    }

    public ObjCBlockFragment addSentence(int index, CharSequence sentence, String... imports) {
        this.sentences.add(index, sentence);
        this.imports.addAll(Arrays.asList(imports));
        return this;
    }

    public ObjCBlockFragment addSentence(CharSequence sentence, Class<? extends ObjC>... objCClasses) {
        this.sentences.add(sentence);
        for (Class<? extends ObjC> objCClass : objCClasses) this.imports.addAll(ObjCType.valueOf(objCClass).imports());
        return this;
    }

    public ObjCBlockFragment addSentence(int index, CharSequence sentence, Class<? extends ObjC>... objCClasses) {
        this.sentences.add(index, sentence);
        for (Class<? extends ObjC> objCClass : objCClasses) this.imports.addAll(ObjCType.valueOf(objCClass).imports());
        return this;
    }

    public ObjCBlockFragment addSentence(CharSequence sentence, ObjCType... types) {
        this.sentences.add(sentence);
        for (ObjCType type : types) for (ObjCClass dependency : type.dependencies()) imports.addAll(dependency.imports());
        return this;
    }

    public ObjCBlockFragment addSentence(int index, CharSequence sentence, ObjCType... types) {
        this.sentences.add(index, sentence);
        for (ObjCType type : types) for (ObjCClass dependency : type.dependencies()) imports.addAll(dependency.imports());
        return this;
    }

    public ObjCBlockFragment addImports(String... imports) {
        this.imports.addAll(Arrays.asList(imports));
        return this;
    }

    public List<CharSequence> getSentences() {
        return sentences;
    }

    public ObjCBlockFragment setSentences(List<CharSequence> sentences) {
        this.sentences = sentences;
        return this;
    }

    public Set<String> getImports() {
        return imports;
    }

    public ObjCBlockFragment setImports(Set<String> imports) {
        this.imports = imports;
        return this;
    }

}
