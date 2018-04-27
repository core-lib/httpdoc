package io.httpdoc.core.generating;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 类型生成
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:31
 **/
public class ClassGenerating extends ModifiedGenerating implements Generating {
    private String pkg;
    private List<String> imports = new ArrayList<>();
    private String name;
    private String superclass;
    private List<String> interfaces = new ArrayList<>();
    private List<FieldGenerating> fieldGeneratings = new ArrayList<>();
    private List<StaticBlockGenerating> staticBlockGeneratings = new ArrayList<>();
    private List<InstanceBlockGenerating> instanceBlockGeneratings = new ArrayList<>();
    private List<ConstructorGenerating> constructorGeneratings = new ArrayList<>();
    private List<MethodGenerating> methodGeneratings = new ArrayList<>();
    private List<ClassGenerating> classGeneratings = new ArrayList<>();

    public ClassGenerating(String pkg, String name) {
        this.pkg = pkg;
        this.name = name;
    }

    public ClassGenerating(int modifier, String pkg, String name) {
        super(modifier);
        this.pkg = pkg;
        this.name = name;
    }

    public ClassGenerating(String pkg, String name, String superclass) {
        this.pkg = pkg;
        this.name = name;
        this.superclass = superclass;
    }

    public ClassGenerating(int modifier, String pkg, String name, String superclass) {
        super(modifier);
        this.pkg = pkg;
        this.name = name;
        this.superclass = superclass;
    }

    public ClassGenerating(String pkg, String name, String superclass, List<String> interfaces) {
        this.pkg = pkg;
        this.name = name;
        this.superclass = superclass;
        this.interfaces = interfaces;
    }

    public ClassGenerating(int modifier, String pkg, String name, String superclass, List<String> interfaces) {
        super(modifier);
        this.pkg = pkg;
        this.name = name;
        this.superclass = superclass;
        this.interfaces = interfaces;
    }

    @Override
    public <T extends Appender<T>> void generate(T appender) throws IOException {
        appender.appendln("package " + pkg);
        appender.appendln();
        for (String i : imports) appender.appendln("import " + i);
        appender.appendln();
        super.generate(appender);

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

    public List<FieldGenerating> getFieldGeneratings() {
        return fieldGeneratings;
    }

    public void setFieldGeneratings(List<FieldGenerating> fieldGeneratings) {
        this.fieldGeneratings = fieldGeneratings;
    }

    public List<StaticBlockGenerating> getStaticBlockGeneratings() {
        return staticBlockGeneratings;
    }

    public void setStaticBlockGeneratings(List<StaticBlockGenerating> staticBlockGeneratings) {
        this.staticBlockGeneratings = staticBlockGeneratings;
    }

    public List<InstanceBlockGenerating> getInstanceBlockGeneratings() {
        return instanceBlockGeneratings;
    }

    public void setInstanceBlockGeneratings(List<InstanceBlockGenerating> instanceBlockGeneratings) {
        this.instanceBlockGeneratings = instanceBlockGeneratings;
    }

    public List<ConstructorGenerating> getConstructorGeneratings() {
        return constructorGeneratings;
    }

    public void setConstructorGeneratings(List<ConstructorGenerating> constructorGeneratings) {
        this.constructorGeneratings = constructorGeneratings;
    }

    public List<MethodGenerating> getMethodGeneratings() {
        return methodGeneratings;
    }

    public void setMethodGeneratings(List<MethodGenerating> methodGeneratings) {
        this.methodGeneratings = methodGeneratings;
    }

    public List<ClassGenerating> getClassGeneratings() {
        return classGeneratings;
    }

    public void setClassGeneratings(List<ClassGenerating> classGeneratings) {
        this.classGeneratings = classGeneratings;
    }
}
