package io.httpdoc.core;

/**
 * 生命周期的
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/11/12
 */
public interface Lifecycle {

    /**
     * 初始化
     *
     * @param config 配置
     * @throws Exception 初始化异常
     */
    void initial(Config config) throws Exception;

    /**
     * 销毁
     */
    void destroy();

}
