package io.httpdoc.core.modeler;

import io.httpdoc.core.Claxx;
import io.httpdoc.core.exception.SchemaDesignException;

/**
 * 模型师
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-18 10:48
 **/
public interface Modeler {

    /**
     * 设计
     *
     * @param archetype 原型
     * @return 模型
     * @throws SchemaDesignException Schema 不可设计的异常
     */
    Claxx design(Archetype archetype) throws SchemaDesignException;

}
