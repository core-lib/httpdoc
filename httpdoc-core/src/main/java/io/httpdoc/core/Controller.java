package io.httpdoc.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源控制器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-12 13:35
 **/
public class Controller extends Definition implements Ordered<Controller> {
    private static final long serialVersionUID = -8892526543537266934L;

    private String pkg;
    private String name;
    private String path;
    private List<String> produces = new ArrayList<>();
    private List<String> consumes = new ArrayList<>();
    private List<Operation> operations = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private String summary;
    private String deprecated;
    private int order = Integer.MAX_VALUE;

    @Override
    public int compareTo(Controller that) {
        int c = Integer.compare(this.getOrder(), that.getOrder());
        if (c != 0) return c;
        else return this.getName().compareTo(that.getName());
    }

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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(String deprecated) {
        this.deprecated = deprecated;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
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
