package io.httpdoc.core.decode;

import io.httpdoc.core.conversion.StandardConverter;
import io.httpdoc.core.deserialization.YamlDeserializer;

/**
 * 缺省的文档解码器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-17 10:32
 **/
public class DefaultDecoder extends CompositeDecoder {

    public DefaultDecoder() {
        super(new StandardConverter(), new YamlDeserializer());
    }

}
