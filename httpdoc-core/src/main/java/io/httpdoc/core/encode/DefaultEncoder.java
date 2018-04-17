package io.httpdoc.core.encode;

import io.httpdoc.core.conversion.StandardConverter;
import io.httpdoc.core.serialization.YamlSerializer;

/**
 * 缺省的文档编译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-17 10:10
 **/
public class DefaultEncoder extends CompositeEncoder {

    public DefaultEncoder() {
        super(new StandardConverter(), new YamlSerializer());
    }

}
