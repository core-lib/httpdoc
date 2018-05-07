package io.httpdoc.core;

public class DefaultPreference implements Preference {

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
}
