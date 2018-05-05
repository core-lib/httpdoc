package io.httpdoc.core.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.annotation.HDAnnotation;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.type.HDClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 方法碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:31
 **/
public class MethodFragment extends ModifiedFragment implements Fragment {
    private CommentFragment commentFragment;
    private List<HDAnnotation> annotations = new ArrayList<>();
    private List<TypeParameterFragment> typeParameterFragments = new ArrayList<>();
    private String type;
    private String name;
    private List<ParameterFragment> parameterFragments = new ArrayList<>();
    private List<HDClass> exceptions = new ArrayList<>();
    private BlockFragment blockFragment;

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (commentFragment != null) commentFragment.joinTo(appender, preference);

        for (int i = 0; annotations != null && i < annotations.size(); i++) {
            annotations.get(i).joinTo(appender, preference);
            appender.enter();
        }

        appender.enter();
        super.joinTo(appender, preference);

        for (int i = 0; typeParameterFragments != null && i < typeParameterFragments.size(); i++) {
            if (i == 0) appender.append("<");
            else appender.append(", ");
            typeParameterFragments.get(i).joinTo(appender, preference);
            if (i == typeParameterFragments.size() - 1) appender.append("> ");
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

    public List<TypeParameterFragment> getTypeParameterFragments() {
        return typeParameterFragments;
    }

    public void setTypeParameterFragments(List<TypeParameterFragment> typeParameterFragments) {
        this.typeParameterFragments = typeParameterFragments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
