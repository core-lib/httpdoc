package io.httpdoc.core.strategy;

import io.httpdoc.core.Claxx;

import java.io.File;
import java.io.IOException;

/**
 * 备份策略
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-04 21:07
 **/
public class BackupStrategy extends FilterStrategy implements Strategy {
    private final String prefix;
    private final String suffix;

    public BackupStrategy() {
        this(new OverrideStrategy());
    }

    public BackupStrategy(Strategy strategy) {
        this(strategy, "", ".bak");
    }

    public BackupStrategy(String prefix, String suffix) {
        this(new OverrideStrategy(), prefix, suffix);
    }

    public BackupStrategy(Strategy strategy, String prefix, String suffix) {
        super(strategy);
        this.prefix = prefix != null ? prefix : "";
        this.suffix = suffix != null ? suffix : "";
    }

    @Override
    public void execute(Task task) throws IOException {
        String directory = task.getDirectory();
        for (Claxx claxx : task) {
            String path = directory + claxx.getPath();
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                String dir = file.getParent();
                String name = prefix + file.getName() + suffix;
                File dest = new File(dir, name);
                int count = 0;
                while (dest.exists()) dest = new File(dir, prefix + file.getName() + suffix + (++count));
                if (!file.renameTo(dest)) throw new IOException("can not rename file from [" + file + "] to [" + dest + "]");
            }
            super.execute(new Task(directory, claxx));
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}
