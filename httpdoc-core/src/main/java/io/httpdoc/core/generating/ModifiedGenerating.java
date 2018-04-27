package io.httpdoc.core.generating;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Modifier;

/**
 * ModifiedGenerating
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:45
 **/
public abstract class ModifiedGenerating implements Generating {
    protected int modifier;

    protected ModifiedGenerating() {
    }

    protected ModifiedGenerating(int modifier) {
        this.modifier = modifier;
    }

    @Override
    public <T extends Appender<T>> void generate(T appender) throws IOException {
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

}
