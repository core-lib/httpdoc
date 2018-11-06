package io.httpdoc.spring.boot;

import io.httpdoc.core.conversion.Converter;
import io.httpdoc.core.conversion.StandardConverter;
import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.interpretation.SourceInterpreter;
import io.httpdoc.core.serialization.Serializer;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.supplier.SystemSupplier;
import io.httpdoc.core.translation.Translator;
import io.httpdoc.web.HttpdocConversionProvider;
import io.httpdoc.web.HttpdocFilterSupport;
import io.httpdoc.web.HttpdocMergedTranslator;
import io.httpdoc.web.HttpdocSuffixSerializer;
import io.httpdoc.web.conversion.ConversionProvider;
import org.springframework.context.annotation.Import;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.lang.annotation.*;

/**
 * HttpDoc 框架与 Spring Boot 集成注解
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-08-08 9:45
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(HttpdocFilterRegistrar.class)
public @interface EnableHttpdoc {

    /**
     * @return Filter name
     */
    String name() default "httpdoc";

    /**
     * @return Filter bean name
     */
    String bean() default "httpdoc";

    /**
     * @return Filter async supported
     */
    boolean asyncSupported() default true;

    /**
     * @return Filter dispatcher type supported, empty means all.
     */
    DispatcherType[] dispatcherTypes() default {DispatcherType.REQUEST};

    /**
     * @return Filter match after
     */
    boolean matchAfter() default false;

    /**
     * @return Filter enabled
     */
    boolean enabled() default true;

    /**
     * @return URL patterns
     */
    String[] value() default {"/httpdoc", "/httpdoc.yaml", "/httpdoc.json", "/httpdoc.xml"};

    /**
     * @return Filter Class
     */
    Class<? extends Filter> filter() default HttpdocFilterSupport.class;

    /**
     * @return Filter Order
     */
    int order() default Integer.MAX_VALUE;

    /**
     * @return Filter Init Parameters
     */
    Param[] params() default {};

    /**
     * @return HttpDoc Version
     */
    String httpdoc() default "";

    /**
     * @return Network Protocol
     */
    String protocol() default "";

    /**
     * @return Server Hostname
     */
    String hostname() default "";

    /**
     * @return Server Port
     */
    int port() default -1;

    /**
     * @return Server Context Path
     */
    String context() default "";

    /**
     * @return Server API Version
     */
    String version() default "";

    /**
     * @return Document charset
     */
    String charset() default "";

    /**
     * @return Document Content Type
     */
    String contentType() default "";

    /**
     * @return Document Translator
     */
    Class<? extends Translator> translator() default HttpdocMergedTranslator.class;

    /**
     * @return Document Supplier
     */
    Class<? extends Supplier> supplier() default SystemSupplier.class;

    /**
     * @return Document Interpreter
     */
    Class<? extends Interpreter> interpreter() default SourceInterpreter.class;

    /**
     * @return Document Converter
     */
    Class<? extends Converter> converter() default StandardConverter.class;

    /**
     * @return Document Serializer
     */
    Class<? extends Serializer> serializer() default HttpdocSuffixSerializer.class;

    /**
     * @return Document Conversion Provider
     */
    Class<? extends ConversionProvider> conversionProvider() default HttpdocConversionProvider.class;

}
