package io.httpdoc.springmvc;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(HttpdocSpringMVCConfiguration.class)
public @interface EnableHttpdoc {
}