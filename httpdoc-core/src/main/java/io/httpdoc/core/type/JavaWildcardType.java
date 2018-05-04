package io.httpdoc.core.type;

import java.util.ArrayList;
import java.util.List;

/**
 * 通配类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-04 15:20
 **/
public class JavaWildcardType extends JavaType {
    private JavaType[] upperBounds;
    private JavaType[] lowerBounds;

    public JavaWildcardType(JavaType[] upperBounds, JavaType[] lowerBounds) {
        this.upperBounds = upperBounds;
        this.lowerBounds = lowerBounds;
    }

    @Override
    public CharSequence getFormatName() {
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        for (int i = 0; upperBounds != null && i < upperBounds.length; i++) {
            if (i == 0) builder.append(" extends ");
            else builder.append(", ");
            builder.append(upperBounds[i].getFormatName());
        }
        for (int i = 0; lowerBounds != null && i < lowerBounds.length; i++) {
            if (i == 0) builder.append(" super ");
            else builder.append(", ");
            builder.append(lowerBounds[i].getFormatName());
        }
        return builder;
    }

    @Override
    public List<String> imports() {
        List<String> imports = new ArrayList<>();
        for (int i = 0; upperBounds != null && i < upperBounds.length; i++) imports.addAll(upperBounds[i].imports());
        for (int i = 0; lowerBounds != null && i < lowerBounds.length; i++) imports.addAll(lowerBounds[i].imports());
        return imports;
    }

    public JavaType[] getUpperBounds() {
        return upperBounds;
    }

    public JavaType[] getLowerBounds() {
        return lowerBounds;
    }

}
