package io.httpdoc.core.fragment;

import io.httpdoc.core.type.HDType;

/**
 * Setter方法碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:35
 **/
public class SetterMethodFragment extends MethodFragment {

    public SetterMethodFragment(HDType type, String name) {
        this.resultFragment = new ResultFragment(HDType.valueOf(void.class));
        this.name = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        ParameterFragment parameter = new ParameterFragment(type, name);
        this.parameterFragments.add(parameter);
        this.blockFragment = new BlockFragment("this." + name + " = " + name + ";");
    }

}
