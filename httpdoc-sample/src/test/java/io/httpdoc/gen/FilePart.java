package io.httpdoc.gen;

import org.qfox.jestful.client.Part;
import org.qfox.jestful.commons.IOKit;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class FilePart implements Part {
    private final File file;

    public FilePart(String filepath) {
        this(new File(filepath));
    }

    public FilePart(File file) {
        this.file = file;
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public void writeTo(OutputStream out) throws IOException {
        IOKit.transfer(file, out);
    }
}
