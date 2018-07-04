package io.httpdoc.core.strategy;

import io.httpdoc.core.Preference;
import io.httpdoc.core.fragment.ClassFragment;

/**
 * 待生成的类
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-04 20:28
 **/
public class Claxx {
    private String path;
    private ClassFragment classFragment;
    private Preference preference;

    public Claxx(String path, ClassFragment classFragment, Preference preference) {
        this.path = path;
        this.classFragment = classFragment;
        this.preference = preference;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ClassFragment getClassFragment() {
        return classFragment;
    }

    public void setClassFragment(ClassFragment classFragment) {
        this.classFragment = classFragment;
    }

    public Preference getPreference() {
        return preference;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }
}
