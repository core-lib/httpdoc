package io.httpdoc.core.fragment;

import io.httpdoc.core.appender.Appender;

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
    private String type;
    private String name;
    private List<ParameterFragment> parameterFragments = new ArrayList<>();
    private List<ExceptionFragment> exceptionFragments = new ArrayList<>();
    private BlockFragment blockFragment;

    @Override
    public <T extends Appender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (commentFragment != null) commentFragment.joinTo(appender, preference);
        appender.enter();
        super.joinTo(appender, preference);
        if (type != null) appender.append(type).append(" ");
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
            if (i > 0) appender.append(", ");
            ExceptionFragment fragment = exceptionFragments.get(i);
            fragment.joinTo(appender, preference);
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
}
