package io.httpdoc.core.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;

import java.io.IOException;
import java.lang.reflect.Modifier;

/**
 * ModifiedFragment
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:45
 **/
public abstract class ModifiedFragment implements Fragment {
    protected int modifier;

    protected ModifiedFragment(int modifier) {
        this.modifier = modifier;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (Modifier.isPublic(modifier)) appender.append("public ");
        if (Modifier.isProtected(modifier)) appender.append("protected ");
        if (Modifier.isPrivate(modifier)) appender.append("private ");
        if (Modifier.isAbstract(modifier)) appender.append("abstract ");
        if (Modifier.isStatic(modifier)) appender.append("static ");
        if (Modifier.isFinal(modifier)) appender.append("final ");
        if (Modifier.isInterface(modifier)) appender.append("interface ");
        if (Modifier.isVolatile(modifier)) appender.append("volatile ");
        if (Modifier.isNative(modifier)) appender.append("native ");
        if (Modifier.isStrict(modifier)) appender.append("strict ");
        if (Modifier.isSynchronized(modifier)) appender.append("synchronized ");
        if (Modifier.isTransient(modifier)) appender.append("transient ");
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }
}
