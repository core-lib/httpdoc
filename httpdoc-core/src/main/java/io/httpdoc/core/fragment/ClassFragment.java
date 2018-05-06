package io.httpdoc.core.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.annotation.HDAnnotation;
import io.httpdoc.core.appender.IndentAppender;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.type.HDClass;
import io.httpdoc.core.type.HDType;
import io.httpdoc.core.type.HDTypeVariable;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 类型碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:31
 **/
public class ClassFragment extends ModifiedFragment implements Fragment {
    private String pkg;
    private CommentFragment commentFragment;
    private List<HDAnnotation> annotations = new ArrayList<>();
    private HDClass clazz;
    private HDType superclass;
    private List<HDType> interfaces = new ArrayList<>();
    private List<FieldFragment> fieldFragments = new ArrayList<>();
    private List<StaticBlockFragment> staticBlockFragments = new ArrayList<>();
    private List<InstanceBlockFragment> instanceBlockFragments = new ArrayList<>();
    private List<ConstructorFragment> constructorFragments = new ArrayList<>();
    private List<MethodFragment> methodFragments = new ArrayList<>();
    private List<ClassFragment> classFragments = new ArrayList<>();

    public ClassFragment() {
        this(Modifier.PUBLIC);
    }

    public ClassFragment(int modifier) {
        super(modifier);
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (pkg != null) appender.append("package ").append(pkg).append(";").enter();
        appender.enter();

        if (commentFragment != null) commentFragment.joinTo(appender, preference);
        appender.enter();

        for (int i = 0; annotations != null && i < annotations.size(); i++) {
            annotations.get(i).joinTo(appender, preference);
            appender.enter();
        }

        super.joinTo(appender, preference);
        appender.append(clazz.getCategory().name).append(" ").append(clazz).append(" ");

        HDTypeVariable[] typeParameters = clazz.getTypeParameters();
        for (int i = 0; typeParameters != null && i < typeParameters.length; i++) {
            if (i == 0) appender.append("<");
            else appender.append(", ");
            appender.append(typeParameters[i].getFormatName());
            if (i == typeParameters.length - 1) appender.append(">");
        }

        if (superclass != null) {
            appender.append("extends ");
            appender.append(superclass);
        }

        for (int i = 0; interfaces != null && i < interfaces.size(); i++) {
            if (i == 0) appender.append("implements ");
            else appender.append(", ");
            appender.append(interfaces.get(i));
        }

        appender.append("{").enter();

        IndentAppender indented = new IndentAppender(appender, preference.getIndent());

        // 静态属性
        for (FieldFragment fragment : fieldFragments) {
            if (!Modifier.isStatic(fragment.modifier)) continue;
            fragment.joinTo(indented, preference);
            indented.enter();
        }

        // 静态代码块
        for (Fragment fragment : staticBlockFragments) {
            fragment.joinTo(indented, preference);
            indented.enter();
        }

        // 实例属性
        for (FieldFragment fragment : fieldFragments) {
            if (Modifier.isStatic(fragment.modifier)) continue;
            fragment.joinTo(indented, preference);
            indented.enter();
        }

        // 实例代码块
        for (Fragment fragment : instanceBlockFragments) {
            fragment.joinTo(indented, preference);
            indented.enter();
        }

        // 构造器
        for (Fragment constructor : constructorFragments) {
            constructor.joinTo(indented, preference);
            indented.enter();
        }

        // 方法
        for (Fragment fragment : methodFragments) {
            fragment.joinTo(indented, preference);
            indented.enter();
        }

        // 内部类
        for (Fragment fragment : classFragments) {
            fragment.joinTo(indented, preference);
            indented.enter();
        }

        indented.close();
        appender.append("}");
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public CommentFragment getCommentFragment() {
        return commentFragment;
    }

    public void setCommentFragment(CommentFragment commentFragment) {
        this.commentFragment = commentFragment;
    }

    public List<HDAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<HDAnnotation> annotations) {
        this.annotations = annotations;
    }

    public HDClass getClazz() {
        return clazz;
    }

    public void setClazz(HDClass clazz) {
        this.clazz = clazz;
    }

    public HDType getSuperclass() {
        return superclass;
    }

    public void setSuperclass(HDType superclass) {
        this.superclass = superclass;
    }

    public List<HDType> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<HDType> interfaces) {
        this.interfaces = interfaces;
    }

    public List<FieldFragment> getFieldFragments() {
        return fieldFragments;
    }

    public void setFieldFragments(List<FieldFragment> fieldFragments) {
        this.fieldFragments = fieldFragments;
    }

    public List<StaticBlockFragment> getStaticBlockFragments() {
        return staticBlockFragments;
    }

    public void setStaticBlockFragments(List<StaticBlockFragment> staticBlockFragments) {
        this.staticBlockFragments = staticBlockFragments;
    }

    public List<InstanceBlockFragment> getInstanceBlockFragments() {
        return instanceBlockFragments;
    }

    public void setInstanceBlockFragments(List<InstanceBlockFragment> instanceBlockFragments) {
        this.instanceBlockFragments = instanceBlockFragments;
    }

    public List<ConstructorFragment> getConstructorFragments() {
        return constructorFragments;
    }

    public void setConstructorFragments(List<ConstructorFragment> constructorFragments) {
        this.constructorFragments = constructorFragments;
    }

    public List<MethodFragment> getMethodFragments() {
        return methodFragments;
    }

    public void setMethodFragments(List<MethodFragment> methodFragments) {
        this.methodFragments = methodFragments;
    }

    public List<ClassFragment> getClassFragments() {
        return classFragments;
    }

    public void setClassFragments(List<ClassFragment> classFragments) {
        this.classFragments = classFragments;
    }
}
