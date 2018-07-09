package io.httpdoc.core.strategy;

import java.io.IOException;

/**
 * 过滤器策略
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-09 13:28
 **/
public abstract class FilterStrategy implements Strategy {
    protected final Strategy strategy;

    protected FilterStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public void execute(Task task) throws IOException {
        strategy.execute(task);
    }

}
