package io.httpdoc.spring.mvc;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

public abstract class MultipartKit {

    public static boolean isMultipartFile(Type type) {
        return type instanceof Class<?>
                && (MultipartFile.class.isAssignableFrom((Class<?>) type) || Part.class.isAssignableFrom((Class<?>) type));
    }

    public static boolean isMultipartFiles(Type type) {
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            return clazz.isArray() && isMultipartFile(clazz.getComponentType());
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class<?>)) return false;
            Class<?> clazz = (Class<?>) rawType;
            if (!Collection.class.isAssignableFrom(clazz)) return false;
            Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
            return isMultipartFile(actualTypeArgument);
        } else {
            return false;
        }
    }

}
