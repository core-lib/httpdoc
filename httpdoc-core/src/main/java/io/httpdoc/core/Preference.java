package io.httpdoc.core;

/**
 * 代码风格偏好
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-28 13:44
 **/
public interface Preference {

    Preference DEFAULT = new Preference() {
        @Override
        public int getIndent() {
            return 4;
        }

        @Override
        public boolean isAnnotationDefaultValueHidden() {
            return true;
        }

        @Override
        public boolean isAnnotationValueKeyHiddenIfUnnecessary() {
            return true;
        }
    };

    int getIndent();

    boolean isAnnotationDefaultValueHidden();

    boolean isAnnotationValueKeyHiddenIfUnnecessary();

}
