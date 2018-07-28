package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.core.kit.StringKit;
import io.httpdoc.objc.type.ObjCType;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * 函数代码碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-07-24 17:41
 **/
public class SelectorFragment implements Fragment {
    protected boolean instantial = true;
    protected ResultFragment resultFragment;
    protected String name;
    protected Set<ParameterFragment> parameterFragments = new LinkedHashSet<>();
    protected String comment;
    protected BlockFragment blockFragment;

    public SelectorFragment() {
    }

    public SelectorFragment(ResultFragment resultFragment, String name) {
        this.resultFragment = resultFragment;
        this.name = name;
    }

    public SelectorFragment(ResultFragment resultFragment, String name, String comment) {
        this.resultFragment = resultFragment;
        this.name = name;
        this.comment = comment;
    }

    public SelectorFragment(boolean instantial, ResultFragment resultFragment, String name) {
        this.instantial = instantial;
        this.resultFragment = resultFragment;
        this.name = name;
    }

    public SelectorFragment(boolean instantial, ResultFragment resultFragment, String name, String comment) {
        this.instantial = instantial;
        this.resultFragment = resultFragment;
        this.name = name;
        this.comment = comment;
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new TreeSet<>();
        if (resultFragment != null) imports.addAll(resultFragment.imports());
        for (ParameterFragment parameterFragment : parameterFragments) imports.addAll(parameterFragment.imports());
        if (blockFragment != null) imports.addAll(blockFragment.imports());
        return imports;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        String comment = comment();
        CommentFragment commentFragment = new CommentFragment(comment);
        commentFragment.joinTo(appender, preference);
        if (instantial) appender.append("- ");
        else appender.append("+ ");

        if (resultFragment != null) resultFragment.joinTo(appender, preference);
        else appender.append("(void)");

        appender.append(name);
        int index = 0;
        for (ParameterFragment parameterFragment : parameterFragments) {
            if (index++ == 0) appender.append("_");
            else appender.enter().append("    ");
            parameterFragment.joinTo(appender, preference);
        }

        if (blockFragment != null) blockFragment.joinTo(appender, preference);
        else appender.append(";");
    }

    protected String comment() {
        StringBuilder builder = new StringBuilder(comment != null ? comment : "").append('\n');
        for (ParameterFragment parameterFragment : parameterFragments) {
            String name = parameterFragment.getVariable();
            String comment = parameterFragment.getComment();
            if (!StringKit.isBlank(comment)) builder.append('\n').append("@param ").append(name).append(" ").append(comment);
        }
        if (resultFragment != null) {
            String comment = resultFragment.getComment();
            if (!StringKit.isBlank(comment)) builder.append('\n').append("@return ").append(" ").append(comment);
        }
        return builder.toString();
    }

    public SelectorFragment addParameterFragment(String name, ObjCType type, String variable) {
        return addParameterFragment(new ParameterFragment(name, type, variable));
    }

    public SelectorFragment addParameterFragment(String name, ObjCType type, String variable, String comment) {
        return addParameterFragment(new ParameterFragment(name, type, variable, comment));
    }

    public SelectorFragment addParameterFragment(ParameterFragment parameterFragment) {
        parameterFragments.add(parameterFragment);
        return this;
    }

    public SelectorFragment addSentence(CharSequence sentence, String... imports) {
        if (blockFragment == null) blockFragment = new BlockFragment();
        blockFragment.addSentence(sentence, imports);
        return this;
    }

    public SelectorFragment addImports(String... imports) {
        if (blockFragment == null) blockFragment = new BlockFragment();
        blockFragment.addImports(imports);
        return this;
    }

    public boolean isInstantial() {
        return instantial;
    }

    public SelectorFragment setInstantial(boolean instantial) {
        this.instantial = instantial;
        return this;
    }

    public ResultFragment getResultFragment() {
        return resultFragment;
    }

    public SelectorFragment setResultFragment(ResultFragment resultFragment) {
        this.resultFragment = resultFragment;
        return this;
    }

    public String getName() {
        return name;
    }

    public SelectorFragment setName(String name) {
        this.name = name;
        return this;
    }

    public Set<ParameterFragment> getParameterFragments() {
        return parameterFragments;
    }

    public SelectorFragment setParameterFragments(Set<ParameterFragment> parameterFragments) {
        this.parameterFragments = parameterFragments;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public SelectorFragment setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public BlockFragment getBlockFragment() {
        return blockFragment;
    }

    public SelectorFragment setBlockFragment(BlockFragment blockFragment) {
        this.blockFragment = blockFragment;
        return this;
    }

}
