package io.httpdoc.core.appender;

import io.httpdoc.core.kit.IOKit;

import java.io.*;

public class FileAppender extends EnterableAppender<FileAppender> implements LineAppender<FileAppender> {
    private final FileOutputStream fos;
    private final OutputStreamWriter osw;
    private final BufferedWriter br;

    public FileAppender(String filepath) throws IOException {
        this(filepath != null ? new File(filepath) : null);
    }

    public FileAppender(File file) throws IOException {
        if (file == null) throw new NullPointerException();
        File directory = file.getParentFile();
        if (!directory.exists() && !directory.mkdirs()) throw new IOException("could not create directory : " + directory);
        br = new BufferedWriter(osw = new OutputStreamWriter(fos = new FileOutputStream(file)));
    }

    @Override
    public FileAppender append(char c) throws IOException {
        br.write(c);
        return this;
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (br != null) br.flush();
        if (osw != null) osw.flush();
        if (fos != null) fos.flush();
        IOKit.close(br);
        IOKit.close(osw);
        IOKit.close(fos);
    }
}
