package io.httpdoc.core.strategy;

import io.httpdoc.core.Claxx;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/**
 * 生成任务
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-09 14:18
 **/
public class Task implements Iterable<Claxx> {
    private final String directory;
    private final Collection<Claxx> classes;

    public Task(String directory, Claxx claxx) {
        this(directory, Collections.singleton(claxx));
    }

    public Task(String directory, Collection<Claxx> classes) {
        this.directory = directory;
        this.classes = classes;
    }

    @Override
    public Iterator<Claxx> iterator() {
        return classes != null ? classes.iterator() : Collections.<Claxx>emptyList().iterator();
    }

    public String getDirectory() {
        return directory;
    }

    public Collection<Claxx> getClasses() {
        return classes;
    }

}
