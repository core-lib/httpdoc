package io.httpdoc.core.conversion;

import io.httpdoc.core.*;

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
public class StandardConverter implements Converter {

    @Override
    public Map<String, Object> convert(Document document) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("httpdoc", document.getHttpdoc());
        map.put("protocol", document.getProtocol());
        map.put("hostname", document.getHostname());
        map.put("ctxtpath", document.getCtxtpath());
        map.put("version", document.getVersion());
        map.put("controllers", doConvert(document.getControllers()));
        map.put("schemas", doConvert(document.getSchemas()));
        return map;
    }

    protected List<Map<String, Object>> doConvert(List<Controller> controllers) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Controller controller : controllers) list.add(doConvert(controller));
        return list;
    }

    protected Map<String, Object> doConvert(Controller controller) {
        return null;
    }

    protected Map<String, Map<String, Object>> doConvert(Map<String, Schema> schemas) {
        Map<String, Map<String, Object>> map = new LinkedHashMap<>();
        for (Map.Entry<String, Schema> entry : schemas.entrySet()) {
            String name = entry.getKey();
            Schema schema = entry.getValue();
            Map<String, Object> m = doConvert(schema);
            if (m == null) continue;
            map.put(name, m);
        }
        return map;
    }

    protected Map<String, Object> doConvert(Schema schema) {
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
                Schema superclass = schema.getSuperclass();
                if (superclass != null) map.put("superclass", format(superclass));
                Map<String, Property> properties = schema.getProperties();
                Map<String, Object> m = new LinkedHashMap<>();
                for (Map.Entry<String, Property> entry : properties.entrySet()) {
                    String name = entry.getKey();
                    Property property = entry.getValue();
                    Schema s = property.getSchema();
                    String description = property.getDescription();
                    String reference = format(s);
                    if (description != null) {
                        Map<String, Object> p = new LinkedHashMap<>();
                        p.put("type", reference);
                        p.put("description", description);
                        m.put("name", p);
                    } else {
                        m.put(name, reference);
                    }
                }
                map.put("properties", m);
                break;
        }
        String description = schema.getDescription();
        if (description != null) map.put("description", description);
        return map;
    }

    protected String format(Schema schema) {
        Category category = schema.getCategory();
        switch (category) {
            case BASIC:
                return schema.getName();
            case DICTIONARY:
                return "Dictionary<String, " + format(schema.getComponent()) + ">";
            case ARRAY:
                return format(schema.getComponent()) + "[]";
            case ENUM:
                return "@/schemas/" + schema.getName();
            case OBJECT:
                return "@/schemas/" + schema.getName();
            default:
                return null;
        }
    }

    @Override
    public Document convert(Map<String, Object> dictionary) {
        return null;
    }
}
