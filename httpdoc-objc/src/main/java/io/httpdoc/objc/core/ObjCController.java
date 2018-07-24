package io.httpdoc.objc.core;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Operation;

import java.util.ArrayList;
import java.util.List;

public class ObjCController extends Controller {
    private static final long serialVersionUID = -534658184125799508L;
    private final String prefix;
    private final Controller controller;

    public ObjCController(String prefix, Controller controller) {
        this.prefix = prefix;
        this.controller = controller;
    }

    @Override
    public String getPkg() {
        return controller.getPkg();
    }

    @Override
    public void setPkg(String pkg) {
        controller.setPkg(pkg);
    }

    @Override
    public String getName() {
        return controller.getName();
    }

    @Override
    public void setName(String name) {
        controller.setName(name);
    }

    @Override
    public String getPath() {
        return controller.getPath();
    }

    @Override
    public void setPath(String path) {
        controller.setPath(path);
    }

    @Override
    public List<String> getProduces() {
        return controller.getProduces();
    }

    @Override
    public void setProduces(List<String> produces) {
        controller.setProduces(produces);
    }

    @Override
    public List<String> getConsumes() {
        return controller.getConsumes();
    }

    @Override
    public void setConsumes(List<String> consumes) {
        controller.setConsumes(consumes);
    }

    @Override
    public List<Operation> getOperations() {
        List<Operation> operations = controller.getOperations();
        if (operations == null) return null;
        List<Operation> list = new ArrayList<>();
        for (Operation operation : operations) list.add(new ObjCOperation(prefix, operation));
        return list;
    }

    @Override
    public void setOperations(List<Operation> operations) {
        controller.setOperations(operations);
    }

    @Override
    public List<String> getTags() {
        return controller.getTags();
    }

    @Override
    public void setTags(List<String> tags) {
        controller.setTags(tags);
    }

    @Override
    public String getDescription() {
        return controller.getDescription();
    }

    @Override
    public void setDescription(String description) {
        controller.setDescription(description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ObjCController that = (ObjCController) o;

        return controller != null ? controller.equals(that.controller) : that.controller == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (controller != null ? controller.hashCode() : 0);
        return result;
    }

}
