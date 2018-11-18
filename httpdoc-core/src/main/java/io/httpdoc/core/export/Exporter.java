package io.httpdoc.core.export;

import java.io.IOException;

/**
 * 导出器
 */
public interface Exporter {

    /**
     * @return 平台
     */
    String platform();

    /**
     * @return 框架
     */
    String framework();

    /**
     * 导出
     *
     * @param url    文档地址
     * @param folder 导出目录
     * @throws IOException I/O 异常
     */
    void export(String url, String folder) throws IOException;

}
