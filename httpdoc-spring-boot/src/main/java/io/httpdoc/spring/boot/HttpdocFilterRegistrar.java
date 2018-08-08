package io.httpdoc.spring.boot;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HttpDoc Filter 注册器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-08-08 9:54
 **/
public class HttpdocFilterRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableHttpdoc.class.getName()));
        BeanDefinition httpdoc = new RootBeanDefinition(FilterRegistrationBean.class);

        String name = attributes.getString("name");
        httpdoc.getPropertyValues().add("name", name);

        int order = attributes.getNumber("order");
        httpdoc.getPropertyValues().add("order", order);

        String[] patterns = attributes.getStringArray("value");
        httpdoc.getPropertyValues().add("urlPatterns", Arrays.asList(patterns));

        Class<?> filter = attributes.getClass("filter");
        httpdoc.getPropertyValues().add("filter", newInstance(filter));

        Map<String, String> parameters = new LinkedHashMap<>();
        AnnotationAttributes[] params = attributes.getAnnotationArray("params");
        for (AnnotationAttributes param : params) parameters.put(param.getString("name"), param.getString("value"));

        parameters.put("httpdoc", attributes.getString("httpdoc"));
        parameters.put("protocol", attributes.getString("protocol"));
        parameters.put("hostname", attributes.getString("hostname"));
        parameters.put("port", attributes.getNumber("port").toString());
        parameters.put("context", attributes.getString("context"));
        parameters.put("version", attributes.getString("version"));
        parameters.put("charset", attributes.getString("charset"));
        parameters.put("contentType", attributes.getString("contentType"));
        parameters.put("translator", attributes.getClass("translator").getName());
        parameters.put("supplier", attributes.getClass("supplier").getName());
        parameters.put("interpreter", attributes.getClass("interpreter").getName());
        parameters.put("converter", attributes.getClass("converter").getName());
        parameters.put("serializer", attributes.getClass("serializer").getName());
        parameters.put("conversionProvider", attributes.getClass("conversionProvider").getName());

        httpdoc.getPropertyValues().add("initParameters", parameters);

        registry.registerBeanDefinition("httpdoc", httpdoc);
    }

    private <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
