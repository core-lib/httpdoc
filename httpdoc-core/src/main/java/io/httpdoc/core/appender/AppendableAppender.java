package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * Appendable 拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-28 11:06
 **/
public class AppendableAppender extends EnterableAppender<AppendableAppender> implements LineAppender<AppendableAppender> {
    private final Appendable appendable;

    public AppendableAppender(Appendable appendable) {
        if (appendable == null) throw new NullPointerException();
        this.appendable = appendable;
    }

    @Override
    public AppendableAppender append(char c) throws IOException {
        validate();
        appendable.append(c);
        return this;
    }

}
