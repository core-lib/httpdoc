package io.httpdoc.jestful;

import io.httpdoc.core.Document;
import io.httpdoc.core.Generation;
import io.httpdoc.core.Generator;
import io.httpdoc.core.Schema;

import java.util.Map;

/**
 * Jestful Client 生成器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 15:59
 **/
public class JestfulClientGenerator implements Generator {

    @Override
    public void generate(Generation generation) {
        Document document = generation.getDocument();
        String directory = generation.getDirectory();

        Map<String, Schema> schemas = document.getSchemas();


    }

}
