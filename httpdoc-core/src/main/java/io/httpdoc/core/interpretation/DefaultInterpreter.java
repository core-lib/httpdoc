package io.httpdoc.core.interpretation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DefaultInterpreter implements Interpreter {

    @Override
    public ClassInterpretation interpret(Class<?> clazz) {
        return null;
    }

    @Override
    public MethodInterpretation interpret(Method method) {
        return null;
    }

    @Override
    public FieldInterpretation interpret(Field field) {
        return null;
    }

    @Override
    public EnumInterpretation interpret(Enum<?> constant) {
        return null;
    }

    @Override
    public FieldInterpretation interpret(PropertyDescriptor descriptor) {
        return null;
    }
}
