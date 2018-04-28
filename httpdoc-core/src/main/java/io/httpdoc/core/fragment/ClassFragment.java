package io.httpdoc.core.fragment;

import io.httpdoc.core.appender.Appender;
import io.httpdoc.core.appender.IndentedAppender;

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
    private List<String> imports = new ArrayList<>();
    private CommentFragment commentFragment;
    private String name;
    private String superclass;
    private List<String> interfaces = new ArrayList<>();
    private List<FieldFragment> fieldFragments = new ArrayList<>();
    private List<StaticBlockFragment> staticBlockFragments = new ArrayList<>();
    private List<InstanceBlockFragment> instanceBlockFragments = new ArrayList<>();
    private List<ConstructorFragment> constructorFragments = new ArrayList<>();
    private List<MethodFragment> methodFragments = new ArrayList<>();
    private List<ClassFragment> classFragments = new ArrayList<>();

    @Override
    public <T extends Appender<T>> void joinTo(T apd, Preference preference) throws IOException {
        if (pkg != null) apd.append("package ").append(pkg).append(";").enter();
        apd.enter();

        for (String inport : imports) apd.append("import ").append(inport).append(";").enter();
        apd.enter();

        if (commentFragment != null) commentFragment.joinTo(apd, preference);

        super.joinTo(apd, preference);
        apd.append("class").append(name).append(" ");

        if (superclass != null) apd.append("extends ").append(superclass).append(" ");
        if (!interfaces.isEmpty()) {
            apd.append("implements ");
            for (int i = 0; i < interfaces.size(); i++) {
                if (i > 0) apd.append(",");
                apd.append(interfaces.get(i)).append(" ");
            }
        }

        apd.append("{").enter();

        IndentedAppender iapd = new IndentedAppender(preference.getIndent(), apd);

        // 静态属性
        for (FieldFragment fragment : fieldFragments) {
            if (!Modifier.isStatic(fragment.modifier)) continue;
            fragment.joinTo(iapd, preference);
            iapd.enter();
        }
        iapd.flush();

        // 静态代码块
        for (Fragment fragment : staticBlockFragments) {
            fragment.joinTo(iapd, preference);
            iapd.enter();
        }
        iapd.flush();

        // 实例属性
        for (FieldFragment fragment : fieldFragments) {
            if (Modifier.isStatic(fragment.modifier)) continue;
            fragment.joinTo(iapd, preference);
            iapd.enter();
        }
        iapd.flush();

        // 实例代码块
        for (Fragment fragment : instanceBlockFragments) {
            fragment.joinTo(iapd, preference);
            iapd.enter();
        }
        iapd.flush();

        // 构造器
        for (Fragment constructor : constructorFragments) {
            constructor.joinTo(iapd, preference);
            iapd.enter();
        }
        iapd.flush();

        // 构造器
        for (Fragment fragment : methodFragments) {
            fragment.joinTo(iapd, preference);
            iapd.enter();
        }
        iapd.flush();

        // 内部类
        for (Fragment fragment : classFragments) {
            fragment.joinTo(iapd, preference);
            iapd.enter();
        }
        iapd.flush();

        apd.append("}");
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public CommentFragment getCommentFragment() {
        return commentFragment;
    }

    public void setCommentFragment(CommentFragment commentFragment) {
        this.commentFragment = commentFragment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuperclass() {
        return superclass;
    }

    public void setSuperclass(String superclass) {
        this.superclass = superclass;
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(List<String> interfaces) {
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
