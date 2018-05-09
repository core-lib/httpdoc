package io.httpdoc.core.appender;

import java.io.IOException;
import java.io.Writer;

public class WriterAppender extends EnterableAppender<WriterAppender> implements LineAppender<WriterAppender> {
    private final Writer writer;

    public WriterAppender(Writer writer) {
        if (writer == null) throw new NullPointerException();
        this.writer = writer;
    }

    @Override
    public WriterAppender append(char c) throws IOException {
        writer.write(c);
        return this;
    }

    @Override
    public void close() throws IOException {
        super.close();
        writer.close();
    }
}
