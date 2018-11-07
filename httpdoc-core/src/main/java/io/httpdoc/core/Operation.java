package io.httpdoc.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源操作符
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-12 13:35
 **/
public class Operation extends Definition implements Ordered<Operation> {
    private static final long serialVersionUID = 5078545277567296662L;

    private String name;
    private String method;
    private String path;
    private List<String> produces = new ArrayList<>();
    private List<String> consumes = new ArrayList<>();
    private List<Parameter> parameters = new ArrayList<>();
    private Result result;
    private List<String> tags = new ArrayList<>();
    private boolean multipart;
    private String summary;
    private String deprecated;
    private int order = Integer.MAX_VALUE;

    @Override
    public int compareTo(Operation that) {
        int c = Integer.compare(this.getOrder(), that.getOrder());
        if (c != 0) return c;
        else return this.getName().compareTo(that.getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isMultipart() {
        return multipart;
    }

    public void setMultipart(boolean multipart) {
        this.multipart = multipart;
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
}
