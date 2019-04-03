package io.httpdoc.retrofit2;

import io.httpdoc.core.Schema;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.supplier.SystemSupplier;
import okhttp3.RequestBody;

import java.lang.reflect.Type;

/**
 * Retrofit 类型提供器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-16 13:39
 **/
public class Retrofit2Supplier implements Supplier {
    private final Supplier supplier;

    public Retrofit2Supplier() {
        this(new SystemSupplier());
    }

    public Retrofit2Supplier(Supplier supplier) {
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
        if ("File".equals(schema.getName())) return RequestBody.class;
        else return supplier.acquire(schema);
    }
}
