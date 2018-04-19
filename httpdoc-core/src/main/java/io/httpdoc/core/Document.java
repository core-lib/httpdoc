package io.httpdoc.core;

import io.httpdoc.core.conversion.Format;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 文档
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-12 19:06
 **/
public class Document extends Definition {
    private static final long serialVersionUID = 4240537886514527060L;

    private String httpdoc;
    private String protocol;
    private String hostname;
    private String ctxtpath;
    private String version;
    private String refPrefix = Format.REF_PREFIX;
    private String refSuffix = Format.REF_SUFFIX;
    private String mapPrefix = Format.MAP_PREFIX;
    private String mapSuffix = Format.MAP_SUFFIX;
    private String arrPrefix = Format.ARR_PREFIX;
    private String arrSuffix = Format.ARR_SUFFIX;
    private List<Controller> controllers = new ArrayList<>();
    private Map<String, Schema> schemas = new LinkedHashMap<>();

    public String getHttpdoc() {
        return httpdoc;
    }

    public void setHttpdoc(String httpdoc) {
        this.httpdoc = httpdoc;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getCtxtpath() {
        return ctxtpath;
    }

    public void setCtxtpath(String ctxtpath) {
        this.ctxtpath = ctxtpath;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRefPrefix() {
        return refPrefix;
    }

    public void setRefPrefix(String refPrefix) {
        this.refPrefix = refPrefix;
    }

    public String getRefSuffix() {
        return refSuffix;
    }

    public void setRefSuffix(String refSuffix) {
        this.refSuffix = refSuffix;
    }

    public String getMapPrefix() {
        return mapPrefix;
    }

    public void setMapPrefix(String mapPrefix) {
        this.mapPrefix = mapPrefix;
    }

    public String getMapSuffix() {
        return mapSuffix;
    }

    public void setMapSuffix(String mapSuffix) {
        this.mapSuffix = mapSuffix;
    }

    public String getArrPrefix() {
        return arrPrefix;
    }

    public void setArrPrefix(String arrPrefix) {
        this.arrPrefix = arrPrefix;
    }

    public String getArrSuffix() {
        return arrSuffix;
    }

    public void setArrSuffix(String arrSuffix) {
        this.arrSuffix = arrSuffix;
    }

    public List<Controller> getControllers() {
        return controllers;
    }

    public void setControllers(List<Controller> controllers) {
        this.controllers = controllers;
    }

    public Map<String, Schema> getSchemas() {
        return schemas;
    }

    public void setSchemas(Map<String, Schema> schemas) {
        this.schemas = schemas;
    }

}
