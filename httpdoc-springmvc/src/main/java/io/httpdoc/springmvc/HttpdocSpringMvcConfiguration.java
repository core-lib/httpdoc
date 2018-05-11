package io.httpdoc.springmvc;

import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.interpretation.SourceInterpreter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

@Configuration
@ComponentScan(basePackages = {
        "io.httpdoc.springmvc"
})
public class HttpdocSpringMvcConfiguration {

    @Bean
    public ParameterNameDiscoverer parameterNameDiscoverer() {
        return new DefaultParameterNameDiscoverer();
    }

    @Bean
    public Interpreter interpreter() {
        return new SourceInterpreter();
    }
}