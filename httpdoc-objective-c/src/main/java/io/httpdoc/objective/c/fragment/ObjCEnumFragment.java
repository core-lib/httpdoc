package io.httpdoc.objective.c.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.EnterMergedAppender;
import io.httpdoc.core.appender.IndentAppender;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.ConstantFragment;
import io.httpdoc.objective.c.ObjC;
import io.httpdoc.objective.c.type.ObjCClass;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Objective-C 枚举类型代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-12 13:02
 **/
public class ObjCEnumFragment extends ObjCClassFragment {

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        // 注释
        if (commentFragment != null) commentFragment.joinTo(appender, preference);

        // 导入
        Set<String> imports = new TreeSet<>(this.imports());
        for (String dependency : imports) {
            if (dependency.startsWith("<") && dependency.endsWith(">")) appender.append("#import ").append(dependency).enter();
            else if (dependency.endsWith("\"") && dependency.endsWith("\"")) appender.append("#import ").append(dependency).enter();
            else appender.append("@class ").append(dependency).append(";").enter();
        }
        appender.enter();

        switch (clazz.getCategory()) {
            case ENUM: {
                appender.append("typedef NS_ENUM(NSInteger, ").append(((ObjCClass) clazz).getSimpleName()).append("){");
                EnterMergedAppender indented = new EnterMergedAppender(new IndentAppender(appender, preference.getIndent()), 2);
                // 枚举常量
                for (int i = 0; i < constantFragments.size(); i++) {
                    if (i == 0) indented.enter();
                    ConstantFragment fragment = constantFragments.get(i);
                    fragment.joinTo(indented, preference);
                    indented.append(" = ").append(String.valueOf(i));
                    if (i == constantFragments.size() - 1) indented.enter();
                    else indented.append(",").enter();
                }
                appender.append("};");
            }
            break;
            case INTERFACE: {
                String enumName = ((ObjCClass) clazz).getSimpleName();
                appender.append("typedef NSString * ").append(enumName).append(" NS_STRING_ENUM;").enter();
                appender.enter();
                for (ConstantFragment fragment : constantFragments) {
                    fragment.joinTo(appender, preference);
                    appender.enter();
                }
                appender.enter();
            }
            break;
            case CLASS: {
                for (ConstantFragment fragment : constantFragments) {
                    fragment.joinTo(appender, preference);
                    appender.enter();
                }
            }
            break;

        }
    }

    @Override
    public Set<String> imports() {
        switch (clazz.getCategory()) {
            case INTERFACE:
                return Collections.singleton(ObjC.FOUNDATION);
            case CLASS:
                return Collections.singleton("\"" + ((ObjCClass) clazz).getSimpleName() + ".h\"");
            default:
                return Collections.emptySet();
        }
    }

}