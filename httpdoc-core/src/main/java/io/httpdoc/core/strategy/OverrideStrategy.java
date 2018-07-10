package io.httpdoc.core.strategy;

import io.httpdoc.core.Claxx;
import io.httpdoc.core.Preference;
import io.httpdoc.core.Src;
import io.httpdoc.core.appender.WriterAppender;
import io.httpdoc.core.kit.IOKit;

import java.io.*;

/**
 * 覆盖策略
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-04 20:34
 **/
public class OverrideStrategy implements Strategy {

    @Override
    public void execute(Task task) throws IOException {
        String directory = task.getDirectory();
        for (Claxx claxx : task) {
            OutputStream out = null;
            Writer writer = null;
            try {
                String path = directory + claxx.getPath();
                File file = new File(path);
                File folder = file.getParentFile();
                if (!folder.exists() && !folder.mkdirs()) throw new IOException("could not create directory : " + folder);
                out = new FileOutputStream(file);
                writer = new OutputStreamWriter(out);
                Src<Preference> src = claxx.getSrc();
                Preference preference = claxx.getPreference();
                WriterAppender appender = new WriterAppender(writer);
                src.joinTo(appender, preference);
                writer.flush();
                out.flush();
            } finally {
                IOKit.close(writer);
                IOKit.close(out);
            }
        }
    }

}
