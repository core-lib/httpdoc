package io.httpdoc.core.fragment;

import io.httpdoc.core.appender.Appender;
import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;

/**
 * 语句碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-02 10:27
 **/
public class SentenceFragment implements Fragment {
    private CharSequence code;

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        appender.append(code);
    }

    public CharSequence getCode() {
        return code;
    }

    public void setCode(CharSequence code) {
        this.code = code;
    }
}
