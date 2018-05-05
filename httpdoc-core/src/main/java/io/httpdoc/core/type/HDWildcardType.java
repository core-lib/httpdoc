package io.httpdoc.core.type;

import java.util.ArrayList;
import java.util.List;

/**
 * 通配类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-04 15:20
 **/
public class HDWildcardType extends HDType {
    private HDType upperBound;
    private HDType lowerBound;

    HDWildcardType() {
    }

    public HDWildcardType(HDType upperBound, HDType lowerBound) {
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
    }

    @Override
    public CharSequence getFormatName() {
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        if (lowerBound != null) builder.append(" super ").append(lowerBound.getFormatName());
        else if (upperBound != null) builder.append(" extends ").append(upperBound.getFormatName());
        return builder;
    }

    @Override
    public List<String> imports() {
        List<String> imports = new ArrayList<>();
        if (lowerBound != null) imports.addAll(lowerBound.imports());
        else if (upperBound != null) imports.addAll(upperBound.imports());
        return imports;
    }

    public HDType getUpperBound() {
        return upperBound;
    }

    void setUpperBound(HDType upperBound) {
        this.upperBound = upperBound;
    }

    public HDType getLowerBound() {
        return lowerBound;
    }

    void setLowerBound(HDType lowerBound) {
        this.lowerBound = lowerBound;
    }
}
