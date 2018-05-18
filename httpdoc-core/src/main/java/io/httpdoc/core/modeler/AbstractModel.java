package io.httpdoc.core.modeler;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * 抽象方案
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-18 11:20
 **/
public abstract class AbstractModel implements Model {

    @Override
    public void buildTo(OutputStream out) throws IOException {
        try (Writer writer = new OutputStreamWriter(out)) {
            buildTo(writer);
        }
    }

}
