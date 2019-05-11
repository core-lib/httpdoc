package io.httpdoc.core.picker;

/**
 * 挑选器
 *
 * @author Payne 646742615@qq.com
 * 2019/5/11 11:14
 */
public interface Picker {

    /**
     * 全挑选
     */
    Picker ALL = new Picker() {
        @Override
        public boolean pick(PickContext pickContext) {
            return true;
        }
    };

    /**
     * 挑选
     *
     * @param pickContext 挑选上下文
     * @return 是否挑出
     */
    boolean pick(PickContext pickContext);

}
