package io.httpdoc.core.fragment;

import io.httpdoc.core.Preference;
import io.httpdoc.core.annotation.HDAnnotation;
import io.httpdoc.core.appender.LineAppender;
import io.httpdoc.core.type.HDType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 参数碎片
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-04-28 17:44
 **/
public class ParameterFragment extends ModifiedFragment {
    private List<HDAnnotation> annotations = new ArrayList<>();
    private HDType type;
    private String name;

    @Override
    public <T extends LineAppender<T>> void joinTo(T appender, Preference preference) throws IOException {
        for (int i = 0; annotations != null && i < annotations.size(); i++) {
            annotations.get(i).joinTo(appender, preference);
            appender.enter();
        }
        super.joinTo(appender, preference);
        appender.append(type);
        appender.append(" ");
        appender.append(name);
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
}
