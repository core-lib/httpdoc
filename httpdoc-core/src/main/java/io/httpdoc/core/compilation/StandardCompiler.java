package io.httpdoc.core.compilation;

import io.httpdoc.core.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽象的文档编译器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-16 14:40
 **/
public abstract class StandardCompiler implements Compiler {

    @Override
    public void compile(Document document, OutputStream out) throws IOException {
        try (OutputStreamWriter writer = new OutputStreamWriter(out)) {
            compile(document, writer);
        }
    }

    @Override
    public void compile(Document document, Writer writer) throws IOException {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("httpdoc", document.getHttpdoc());
        map.put("protocol", document.getProtocol());
        map.put("hostname", document.getHostname());
        map.put("ctxtpath", document.getCtxtpath());
        map.put("version", document.getVersion());
        map.put("controllers", convert(document.getControllers()));
        map.put("schemas", convert(document.getSchemas()));
        serialize(map, writer);
    }

    protected abstract void serialize(Map<String, Object> doc, Writer writer) throws IOException;

    protected List<Map<String, Object>> convert(List<Controller> controllers) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Controller controller : controllers) list.add(convert(controller));
        return list;
    }

    protected Map<String, Object> convert(Controller controller) {
        return null;
    }

    protected Map<String, Map<String, Object>> convert(Map<String, Schema> schemas) {
        Map<String, Map<String, Object>> map = new LinkedHashMap<>();
        for (Map.Entry<String, Schema> entry : schemas.entrySet()) {
            String name = entry.getKey();
            Schema schema = entry.getValue();
            Map<String, Object> m = convert(schema);
            if (m == null) continue;
            map.put(name, m);
        }
        return map;
    }

    protected Map<String, Object> convert(Schema schema) {
        Category category = schema.getCategory();
        Map<String, Object> map = new LinkedHashMap<>();
        switch (category) {
            case BASIC:
                return null;
            case DICTIONARY:
                return null;
            case ARRAY:
                return null;
            case ENUM:
                List<Constant> constants = schema.getConstants();
                boolean commented = false;
                for (Constant constant : constants) commented = commented || constant.getDescription() != null;
                if (commented) {
                    Map<String, String> m = new LinkedHashMap<>();
                    for (Constant constant : constants) m.put(constant.getName(), constant.getDescription());
                    map.put("constants", m);
                } else {
                    List<String> names = new ArrayList<>();
                    for (Constant constant : constants) names.add(constant.getName());
                    map.put("constants", names);
                }
                break;
            case OBJECT:

                break;
        }
        return map;
    }

}
