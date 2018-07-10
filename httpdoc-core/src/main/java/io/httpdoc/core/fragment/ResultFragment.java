package io.httpdoc.core.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.type.HDClass;
import io.httpdoc.core.type.HDType;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * 返回值代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-10 13:12
 **/
public class ResultFragment implements Fragment {
    public static final HDClass VOID = HDType.valueOf(void.class);
    private HDType type;
    private String comment;

    public ResultFragment() {
    }

    public ResultFragment(HDType type) {
        this.type = type;
    }

    public ResultFragment(HDType type, String comment) {
        this.type = type;
        this.comment = comment;
    }

    @Override
    public Set<String> imports() {
        return type != null && !type.equals(VOID) ? type.imports() : Collections.<String>emptySet();
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (type != null) appender.append(type).append(" ");
        else appender.append("void ");
    }

    public HDType getType() {
        return type;
    }

    public void setType(HDType type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
