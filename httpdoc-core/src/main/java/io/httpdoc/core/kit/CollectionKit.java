package io.httpdoc.core.kit;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class CollectionKit {

    public static <T> List<T> merge(List<T>... lists) {
        List<T> merged = new ArrayList<>();
        for (List<T> list : lists) merged.addAll(list);
        return merged;
    }

    public static <T> Set<T> merge(Set<T>... sets) {
        Set<T> merged = new LinkedHashSet<>();
        for (Set<T> list : sets) merged.addAll(list);
        return merged;
    }

}
