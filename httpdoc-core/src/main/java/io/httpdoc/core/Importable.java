package io.httpdoc.core;

import java.util.List;
import java.util.Map;

/**
 * 可导入的
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-04 10:16
 **/
public interface Importable {

    void importTo(Map<Importable, List<String>> imports);

    int hashCode();

    boolean equals(Object that);

}
