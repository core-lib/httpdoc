package io.httpdoc.springmvc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 路径处理类
 *
 * @author 钟宝林
 * @date 2018-05-16 14:08
 **/
public class PathProcessor {

    private static final Pattern GLOB_PATTERN = Pattern.compile("\\?|\\*|\\{((?:\\{[^/]+? }|[^/{}]|\\\\[{}])+?)}");

    private static final String DEFAULT_VARIABLE_PATTERN = "(.*)";

    private final Pattern pattern;

    private final String path;

    private final List<String> variableNames = new LinkedList<>();

    private final Map<String, String> replaceMapper = new HashMap<>();

    PathProcessor(String pattern) {
        this(pattern, true);
    }

    PathProcessor(String pattern, boolean caseSensitive) {
        this.path = pattern;
        StringBuilder patternBuilder = new StringBuilder();
        Matcher matcher = GLOB_PATTERN.matcher(pattern);
        int end = 0;
        while (matcher.find()) {
            patternBuilder.append(quote(pattern, end, matcher.start()));
            String match = matcher.group();
            if ("?".equals(match)) {
                patternBuilder.append('.');
            } else if ("*".equals(match)) {
                patternBuilder.append(".*");
            } else if (match.startsWith("{") && match.endsWith("}")) {
                int colonIdx = match.indexOf(':');
                if (colonIdx == -1) {
                    patternBuilder.append(DEFAULT_VARIABLE_PATTERN);
                    String variableName = matcher.group(1);
                    this.variableNames.add(variableName);
                    replaceMapper.put(match, variableName);
                } else {
                    String variablePattern = match.substring(colonIdx + 1, match.length() - 1);
                    patternBuilder.append('(');
                    patternBuilder.append(variablePattern);
                    patternBuilder.append(')');
                    String variableName = match.substring(1, colonIdx);
                    this.variableNames.add(variableName);
                    replaceMapper.put(match, variableName);
                }
            }
            end = matcher.end();
        }
        patternBuilder.append(quote(pattern, end, pattern.length()));
        this.pattern = (caseSensitive ? Pattern.compile(patternBuilder.toString()) : Pattern.compile(patternBuilder.toString(), Pattern.CASE_INSENSITIVE));
    }

    public Map<String, String> getReplaceMapper() {
        return replaceMapper;
    }

    public List<String> getVariableNames() {
        return variableNames;
    }

    private String quote(String s, int start, int end) {
        if (start == end) {
            return "";
        }
        return Pattern.quote(s.substring(start, end));
    }

    /**
     * Main entry point.
     *
     * @return {@code true} if the string matches against the pattern, or {@code false} otherwise.
     */
    public boolean matchStrings(String str, Map<String, String> uriTemplateVariables) {
        Matcher matcher = this.pattern.matcher(str);
        if (matcher.matches()) {
            if (uriTemplateVariables != null) {
                // SPR-8455
                if (this.variableNames.size() != matcher.groupCount()) {
                    throw new IllegalArgumentException("The number of capturing groups in the pattern segment " +
                            this.pattern + " does not match the number of URI template variables it defines, " +
                            "which can occur if capturing groups are used in a URI template regex. " +
                            "Use non-capturing groups instead.");
                }
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String name = this.variableNames.get(i - 1);
                    String value = matcher.group(i);
                    uriTemplateVariables.put(name, value);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public String process() {
        String returnPath = path;
        for (Map.Entry<String, String> entry : replaceMapper.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();

            returnPath = returnPath.replace(key, "{" + value + "}");
        }
        return returnPath;
    }

}
