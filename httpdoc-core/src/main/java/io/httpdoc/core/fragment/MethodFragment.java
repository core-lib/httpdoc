package io.httpdoc.core.fragment;

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
