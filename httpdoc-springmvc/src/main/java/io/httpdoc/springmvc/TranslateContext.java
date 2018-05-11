package io.httpdoc.springmvc;

import io.httpdoc.core.Controller;

import java.util.List;

/**
 * {@link Controller}翻译上下文
 *
 * @author 钟宝林
 * @date 2018-05-11 16:56
 **/
public class TranslateContext {

    private List<ControllerInfoHolder> controllerInfoHolders;

    public List<ControllerInfoHolder> getControllerInfoHolders() {
        return controllerInfoHolders;
    }

    public void setControllerInfoHolders(List<ControllerInfoHolder> controllerInfoHolders) {
        this.controllerInfoHolders = controllerInfoHolders;
    }
}
