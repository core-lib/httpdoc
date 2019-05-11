package io.httpdoc.core.picker;

/**
 * 非门逻辑混合挑选器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/11 11:35
 */
public class NotPicker implements Picker {
    private final Picker delegate;

    public NotPicker(Picker delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean pick(PickContext pickContext) {
        return !delegate.pick(pickContext);
    }
}
