package io.httpdoc.core.fragment;

import io.httpdoc.core.Importable;
import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.annotation.HDAnnotation;
import io.httpdoc.core.kit.StringKit;
import io.httpdoc.core.type.HDClass;
import io.httpdoc.core.type.HDTypeVariable;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 方法碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:31
 **/
public class MethodFragment extends ModifiedFragment implements Fragment {
    protected List<HDAnnotation> annotations = new ArrayList<>();
    protected List<TypeVariableFragment> typeVariableFragments = new ArrayList<>();
    protected ResultFragment resultFragment;
    protected String name;
    protected List<ParameterFragment> parameterFragments = new ArrayList<>();
    protected List<ExceptionFragment> exceptionFragments = new ArrayList<>();
    protected BlockFragment blockFragment;
    protected String comment;

    public MethodFragment() {
        this(Modifier.PUBLIC);
    }

    public MethodFragment(int modifier) {
        super(modifier);
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        String comment = comment();
        CommentFragment commentFragment = new CommentFragment(comment);
        commentFragment.joinTo(appender, preference);

        for (int i = 0; annotations != null && i < annotations.size(); i++) {
            annotations.get(i).joinTo(appender, preference);
            appender.enter();
        }
        super.joinTo(appender, preference);

        for (int i = 0; typeVariableFragments != null && i < typeVariableFragments.size(); i++) {
            if (i == 0) appender.append("<");
            else appender.append(", ");
            TypeVariableFragment fragment = typeVariableFragments.get(i);
            fragment.joinTo(appender, preference);
            if (i == typeVariableFragments.size() - 1) appender.append("> ");
        }

        if (resultFragment != null) resultFragment.joinTo(appender, preference);
        if (name != null) appender.append(name);
        appender.append("(");
        for (int i = 0; parameterFragments != null && i < parameterFragments.size(); i++) {
            if (i > 0) appender.append(", ");
            ParameterFragment fragment = parameterFragments.get(i);
            fragment.joinTo(appender, preference);
        }
        appender.append(")");
        for (int i = 0; exceptionFragments != null && i < exceptionFragments.size(); i++) {
            if (i == 0) appender.append("throws ");
            else appender.append(", ");
            ExceptionFragment fragment = exceptionFragments.get(i);
            fragment.joinTo(appender, preference);
        }
        if (blockFragment != null) blockFragment.joinTo(appender, preference);
        else appender.append(";").enter();
    }

    protected String comment() {
        StringBuilder builder = new StringBuilder(comment != null ? comment : "").append('\n');
        for (int i = 0; parameterFragments != null && i < parameterFragments.size(); i++) {
            ParameterFragment fragment = parameterFragments.get(i);
            String name = fragment.getName();
            String comment = fragment.getComment();
            if (!StringKit.isBlank(comment)) builder.append('\n').append("@param ").append(name).append(" ").append(comment);
        }
        for (int i = 0; typeVariableFragments != null && i < typeVariableFragments.size(); i++) {
            TypeVariableFragment fragment = typeVariableFragments.get(i);
            HDTypeVariable typeVariable = fragment.getTypeVariable();
            String name = typeVariable.getName();
            String comment = fragment.getComment();
            if (!StringKit.isBlank(comment)) builder.append('\n').append("@param <").append(name).append("> ").append(comment);
        }
        if (resultFragment != null) {
            String comment = resultFragment.getComment();
            if (!StringKit.isBlank(comment)) builder.append('\n').append("@return ").append(" ").append(comment);
        }
        for (int i = 0; exceptionFragments != null && i < exceptionFragments.size(); i++) {
            ExceptionFragment fragment = exceptionFragments.get(i);
            HDClass type = fragment.getType();
            CharSequence name = type.getAbbrName();
            String comment = fragment.getComment();
            if (!StringKit.isBlank(comment)) builder.append('\n').append("@throws ").append(name).append(" ").append(comment);
        }
        return builder.toString();
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new LinkedHashSet<>();
        for (Importable importable : annotations) imports.addAll(importable.imports());
        for (Importable importable : typeVariableFragments) imports.addAll(importable.imports());
        if (resultFragment != null) imports.addAll(resultFragment.imports());
        for (Importable importable : parameterFragments) imports.addAll(importable.imports());
        for (Importable importable : exceptionFragments) imports.addAll(importable.imports());
        if (blockFragment != null) imports.addAll(blockFragment.imports());
        return imports;
    }

    public List<HDAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<HDAnnotation> annotations) {
        this.annotations = annotations;
    }

    public List<TypeVariableFragment> getTypeVariableFragments() {
        return typeVariableFragments;
    }

    public void setTypeVariableFragments(List<TypeVariableFragment> typeVariableFragments) {
        this.typeVariableFragments = typeVariableFragments;
    }

    public ResultFragment getResultFragment() {
        return resultFragment;
    }

    public void setResultFragment(ResultFragment resultFragment) {
        this.resultFragment = resultFragment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ParameterFragment> getParameterFragments() {
        return parameterFragments;
    }

    public void setParameterFragments(List<ParameterFragment> parameterFragments) {
        this.parameterFragments = parameterFragments;
    }

    public List<ExceptionFragment> getExceptionFragments() {
        return exceptionFragments;
    }

    public void setExceptionFragments(List<ExceptionFragment> exceptionFragments) {
        this.exceptionFragments = exceptionFragments;
    }

    public BlockFragment getBlockFragment() {
        return blockFragment;
    }

    public void setBlockFragment(BlockFragment blockFragment) {
        this.blockFragment = blockFragment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
