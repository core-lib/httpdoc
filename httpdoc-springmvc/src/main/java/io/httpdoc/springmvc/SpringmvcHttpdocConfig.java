package io.httpdoc.springmvc;

import io.httpdoc.core.interpretation.DefaultInterpreter;
import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.supplier.SystemSupplier;
import org.springframework.stereotype.Component;

/**
 * SpringMVC 文档翻译配置
 *
 * @author 钟宝林
 * @date 2018-05-13 10:50
 **/
@Component
public class SpringmvcHttpdocConfig {

    private String httpdoc;
    private String protocol;
    private String hostname;
    private Integer port;
    private String context;
    private String version;
    private Supplier supplier = new SystemSupplier();
    private Interpreter interpreter = new DefaultInterpreter();

    public SpringmvcHttpdocConfig() {

    }

    private SpringmvcHttpdocConfig(Builder builder) {
        httpdoc = builder.httpdoc;
        protocol = builder.protocol;
        hostname = builder.hostname;
        port = builder.port;
        context = builder.context;
        version = builder.version;
        supplier = builder.supplier;
        interpreter = builder.interpreter;
    }

    public String getHttpdoc() {
        return httpdoc;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHostname() {
        return hostname;
    }

    public Integer getPort() {
        return port;
    }

    public String getContext() {
        return context;
    }

    public String getVersion() {
        return version;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public static class Builder {
        private String httpdoc;
        private String protocol;
        private String hostname;
        private Integer port;
        private String context;
        private String version;
        private Supplier supplier = new SystemSupplier();
        private Interpreter interpreter = new DefaultInterpreter();

        public Builder httpDoc(String httpdoc) {
            this.httpdoc = httpdoc;
            return this;
        }

        public Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder hostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder context(String context) {
            this.context = context;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder provider(Supplier supplier) {
            this.supplier = supplier;
            return this;
        }

        public Builder interpreter(Interpreter interpreter) {
            this.interpreter = interpreter;
            return this;
        }

        public SpringmvcHttpdocConfig build() {
            return new SpringmvcHttpdocConfig(this);
        }
    }

}
