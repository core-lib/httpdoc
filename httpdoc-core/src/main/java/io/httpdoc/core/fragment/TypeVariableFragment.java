package io.httpdoc.core.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.type.HDTypeVariable;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * 类型变量代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-10 13:30
 **/
public class TypeVariableFragment implements Fragment {
    private HDTypeVariable typeVariable;
    private String comment;

    public TypeVariableFragment() {
    }

    public TypeVariableFragment(HDTypeVariable typeVariable) {
        this.typeVariable = typeVariable;
    }

    @Override
    public Set<String> imports() {
        return typeVariable != null ? typeVariable.imports() : Collections.<String>emptySet();
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (typeVariable != null) appender.append(typeVariable);
    }

    public HDTypeVariable getTypeVariable() {
        return typeVariable;
    }

    public void setTypeVariable(HDTypeVariable typeVariable) {
        this.typeVariable = typeVariable;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
