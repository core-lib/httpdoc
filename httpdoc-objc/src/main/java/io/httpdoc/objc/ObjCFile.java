package io.httpdoc.objc;

import io.httpdoc.core.fragment.Fragment;

/**
 * ObjC源文件
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-25 16:49
 **/
public class ObjCFile {
    private final String pkg;
    private final Fragment fragment;

    public ObjCFile(String pkg, Fragment fragment) {
        this.pkg = pkg;
        this.fragment = fragment;
    }

    public String getPkg() {
        return pkg;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
