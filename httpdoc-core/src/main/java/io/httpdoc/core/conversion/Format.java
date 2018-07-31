package io.httpdoc.core.conversion;

import java.io.Serializable;

/**
 * 转换格式
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-18 15:07
 **/
public interface Format extends Serializable {

    String REF_PREFIX = "$/schemas/";
    String REF_SUFFIX = "";
    String MAP_PREFIX = "Dictionary<String,";
    String MAP_SUFFIX = ">";
    String ARR_PREFIX = "";
    String ARR_SUFFIX = "[]";

    String getRefPrefix();

    String getRefSuffix();

    String getMapPrefix();

    String getMapSuffix();

    String getArrPrefix();

    String getArrSuffix();

    boolean isPkgIncluded();

    boolean isCanonical();

}
