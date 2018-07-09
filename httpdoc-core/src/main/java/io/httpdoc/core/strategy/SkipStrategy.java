package io.httpdoc.core.strategy;

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
    public void reply(String directory, Claxx claxx) throws IOException {
        String path = directory + claxx.getPath();
        File file = new File(path);
        if (file.exists()) return;
        super.reply(directory, claxx);
    }

}
