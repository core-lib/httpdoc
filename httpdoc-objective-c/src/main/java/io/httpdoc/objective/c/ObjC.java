package io.httpdoc.objective.c;

import io.httpdoc.objective.c.type.ObjCReferenceType;

import java.util.Arrays;
import java.util.List;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-11 14:55
 **/
public interface ObjC {
    List<String> PRIMARIES = Arrays.asList("id", "instancetype", "bool", "short", "char", "int", "float", "long", "double");
    String FOUNDATION = "<Foundation/Foundation.h>";

    ObjCReferenceType getReferenceType();

}
