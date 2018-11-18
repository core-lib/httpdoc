package io.httpdoc.web;

import io.httpdoc.core.Config;
import io.httpdoc.core.Document;
import io.httpdoc.core.Lifecycle;
import io.httpdoc.core.export.Exporter;
import io.httpdoc.core.kit.IOKit;
import io.httpdoc.core.kit.LoadKit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

public class HttpdocExportHandler implements Handler, Lifecycle {
    private final String root = System.getProperty("java.io.tmpdir");
    private Map<String, Exporter> exporters = LoadKit.load(this.getClass().getClassLoader(), Exporter.class);

    @Override
    public void handle(Document document, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sdk = request.getParameter("sdk");
        Exporter exporter = exporters.get(sdk);
        if (exporter == null) {
            response.sendError(HttpURLConnection.HTTP_NOT_FOUND, "Not Found");
            return;
        }
        UUID random = UUID.randomUUID();
        File folder = new File(root, random + File.separator + sdk + "-sdk");
        try {
            if (!folder.exists() && !folder.mkdirs()) {
                throw new IOException("could not make directory: " + folder);
            }
            exporter.export(document, folder.getPath());
            response.setContentType("application/x-zip-compressed");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(folder.getName() + ".zip", "UTF-8") + "\"");
            OutputStream out = response.getOutputStream();
            ZipKit.pack(folder, out);
        } finally {
            IOKit.delete(folder, true);
        }
    }

    @Override
    public void initial(Config config) throws Exception {
        for (Exporter exporter : exporters.values()) {
            if (exporter instanceof Lifecycle) {
                ((Lifecycle) exporter).initial(config);
            }
        }
    }

    @Override
    public void destroy() {
        for (Exporter exporter : exporters.values()) {
            if (exporter instanceof Lifecycle) {
                ((Lifecycle) exporter).destroy();
            }
        }
    }
}
