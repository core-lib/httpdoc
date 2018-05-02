package io.httpdoc.core.fragment;

import io.httpdoc.core.appender.IndentedAppender;
import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码块碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:37
 **/
public class BlockFragment implements Fragment {
    private List<SentenceFragment> sentenceFragments = new ArrayList<>();

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        appender.append("{").enter();
        IndentedAppender apd = new IndentedAppender(appender, preference.getIndent());
        for (SentenceFragment fragment : sentenceFragments) fragment.joinTo(apd, preference);
        apd.close();
        appender.append("}").enter();
    }

    public List<SentenceFragment> getSentenceFragments() {
        return sentenceFragments;
    }

    public void setSentenceFragments(List<SentenceFragment> sentenceFragments) {
        this.sentenceFragments = sentenceFragments;
    }
}
