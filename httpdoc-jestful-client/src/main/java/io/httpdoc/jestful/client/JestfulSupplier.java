package io.httpdoc.jestful.client;

import io.httpdoc.core.Schema;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.supplier.SystemSupplier;
import org.qfox.jestful.client.Part;

import java.lang.reflect.Type;

/**
 * Jestful 类型提供器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-16 13:39
 **/
public class JestfulSupplier implements Supplier {
    private final Supplier supplier;

    public JestfulSupplier() {
        this(new SystemSupplier());
    }

    public JestfulSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean contains(Type type) {
        return supplier.contains(type);
    }

    @Override
    public Schema acquire(Type type) {
        return supplier.acquire(type);
    }

    @Override
    public boolean contains(Schema schema) {
        return supplier.contains(schema);
    }

    @Override
    public Type acquire(Schema schema) {
        if ("File".equals(schema.getName())) return Part.class;
        return supplier.acquire(schema);
    }

}
