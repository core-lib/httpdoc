package io.httpdoc.core.interpretation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface Interpreter {

    ClassInterpretation interpret(Class<?> clazz) throws Exception;

    MethodInterpretation interpret(Method method);

    FieldInterpretation interpret(Field field);

    EnumInterpretation interpret(Enum<?> constant);

    Interpretation interpret(PropertyDescriptor descriptor);

}
