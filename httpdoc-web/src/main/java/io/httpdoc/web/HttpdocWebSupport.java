package io.httpdoc.web;

import io.httpdoc.core.Document;
import io.httpdoc.core.Lifecycle;
import io.httpdoc.core.Sdk;
import io.httpdoc.core.conversion.Converter;
import io.httpdoc.core.conversion.CustomFormat;
import io.httpdoc.core.conversion.Format;
import io.httpdoc.core.conversion.StandardConverter;
import io.httpdoc.core.export.Exporter;
import io.httpdoc.core.interpretation.Interpreter;
import io.httpdoc.core.interpretation.SourceInterpreter;
import io.httpdoc.core.kit.IOKit;
import io.httpdoc.core.kit.LoadKit;
import io.httpdoc.core.serialization.Serializer;
import io.httpdoc.core.supplier.Supplier;
import io.httpdoc.core.supplier.SystemSupplier;
import io.httpdoc.core.translation.Container;
import io.httpdoc.core.translation.Translation;
import io.httpdoc.core.translation.Translator;
import io.httpdoc.jackson.serialization.JsonSerializer;
import io.httpdoc.web.conversion.Conversion;
import io.httpdoc.web.conversion.ConversionProvider;
import io.httpdoc.web.conversion.ConvertingException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

/**
 * Httpdoc web 容器支持
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-23 16:26
 **/
public abstract class HttpdocWebSupport implements Handler {
    private String httpdoc;
    private String protocol;
    private String hostname;
    private Integer port;
    private String context;
    private String version;
    private String dateFormat = "yyyy-MM-dd HH:mm:ss";
    private String description;
    private String charset = "UTF-8";
    private String contentType = null;
    private Translator translator = new HttpdocMergedTranslator();
    private Supplier supplier = new SystemSupplier();
    private Interpreter interpreter = new SourceInterpreter();
    private Converter converter = new StandardConverter();
    private Serializer serializer = new JsonSerializer();
    private ConversionProvider conversionProvider = new HttpdocConversionProvider();
    private Format format = new CustomFormat();
    private Map<String, Handler> handlers = LoadKit.load(this.getClass().getClassLoader(), Handler.class);
    private Map<String, Exporter> exporters = LoadKit.load(this.getClass().getClassLoader(), Exporter.class);

    public void init(HttpdocWebConfig config) throws ServletException {
        try {
            String packages = config.getInitParameter("packages");
            if (packages == null || packages.trim().length() == 0) {
                throw new IllegalArgumentException("packages to scan is not defined");
            }
            String httpdoc = config.getInitParameter("httpdoc");
            if (httpdoc != null && httpdoc.trim().length() > 0) {
                this.httpdoc = httpdoc;
            }
            String protocol = config.getInitParameter("protocol");
            if (protocol != null && protocol.trim().length() > 0) {
                this.protocol = protocol;
            }
            String hostname = config.getInitParameter("hostname");
            if (hostname != null && hostname.trim().length() > 0) {
                this.hostname = hostname;
            }
            String port = config.getInitParameter("port");
            if (port != null && port.trim().length() > 0 && port.matches("\\d+")) {
                this.port = Integer.valueOf(port);
            }
            String context = config.getInitParameter("context");
            if (context != null && context.trim().length() > 0) {
                this.context = context;
            }
            String version = config.getInitParameter("version");
            if (version != null && version.trim().length() > 0) {
                this.version = version;
            }
            String dateFormat = config.getInitParameter("dateFormat");
            if (dateFormat != null && dateFormat.trim().length() > 0) {
                this.dateFormat = dateFormat;
            }
            String description = config.getInitParameter("description");
            if (description != null && description.trim().length() > 0) {
                this.description = description;
            }
            String charset = config.getInitParameter("charset");
            if (charset != null && charset.trim().length() > 0) {
                this.charset = charset;
            }
            String contentType = config.getInitParameter("contentType");
            if (contentType != null && contentType.trim().length() > 0) {
                this.contentType = contentType;
            }

            String translator = config.getInitParameter("translator");
            if (translator != null && translator.trim().length() > 0) {
                this.translator = Class.forName(translator).asSubclass(Translator.class).newInstance();
            }
            if (this.translator instanceof Lifecycle) {
                ((Lifecycle) this.translator).initial(config);
            }

            String provider = config.getInitParameter("supplier");
            if (provider != null && provider.trim().length() > 0) {
                this.supplier = Class.forName(provider).asSubclass(Supplier.class).newInstance();
            }
            if (this.supplier instanceof Lifecycle) {
                ((Lifecycle) this.supplier).initial(config);
            }

            String interpreter = config.getInitParameter("interpreter");
            if (interpreter != null && interpreter.trim().length() > 0) {
                this.interpreter = Class.forName(interpreter).asSubclass(Interpreter.class).newInstance();
            }
            if (this.interpreter instanceof Lifecycle) {
                ((Lifecycle) this.interpreter).initial(config);
            }

            String converter = config.getInitParameter("converter");
            if (converter != null && converter.trim().length() > 0) {
                this.converter = Class.forName(converter).asSubclass(Converter.class).newInstance();
            }
            if (this.converter instanceof Lifecycle) {
                ((Lifecycle) this.converter).initial(config);
            }

            String serializer = config.getInitParameter("serializer");
            if (serializer != null && serializer.trim().length() > 0) {
                this.serializer = Class.forName(serializer).asSubclass(Serializer.class).newInstance();
            }
            if (this.serializer instanceof Lifecycle) {
                ((Lifecycle) this.serializer).initial(config);
            }

            String conversionProvider = config.getInitParameter("conversionProvider");
            if (conversionProvider != null && conversionProvider.trim().length() > 0) {
                this.conversionProvider = Class.forName(conversionProvider).asSubclass(ConversionProvider.class).newInstance();
            }
            if (this.conversionProvider instanceof Lifecycle) {
                ((Lifecycle) this.conversionProvider).initial(config);
            }

            for (Handler handler : handlers.values()) {
                if (handler instanceof Lifecycle) {
                    ((Lifecycle) handler).initial(config);
                }
            }

            for (Exporter exporter : exporters.values()) {
                if (exporter instanceof Lifecycle) {
                    ((Lifecycle) exporter).initial(config);
                }
            }

            Enumeration<String> names = config.getInitParameterNames();
            while (names.hasMoreElements()) {
                String expression = names.nextElement();
                if (!expression.startsWith("format.")) continue;
                this.conversionProvider.convert(new Conversion(
                        "format",
                        format,
                        CustomFormat.class,
                        true,
                        "UTF-8",
                        expression,
                        new String[]{config.getInitParameter(expression)}
                ));
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void handle(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            HttpdocThreadLocal.bind(req, res);

            Container container = new HttpdocWebContainer(request.getServletContext());
            Translation translation = new Translation(container, supplier, interpreter);

            translation.setHttpdoc(httpdoc);
            translation.setProtocol(protocol != null ? protocol : req.getProtocol().split("/")[0].toLowerCase());
            translation.setHostname(hostname != null ? hostname : req.getServerName());
            translation.setPort(port != null ? port : req.getServerPort());
            translation.setContext(context != null ? context : req.getContextPath());
            translation.setVersion(version);
            translation.setDateFormat(dateFormat);
            translation.setDescription(description);

            Map<String, String[]> map = request.getParameterMap();
            assign("translation", translation, map);

            Document doc = translator.translate(translation);
            for (Map.Entry<String, Exporter> entry : exporters.entrySet()) {
                Sdk sdk = new Sdk();
                sdk.setPlatform(entry.getValue().platform());
                sdk.setFramework(entry.getKey());
                doc.getSdks().add(sdk);
            }

            String action = request.getParameter("action");

            Handler handler = handlers.containsKey(action) ? handlers.get(action) : this;
            handler.handle(doc, req, res);
        } catch (IOException | ServletException e) {
            throw e;
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            HttpdocThreadLocal.clear();
        }
    }

    public void handle(Document document, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String[]> map = request.getParameterMap();
        response.setCharacterEncoding(charset);
        response.setContentType(contentType != null ? contentType : serializer.getType() + "; charset=" + charset);
        Format clone = IOKit.clone(format);
        assign("format", clone, map);
        Map<String, Object> doc = converter.convert(document, clone);
        serializer.serialize(doc, response.getOutputStream());
    }

    private void assign(String name, Object object, Map<String, String[]> map) throws ConvertingException {
        if (object == null) throw new NullPointerException();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            conversionProvider.convert(new Conversion(
                    name,
                    object,
                    object.getClass(),
                    true,
                    "UTF-8",
                    key,
                    value
            ));
        }
    }

    public void destroy() {
        if (translator instanceof Lifecycle) {
            ((Lifecycle) translator).destroy();
        }
        if (supplier instanceof Lifecycle) {
            ((Lifecycle) supplier).destroy();
        }
        if (interpreter instanceof Lifecycle) {
            ((Lifecycle) interpreter).destroy();
        }
        if (converter instanceof Lifecycle) {
            ((Lifecycle) converter).destroy();
        }
        if (serializer instanceof Lifecycle) {
            ((Lifecycle) serializer).destroy();
        }
        if (conversionProvider instanceof Lifecycle) {
            ((Lifecycle) conversionProvider).destroy();
        }
        for (Handler handler : handlers.values()) {
            if (handler instanceof Lifecycle) {
                ((Lifecycle) handler).destroy();
            }
        }
        for (Exporter exporter : exporters.values()) {
            if (exporter instanceof Lifecycle) {
                ((Lifecycle) exporter).destroy();
            }
        }
    }

}
