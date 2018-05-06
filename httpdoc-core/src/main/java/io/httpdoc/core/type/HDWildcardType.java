package io.httpdoc.core.type;

import io.httpdoc.core.Importable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public void importTo(Map<Importable, List<String>> imports) {
        if (imports.containsKey(this)) return;
        else imports.put(this, Collections.<String>emptyList());
        if (upperBound != null) upperBound.importTo(imports);
        if (lowerBound != null) lowerBound.importTo(imports);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HDWildcardType that = (HDWildcardType) o;

        if (upperBound != null ? !upperBound.equals(that.upperBound) : that.upperBound != null) return false;
        return lowerBound != null ? lowerBound.equals(that.lowerBound) : that.lowerBound == null;
    }

    @Override
    public int hashCode() {
        int result = upperBound != null ? upperBound.hashCode() : 0;
        result = 31 * result + (lowerBound != null ? lowerBound.hashCode() : 0);
        return result;
    }
}
