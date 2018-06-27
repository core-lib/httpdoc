package io.httpdoc.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源控制器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-12 13:35
 **/
public class Controller extends Definition {
    private static final long serialVersionUID = -8892526543537266934L;

    private String pkg;
    private String name;
    private String path;
    private List<String> produces = new ArrayList<>();
    private List<String> consumes = new ArrayList<>();
    private List<Operation> operations = new ArrayList<>();

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getProduces() {
        return produces;
    }

    public void setProduces(List<String> produces) {
        this.produces = produces;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public void setConsumes(List<String> consumes) {
        this.consumes = consumes;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Controller that = (Controller) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
