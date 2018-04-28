package io.httpdoc.core.fragment;

import io.httpdoc.core.appender.Appender;
import io.httpdoc.core.appender.IndentedAppender;

import java.io.IOException;

/**
 * 代码块碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:37
 **/
public class BlockFragment implements Fragment {
    private String code;

    @Override
    public <T extends Appender<T>> void joinTo(T appender, Preference preference) throws IOException {
        appender.append("{").enter();
        IndentedAppender apd = new IndentedAppender(preference.getIndent(), appender);
        apd.append(code);
        apd.flush();
        appender.append("}").enter();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
