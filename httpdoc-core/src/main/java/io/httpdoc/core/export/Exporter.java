package io.httpdoc.core.export;

import io.httpdoc.core.Document;

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
     * @param document 文档
     * @param folder   导出目录
     * @throws IOException I/O 异常
     */
    void export(Document document, String folder) throws IOException;

}
