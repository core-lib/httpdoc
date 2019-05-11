package io.httpdoc.core.picker;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 混合挑选器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/11 11:29
 */
public abstract class MixPicker implements Picker {
    protected final Set<Picker> pickers;

    protected MixPicker() {
        this(null);
    }

    protected MixPicker(Collection<? extends Picker> pickers) {
        this.pickers = pickers != null ? new LinkedHashSet<>(pickers) : new LinkedHashSet<Picker>();
    }

    public boolean add(Picker picker) {
        return pickers.add(picker);
    }

    public boolean remove(Picker picker) {
        return pickers.remove(picker);
    }

    public abstract MixPicker mix(Picker picker);

}
