package io.httpdoc.core.fragment;

import io.httpdoc.core.Importable;
import io.httpdoc.core.Preference;
import io.httpdoc.core.annotation.HDAnnotation;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.type.HDType;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 字段碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-27 16:32
 **/
public class FieldFragment extends ModifiedFragment implements Fragment {
    private CommentFragment commentFragment;
    private List<HDAnnotation> annotations = new ArrayList<>();
    private HDType type;
    private String name;
    private AssignmentFragment assignmentFragment;

    public FieldFragment() {
        this(Modifier.PRIVATE);
    }

    public FieldFragment(int modifier) {
        super(modifier);
    }

    public FieldFragment(HDType type, String name) {
        super(Modifier.PRIVATE);
        this.type = type;
        this.name = name;
    }

    public FieldFragment(HDType type, String name, CharSequence assignment) {
        super(Modifier.PRIVATE);
        this.type = type;
        this.name = name;
        this.assignmentFragment = new AssignmentFragment(assignment);
    }

    public FieldFragment(int modifier, HDType type, String name) {
        super(modifier);
        this.type = type;
        this.name = name;
    }

    public FieldFragment(int modifier, HDType type, String name, CharSequence assignment) {
        super(modifier);
        this.type = type;
        this.name = name;
        this.assignmentFragment = new AssignmentFragment(assignment);
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        if (commentFragment != null) commentFragment.joinTo(appender, preference);
        for (int i = 0; annotations != null && i < annotations.size(); i++) {
            annotations.get(i).joinTo(appender, preference);
            appender.enter();
        }
        super.joinTo(appender, preference);
        appender.append(type).append(" ").append(name);
        if (assignmentFragment != null) assignmentFragment.joinTo(appender, preference);
        appender.append(";");
    }

    @Override
    public List<String> imports() {
        List<String> imports = new ArrayList<>();
        if (commentFragment != null) imports.addAll(commentFragment.imports());
        for (Importable importable : annotations) imports.addAll(importable.imports());
        if (type != null) imports.addAll(type.imports());
        if (assignmentFragment != null) imports.addAll(assignmentFragment.imports());
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

    public AssignmentFragment getAssignmentFragment() {
        return assignmentFragment;
    }

    public void setAssignmentFragment(AssignmentFragment assignmentFragment) {
        this.assignmentFragment = assignmentFragment;
    }
}
