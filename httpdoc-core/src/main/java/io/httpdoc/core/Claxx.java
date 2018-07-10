package io.httpdoc.core;

/**
 * 待生成的类
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-04 20:28
 **/
public class Claxx {
    private String path;
    private Src<Preference> src;
    private Preference preference;

    public Claxx(String path, Src<Preference> src, Preference preference) {
        this.path = path;
        this.src = src;
        this.preference = preference;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Src<Preference> getSrc() {
        return src;
    }

    public void setSrc(Src<Preference> src) {
        this.src = src;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }
}
