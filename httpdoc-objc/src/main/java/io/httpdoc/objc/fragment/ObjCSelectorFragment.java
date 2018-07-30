package io.httpdoc.objc.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.appender.TitleCasedAppender;
import io.httpdoc.core.fragment.Fragment;
import io.httpdoc.core.kit.StringKit;
import io.httpdoc.objc.ObjC;
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
public class ObjCSelectorFragment implements Fragment {
    private boolean instantial = true;
    private ObjCResultFragment resultFragment;
    private String name;
    private Set<ObjCParameterFragment> parameterFragments = new LinkedHashSet<>();
    private String comment;
    private ObjCBlockFragment blockFragment;

    public ObjCSelectorFragment() {
    }

    public ObjCSelectorFragment(ObjCResultFragment resultFragment, String name) {
        this.resultFragment = resultFragment;
        this.name = name;
    }

    public ObjCSelectorFragment(ObjCResultFragment resultFragment, String name, String comment) {
        this.resultFragment = resultFragment;
        this.name = name;
        this.comment = comment;
    }

    public ObjCSelectorFragment(boolean instantial, ObjCResultFragment resultFragment, String name) {
        this.instantial = instantial;
        this.resultFragment = resultFragment;
        this.name = name;
    }

    public ObjCSelectorFragment(boolean instantial, ObjCResultFragment resultFragment, String name, String comment) {
        this.instantial = instantial;
        this.resultFragment = resultFragment;
        this.name = name;
        this.comment = comment;
    }

    public ObjCSelectorFragment declaration() {
        ObjCSelectorFragment declaration = new ObjCSelectorFragment();
        declaration.setInstantial(instantial);
        declaration.setResultFragment(resultFragment);
        declaration.setName(name);
        declaration.setParameterFragments(parameterFragments);
        declaration.setComment(comment);
        return declaration;
    }

    @Override
    public Set<String> imports() {
        Set<String> imports = new TreeSet<>();
        if (resultFragment != null) imports.addAll(resultFragment.imports());
        for (ObjCParameterFragment parameterFragment : parameterFragments) imports.addAll(parameterFragment.imports());
        if (blockFragment != null) imports.addAll(blockFragment.imports());
        return imports;
    }

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        String comment = comment();
        ObjCCommentFragment commentFragment = new ObjCCommentFragment(comment);
        commentFragment.joinTo(appender, preference);
        if (instantial) appender.append("- ");
        else appender.append("+ ");

        if (resultFragment != null) resultFragment.joinTo(appender, preference);
        else appender.append("(void)");

        appender.append(name);
        TitleCasedAppender tca = new TitleCasedAppender(appender);
        int index = 0;
        for (ObjCParameterFragment parameterFragment : parameterFragments) {
            if (index++ == 0) appender.append("With");
            else appender.enter().append("    ");
            parameterFragment.joinTo(tca, preference);
        }
        tca.close();

        if (blockFragment != null) blockFragment.joinTo(appender, preference);
        else appender.append(";");
    }

    private String comment() {
        StringBuilder builder = new StringBuilder(comment != null ? comment : "").append('\n');
        for (ObjCParameterFragment parameterFragment : parameterFragments) {
            String name = parameterFragment.getVariable();
            String comment = parameterFragment.getComment();
            if (!StringKit.isBlank(comment)) builder.append('\n').append("@param ").append(name).append(" ").append(comment);
        }
        if (resultFragment != null) {
            String comment = resultFragment.getComment();
            if (!StringKit.isBlank(comment)) builder.append('\n').append("@return ").append(comment);
        }
        return builder.toString();
    }

    public ObjCSelectorFragment addParameterFragment(String name, ObjCType type, String variable) {
        return addParameterFragment(new ObjCParameterFragment(name, type, variable));
    }

    public ObjCSelectorFragment addParameterFragment(String name, ObjCType type, String variable, String comment) {
        return addParameterFragment(new ObjCParameterFragment(name, type, variable, comment));
    }

    public ObjCSelectorFragment addParameterFragment(ObjCParameterFragment parameterFragment) {
        parameterFragments.add(parameterFragment);
        return this;
    }

    public ObjCSelectorFragment addSentence(CharSequence sentence) {
        if (blockFragment == null) blockFragment = new ObjCBlockFragment();
        blockFragment.addSentence(sentence);
        return this;
    }

    public ObjCSelectorFragment addSentence(CharSequence sentence, String... imports) {
        if (blockFragment == null) blockFragment = new ObjCBlockFragment();
        blockFragment.addSentence(sentence, imports);
        return this;
    }

    public ObjCSelectorFragment addSentence(CharSequence sentence, Class<? extends ObjC>... objCClasses) {
        if (blockFragment == null) blockFragment = new ObjCBlockFragment();
        blockFragment.addSentence(sentence, objCClasses);
        return this;
    }

    public ObjCSelectorFragment addSentence(CharSequence sentence, ObjCType... types) {
        if (blockFragment == null) blockFragment = new ObjCBlockFragment();
        blockFragment.addSentence(sentence, types);
        return this;
    }

    public ObjCSelectorFragment addImports(String... imports) {
        if (blockFragment == null) blockFragment = new ObjCBlockFragment();
        blockFragment.addImports(imports);
        return this;
    }

    public boolean isInstantial() {
        return instantial;
    }

    public ObjCSelectorFragment setInstantial(boolean instantial) {
        this.instantial = instantial;
        return this;
    }

    public ObjCResultFragment getResultFragment() {
        return resultFragment;
    }

    public ObjCSelectorFragment setResultFragment(ObjCResultFragment resultFragment) {
        this.resultFragment = resultFragment;
        return this;
    }

    public String getName() {
        return name;
    }

    public ObjCSelectorFragment setName(String name) {
        this.name = name;
        return this;
    }

    public Set<ObjCParameterFragment> getParameterFragments() {
        return parameterFragments;
    }

    public ObjCSelectorFragment setParameterFragments(Set<ObjCParameterFragment> parameterFragments) {
        this.parameterFragments = parameterFragments;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public ObjCSelectorFragment setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public ObjCBlockFragment getBlockFragment() {
        return blockFragment;
    }

    public ObjCSelectorFragment setBlockFragment(ObjCBlockFragment blockFragment) {
        this.blockFragment = blockFragment;
        return this;
    }

}
