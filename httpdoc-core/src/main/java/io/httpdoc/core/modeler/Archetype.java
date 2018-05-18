package io.httpdoc.core.modeler;

import io.httpdoc.core.Schema;
import io.httpdoc.core.provider.Provider;

/**
 * 结构
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-18 11:02
 **/
public class Archetype {
    private String pkg;
    private Provider provider;
    private Schema schema;

    public Archetype() {
    }

    public Archetype(String pkg, Provider provider, Schema schema) {
        this.pkg = pkg;
        this.provider = provider;
        this.schema = schema;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }
}
