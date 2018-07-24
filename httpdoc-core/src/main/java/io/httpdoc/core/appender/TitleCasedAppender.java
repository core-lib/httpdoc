package io.httpdoc.core.appender;

import java.io.IOException;

/**
 * 首字母大写的拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 10:03
 **/
public class TitleCasedAppender extends FilterLineAppender<TitleCasedAppender> {
    private volatile boolean first = true;

    public TitleCasedAppender(LineAppender<?> appender) {
        super(appender);
    }

    @Override
    public TitleCasedAppender append(char c) throws IOException {
        if (first) {
            first = false;
            char upper = new String(new char[]{c}).toUpperCase().charAt(0);
            return super.append(upper);
        } else {
            return super.append(c);
        }
    }
}
