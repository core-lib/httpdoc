package io.httpdoc.objective.c.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.EnterMergedAppender;
import io.httpdoc.core.appender.IndentAppender;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.ClassFragment;
import io.httpdoc.core.fragment.ConstantFragment;
import io.httpdoc.core.fragment.FieldFragment;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.core.type.HDTypeVariable;
import io.httpdoc.objective.c.type.ObjC;
import io.httpdoc.objective.c.type.ObjCClass;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Objective-C类型碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-10 18:37
 **/
public class ObjCClassFragment extends ClassFragment {

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

        // 静态属性
        for (FieldFragment fragment : fieldFragments) {
            if (!Modifier.isStatic(fragment.modifier)) continue;
            fragment.joinTo(appender, preference);
            appender.enter();
        }
        appender.enter();

        // 声明
        switch (clazz.getCategory()) {
            case INTERFACE:
                appender.append("@interface ").append(((ObjCClass) clazz).getSimpleName());
                HDTypeVariable[] typeParameters = clazz.getTypeParameters();
                for (int i = 0; typeParameters != null && i < typeParameters.length; i++) {
                    if (i == 0) appender.append("<");
                    else appender.append(", ");
                    appender.append("__covariant " + typeParameters[i].getAbbrName());
                    if (i == typeParameters.length - 1) appender.append(">");
                }
                appender.append(" : ");
                if (superclass != null) appender.append(((ObjCClass) superclass).getSimpleName());
                else appender.append("NSObject");
                for (int i = 0; interfaces != null && i < interfaces.size(); i++) {
                    if (i == 0) appender.append("<");
                    else appender.append(", ");
                    appender.append(interfaces.get(i));
                    if (i == interfaces.size() - 1) appender.append(">");
                }
                break;
            case CLASS:
                appender.append("@interface ").append(((ObjCClass) clazz).getSimpleName()).append(" ()").enter();
                appender.enter();
                appender.append("@end").enter();
                appender.enter();
                appender.append("@implementation ").append(((ObjCClass) clazz).getSimpleName());
                break;
            case ENUM:
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
                return;
            default:
                throw new IllegalStateException();
        }
        appender.enter().enter();

        // 类型开始

        // 实例属性
        for (FieldFragment fragment : fieldFragments) {
            if (Modifier.isStatic(fragment.modifier)) continue;
            fragment.joinTo(appender, preference);
            appender.enter();
        }
        appender.enter();

        // 构造器
        for (Fragment constructor : constructorFragments) {
            constructor.joinTo(appender, preference);
            appender.enter();
        }
        appender.enter();

        // 方法
        for (Fragment fragment : methodFragments) {
            fragment.joinTo(appender, preference);
            appender.enter();
        }
        appender.enter();

        // 类型结束
        appender.append("@end").enter();
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new LinkedHashSet<>();
        switch (clazz.getCategory()) {
            case CLASS:
                imports.add("\"" + ((ObjCClass) clazz).getSimpleName() + ".h\"");
                break;
            case INTERFACE:
                imports.add(ObjC.FOUNDATION);
                imports.addAll(super.imports());
                if (superclass != null) {
                    imports.remove(((ObjCClass) superclass).getSimpleName());
                    imports.add("\"" + ((ObjCClass) superclass).getSimpleName() + ".h\"");
                }
                break;
            case ENUM:
                imports.add(ObjC.FOUNDATION);
                break;
        }

        return imports;
    }

}
