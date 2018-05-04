package io.httpdoc.core.type;

import java.util.Collections;
import java.util.List;

/**
 * 类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-02 12:18
 **/
public class JavaClass extends JavaType {
    private final String name;
    private final JavaClass component;
    private JavaTypeVariable[] typeParameters;

    public JavaClass(String name) {
        if (name == null) throw new NullPointerException();
        this.name = name;
        this.component = name.endsWith("[]") ? new JavaClass(name.substring(0, name.length() - 2)) : null;
    }

    public JavaClass(Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException();
        } else if (clazz.isArray()) {
            name = (component = new JavaClass(clazz.getComponentType())).getName() + "[]";
        } else {
            name = clazz.getName();
            component = null;
        }
    }

    @Override
    public List<String> imports() {
        return component != null ? component.imports() : Collections.singletonList(name);
    }

    public String getSimpleName() {
        return name.contains(".") ? name.substring(name.lastIndexOf(".") + 1) : name;
    }

    @Override
    public CharSequence getFormatName() {
        return component != null ? component.getFormatName() + "[]" : getSimpleName();
    }

    @Override
    public int length() {
        return getFormatName().length();
    }

    @Override
    public char charAt(int index) {
        return getFormatName().charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return getFormatName().subSequence(start, end);
    }

    public String getName() {
        return name;
    }

    public JavaClass getComponent() {
        return component;
    }

    public JavaTypeVariable[] getTypeParameters() {
        return typeParameters;
    }

    void setTypeParameters(JavaTypeVariable[] typeParameters) {
        this.typeParameters = typeParameters;
    }
}
