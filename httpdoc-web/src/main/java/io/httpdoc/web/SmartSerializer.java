package io.httpdoc.web;

import io.httpdoc.core.serialization.Serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

/**
 * 智能序列化器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-24 13:07
 **/
public class SmartSerializer implements Serializer{

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public void serialize(Map<String, Object> doc, OutputStream out) throws IOException {

    }

    @Override
    public void serialize(Map<String, Object> doc, Writer writer) throws IOException {

    }

}
