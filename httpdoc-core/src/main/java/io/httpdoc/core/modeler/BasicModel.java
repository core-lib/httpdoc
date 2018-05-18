package io.httpdoc.core.modeler;

import io.httpdoc.core.Preference;
import io.httpdoc.core.Src;
import io.httpdoc.core.appender.WriterAppender;

import java.io.IOException;
import java.io.Writer;

/**
 * 基础方案
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-18 11:19
 **/
public class BasicModel extends AbstractModel implements Model {
    private final Src<Preference> src;
    private final Preference preference;

    public BasicModel(Src<Preference> src, Preference preference) {
        this.src = src;
        this.preference = preference;
    }

    @Override
    public void buildTo(Writer writer) throws IOException {
        src.joinTo(new WriterAppender(writer), preference);
    }

}
