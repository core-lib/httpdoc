package io.httpdoc.core.kit;

public class StringKit {

    public static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }

    public static String join(char c, String... values) {
        StringBuilder builder = new StringBuilder();
        for (String value : values) {
            if (builder.length() > 0) builder.append(c);
            builder.append(value);
        }
        return builder.toString();
    }

}