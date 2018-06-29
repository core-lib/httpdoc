package io.httpdoc.core.fragment.annotation;

public class HDAnnotationKey implements CharSequence {
    private final String name;
    private final boolean original;

    public HDAnnotationKey(String name) {
        this(name, false);
    }

    public HDAnnotationKey(String name, boolean original) {
        if (name == null) throw new NullPointerException();
        this.name = name;
        this.original = original;
    }

    @Override
    public int length() {
        return name.length();
    }

    @Override
    public char charAt(int index) {
        return name.charAt(index);
    }

    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {
        return name.subSequence(beginIndex, endIndex);
    }

    public String getName() {
        return name;
    }

    public boolean isOriginal() {
        return original;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HDAnnotationKey that = (HDAnnotationKey) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
