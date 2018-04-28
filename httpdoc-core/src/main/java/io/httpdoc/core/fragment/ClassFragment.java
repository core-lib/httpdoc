package io.httpdoc.core.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 类型生成
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:31
 **/
public class ClassFragment extends ModifiedFragment implements Fragment {
    private String pkg;
    private List<String> imports = new ArrayList<>();
    private String name;
    private String superclass;
    private List<String> interfaces = new ArrayList<>();
    private List<FieldFragment> fieldGeneratings = new ArrayList<>();
    private List<StaticBlockFragment> staticBlockGeneratings = new ArrayList<>();
    private List<InstanceBlockFragment> instanceBlockGeneratings = new ArrayList<>();
    private List<ConstructorFragment> constructorGeneratings = new ArrayList<>();
    private List<MethodFragment> methodGeneratings = new ArrayList<>();
    private List<ClassFragment> classGeneratings = new ArrayList<>();

    public ClassFragment(String pkg, String name) {
        this.pkg = pkg;
        this.name = name;
    }

    public ClassFragment(int modifier, String pkg, String name) {
        super(modifier);
        this.pkg = pkg;
        this.name = name;
    }

    public ClassFragment(String pkg, String name, String superclass) {
        this.pkg = pkg;
        this.name = name;
        this.superclass = superclass;
    }

    public ClassFragment(int modifier, String pkg, String name, String superclass) {
        super(modifier);
        this.pkg = pkg;
        this.name = name;
        this.superclass = superclass;
    }

    public ClassFragment(String pkg, String name, String superclass, List<String> interfaces) {
        this.pkg = pkg;
        this.name = name;
        this.superclass = superclass;
        this.interfaces = interfaces;
    }

    public ClassFragment(int modifier, String pkg, String name, String superclass, List<String> interfaces) {
        super(modifier);
        this.pkg = pkg;
        this.name = name;
        this.superclass = superclass;
        this.interfaces = interfaces;
    }

    @Override
    public <T extends Appender<T>> void joinTo(T appender) throws IOException {

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

    public List<FieldFragment> getFieldGeneratings() {
        return fieldGeneratings;
    }

    public void setFieldGeneratings(List<FieldFragment> fieldGeneratings) {
        this.fieldGeneratings = fieldGeneratings;
    }

    public List<StaticBlockFragment> getStaticBlockGeneratings() {
        return staticBlockGeneratings;
    }

    public void setStaticBlockGeneratings(List<StaticBlockFragment> staticBlockGeneratings) {
        this.staticBlockGeneratings = staticBlockGeneratings;
    }

    public List<InstanceBlockFragment> getInstanceBlockGeneratings() {
        return instanceBlockGeneratings;
    }

    public void setInstanceBlockGeneratings(List<InstanceBlockFragment> instanceBlockGeneratings) {
        this.instanceBlockGeneratings = instanceBlockGeneratings;
    }

    public List<ConstructorFragment> getConstructorGeneratings() {
        return constructorGeneratings;
    }

    public void setConstructorGeneratings(List<ConstructorFragment> constructorGeneratings) {
        this.constructorGeneratings = constructorGeneratings;
    }

    public List<MethodFragment> getMethodGeneratings() {
        return methodGeneratings;
    }

    public void setMethodGeneratings(List<MethodFragment> methodGeneratings) {
        this.methodGeneratings = methodGeneratings;
    }

    public List<ClassFragment> getClassGeneratings() {
        return classGeneratings;
    }

    public void setClassGeneratings(List<ClassFragment> classGeneratings) {
        this.classGeneratings = classGeneratings;
    }
}
