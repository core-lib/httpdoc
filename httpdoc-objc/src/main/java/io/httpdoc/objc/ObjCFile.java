package io.httpdoc.objc;

import io.httpdoc.core.Preference;
import io.httpdoc.core.Src;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;

import java.io.IOException;

/**
 * ObjC源文件
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 16:49
 **/
public class ObjCFile implements Src<Preference> {
    private final String pkg;
    private final String name;
    private final Type type;
    private final Fragment fragment;

    public ObjCFile(String pkg, String name, Type type, Fragment fragment) {
        this.pkg = pkg;
        this.name = name;
        this.type = type;
        this.fragment = fragment;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        fragment.joinTo(appender, preference);
    }

    public String getPkg() {
        return pkg;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public enum Type {
        INTERFACE(".h"), IMPLEMENTATION(".m");

        public final String extension;

        Type(String extension) {
            this.extension = extension;
        }
    }

}
