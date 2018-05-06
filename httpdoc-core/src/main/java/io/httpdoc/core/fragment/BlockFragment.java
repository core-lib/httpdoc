package io.httpdoc.core.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.IndentAppender;
import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 代码块碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:37
 **/
public class BlockFragment implements Fragment {
    private List<CharSequence> sentences = new ArrayList<>();

    public BlockFragment(CharSequence... sentences) {
        this(Arrays.asList(sentences));
    }

    public BlockFragment(List<CharSequence> sentences) {
        this.sentences = new ArrayList<>(sentences);
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        appender.append("{").enter();
        IndentAppender apd = new IndentAppender(appender, preference.getIndent());
        for (CharSequence sentence : sentences) apd.append(sentence).enter();
        apd.close();
        appender.append("}").enter();
    }

    public List<CharSequence> getSentences() {
        return sentences;
    }

    public void setSentences(List<CharSequence> sentences) {
        this.sentences = sentences;
    }
}
