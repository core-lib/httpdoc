package io.httpdoc.core.description;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NullDescriber implements Describer {

    @Override
    public String describe(Class<?> clazz) {
        return null;
    }

    @Override
    public String describe(Method method) {
        return null;
    }

    @Override
    public String describe(Field field) {
        return null;
    }

    @Override
    public String describe(Enum<?> constant) {
        return null;
    }
}
