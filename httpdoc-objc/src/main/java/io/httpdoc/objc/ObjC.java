package io.httpdoc.objc;

/**
 * ObjC 系统类型
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-27 14:10
 **/
public interface ObjC {

    String FOUNDATION = "<Foundation/Foundation.h>";

    String getName();

    Kind getKind();

    Reference getReference();

    String getLocation();

    boolean isExternal();

    enum Kind {
        CLASS,
        PROTOCOL,
        PRIMITIVE,
        TYPEDEF,
        BLOCK,
        GENERIC
    }

    enum Reference {
        STRONG,
        WEAK,
        COPY,
        ASSIGN
    }

}
