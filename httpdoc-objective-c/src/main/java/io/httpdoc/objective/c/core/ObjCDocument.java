package io.httpdoc.objective.c.core;

import io.httpdoc.core.Controller;
import io.httpdoc.core.Document;
import io.httpdoc.core.Schema;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class ObjCDocument extends Document {
    private static final long serialVersionUID = -1120462878218884601L;
    private final String prefix;
    private final Document document;

    public ObjCDocument(String prefix, Document document) {
        this.prefix = prefix;
        this.document = document;
    }

    @Override
    public String getHttpdoc() {
        return document.getHttpdoc();
    }

    @Override
    public void setHttpdoc(String httpdoc) {
        document.setHttpdoc(httpdoc);
    }

    @Override
    public String getProtocol() {
        return document.getProtocol();
    }

    @Override
    public void setProtocol(String protocol) {
        document.setProtocol(protocol);
    }

    @Override
    public String getHostname() {
        return document.getHostname();
    }

    @Override
    public void setHostname(String hostname) {
        document.setHostname(hostname);
    }

    @Override
    public Integer getPort() {
        return document.getPort();
    }

    @Override
    public void setPort(Integer port) {
        document.setPort(port);
    }

    @Override
    public String getContext() {
        return document.getContext();
    }

    @Override
    public void setContext(String context) {
        document.setContext(context);
    }

    @Override
    public String getVersion() {
        return document.getVersion();
    }

    @Override
    public void setVersion(String version) {
        document.setVersion(version);
    }

    @Override
    public String getRefPrefix() {
        return document.getRefPrefix();
    }

    @Override
    public void setRefPrefix(String refPrefix) {
        document.setRefPrefix(refPrefix);
    }

    @Override
    public String getRefSuffix() {
        return document.getRefSuffix();
    }

    @Override
    public void setRefSuffix(String refSuffix) {
        document.setRefSuffix(refSuffix);
    }

    @Override
    public String getMapPrefix() {
        return document.getMapPrefix();
    }

    @Override
    public void setMapPrefix(String mapPrefix) {
        document.setMapPrefix(mapPrefix);
    }

    @Override
    public String getMapSuffix() {
        return document.getMapSuffix();
    }

    @Override
    public void setMapSuffix(String mapSuffix) {
        document.setMapSuffix(mapSuffix);
    }

    @Override
    public String getArrPrefix() {
        return document.getArrPrefix();
    }

    @Override
    public void setArrPrefix(String arrPrefix) {
        document.setArrPrefix(arrPrefix);
    }

    @Override
    public String getArrSuffix() {
        return document.getArrSuffix();
    }

    @Override
    public void setArrSuffix(String arrSuffix) {
        document.setArrSuffix(arrSuffix);
    }

    @Override
    public Set<Controller> getControllers() {
        Set<Controller> controllers = document.getControllers();
        if (controllers == null) return null;
        Set<Controller> set = new LinkedHashSet<>();
        for (Controller controller : controllers) set.add(new ObjCController(prefix, controller));
        return set;
    }

    @Override
    public void setControllers(Set<Controller> controllers) {
        document.setControllers(controllers);
    }

    @Override
    public Map<String, Schema> getSchemas() {
        Map<String, Schema> schemas = document.getSchemas();
        if (schemas == null) return null;
        Map<String, Schema> map = new LinkedHashMap<>();
        for (Map.Entry<String, Schema> entry : schemas.entrySet()) map.put(entry.getKey(), new ObjCSchema(prefix, entry.getValue()));
        return map;
    }

    @Override
    public void setSchemas(Map<String, Schema> schemas) {
        document.setSchemas(schemas);
    }

    @Override
    public String getDescription() {
        return document.getDescription();
    }

    @Override
    public void setDescription(String description) {
        document.setDescription(description);
    }
}
