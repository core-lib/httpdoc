package io.httpdoc.core.fragment;

import io.httpdoc.core.Importable;
import io.httpdoc.core.Preference;
import io.httpdoc.core.annotation.HDAnnotation;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.type.HDClass;
import io.httpdoc.core.type.HDType;
import io.httpdoc.core.type.HDTypeVariable;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 方法碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:31
 **/
public class MethodFragment extends ModifiedFragment implements Fragment {
    protected CommentFragment commentFragment;
    protected List<HDAnnotation> annotations = new ArrayList<>();
    protected List<HDTypeVariable> typeVariables = new ArrayList<>();
    protected HDType type;
    protected String name;
    protected List<ParameterFragment> parameterFragments = new ArrayList<>();
    protected List<HDClass> exceptions = new ArrayList<>();
    protected BlockFragment blockFragment;

    public MethodFragment() {
        this(Modifier.PUBLIC);
    }

    public MethodFragment(int modifier) {
        super(modifier);
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (commentFragment != null) commentFragment.joinTo(appender, preference);
        appender.enter();

        for (int i = 0; annotations != null && i < annotations.size(); i++) {
            annotations.get(i).joinTo(appender, preference);
            appender.enter();
        }
        super.joinTo(appender, preference);

        for (int i = 0; typeVariables != null && i < typeVariables.size(); i++) {
            if (i == 0) appender.append("<");
            else appender.append(", ");
            appender.append(typeVariables.get(i));
            if (i == typeVariables.size() - 1) appender.append("> ");
        }

        if (type != null) appender.append(type).append(" ");
        else appender.append("void ");
        if (name != null) appender.append(name);
        appender.append("(");
        for (int i = 0; parameterFragments != null && i < parameterFragments.size(); i++) {
            if (i > 0) appender.append(", ");
            ParameterFragment fragment = parameterFragments.get(i);
            fragment.joinTo(appender, preference);
        }
        appender.append(")");
        for (int i = 0; exceptions != null && i < exceptions.size(); i++) {
            if (i == 0) appender.append("throws ");
            else appender.append(", ");
            appender.append(exceptions.get(i));
        }
        if (blockFragment != null) blockFragment.joinTo(appender, preference);
        else appender.append(";");
    }

    @Override
    public List<String> imports() {
        List<String> imports = new ArrayList<>();
        if (commentFragment != null) imports.addAll(commentFragment.imports());
        for (Importable importable : annotations) imports.addAll(importable.imports());
        for (Importable importable : typeVariables) imports.addAll(importable.imports());
        if (type != null) imports.addAll(type.imports());
        for (Importable importable : parameterFragments) imports.addAll(importable.imports());
        for (Importable importable : exceptions) imports.addAll(importable.imports());
        if (blockFragment != null) imports.addAll(blockFragment.imports());
        return imports;
    }

    public CommentFragment getCommentFragment() {
        return commentFragment;
    }

    public void setCommentFragment(CommentFragment commentFragment) {
        this.commentFragment = commentFragment;
    }

    public List<HDAnnotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<HDAnnotation> annotations) {
        this.annotations = annotations;
    }

    public List<HDTypeVariable> getTypeVariables() {
        return typeVariables;
    }

    public void setTypeVariables(List<HDTypeVariable> typeVariables) {
        this.typeVariables = typeVariables;
    }

    public HDType getType() {
        return type;
    }

    public void setType(HDType type) {
        this.type = type;
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

    public List<HDClass> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<HDClass> exceptions) {
        this.exceptions = exceptions;
    }

    public BlockFragment getBlockFragment() {
        return blockFragment;
    }

    public void setBlockFragment(BlockFragment blockFragment) {
        this.blockFragment = blockFragment;
    }
}
