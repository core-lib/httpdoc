package io.httpdoc.core.fragment;

import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.type.JavaType;

import java.io.IOException;

/**
 * 字段碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:32
 **/
public class FieldFragment extends ModifiedFragment implements Fragment {
    private CommentFragment commentFragment;
    private JavaType type;
    private String name;

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (commentFragment != null) commentFragment.joinTo(appender, preference);
        super.joinTo(appender, preference);
        appender.append(type).append(" ").append(name).append(";").enter();
    }

    public CommentFragment getCommentFragment() {
        return commentFragment;
    }

    public void setCommentFragment(CommentFragment commentFragment) {
        this.commentFragment = commentFragment;
    }

    public JavaType getType() {
        return type;
    }

    public void setType(JavaType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
