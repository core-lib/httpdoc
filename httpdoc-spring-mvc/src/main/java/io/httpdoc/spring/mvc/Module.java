package io.httpdoc.spring.mvc;

import io.httpdoc.core.Build;

import java.io.Serializable;

/**
 * Created by payne on 2017/3/28.
 */
public class Module extends Build implements Serializable {
    private static final long serialVersionUID = 8979830823718754930L;

    private static volatile Module instance;

    private String name;
    private String version;
    private String parentName;
    private String parentVersion;

    private Module() {
    }

    public static Module getInstance() {
        if (instance != null) return instance;
        synchronized (Module.class) {
            if (instance != null) return instance;
            return instance = new Module();
        }
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getParentName() {
        return parentName;
    }

    public String getParentVersion() {
        return parentVersion;
    }
}
