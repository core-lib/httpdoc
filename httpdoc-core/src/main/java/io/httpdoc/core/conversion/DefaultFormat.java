package io.httpdoc.core.conversion;

/**
 * 默认转换格式
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-18 15:10
 **/
public class DefaultFormat implements Format {

    @Override
    public String getRefPrefix() {
        return REF_PREFIX;
    }

    @Override
    public String getRefSuffix() {
        return REF_SUFFIX;
    }

    @Override
    public String getMapPrefix() {
        return MAP_PREFIX;
    }

    @Override
    public String getMapSuffix() {
        return MAP_SUFFIX;
    }

    @Override
    public String getArrPrefix() {
        return ARR_PREFIX;
    }

    @Override
    public String getArrSuffix() {
        return ARR_SUFFIX;
    }

    @Override
    public boolean isPkgIncluded() {
        return false;
    }

}
