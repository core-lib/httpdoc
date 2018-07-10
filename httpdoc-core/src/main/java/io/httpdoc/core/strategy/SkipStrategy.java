package io.httpdoc.core.strategy;

import io.httpdoc.core.Claxx;

import java.io.File;
import java.io.IOException;

/**
 * 跳过已存在策略
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-04 20:35
 **/
public class SkipStrategy extends FilterStrategy implements Strategy {

    public SkipStrategy() {
        this(new OverrideStrategy());
    }

    public SkipStrategy(Strategy strategy) {
        super(strategy);
    }

    @Override
    public void execute(Task task) throws IOException {
        String directory = task.getDirectory();
        for (Claxx claxx : task) {
            String path = directory + claxx.getPath();
            File file = new File(path);
            if (file.exists()) return;
            super.execute(new Task(directory, claxx));
        }
    }

}
