package io.httpdoc.core;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-12 16:26
 **/
public class Girl extends Person {
    private Boy boyfriend;

    public Boy getBoyfriend() {
        return boyfriend;
    }

    public void setBoyfriend(Boy boyfriend) {
        this.boyfriend = boyfriend;
    }
}
