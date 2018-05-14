package io.httpdoc.springmvc;

import io.httpdoc.core.Controller;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SpringMVC Controller 信息持有
 *
 * @author 钟宝林
 * @date 2018-04-17 14:08
 **/
public class ControllerInfoHolder {
    /**
     * 全局 Controller 信息
     */
    public static final List<ControllerInfoHolder> controllerInfoHolders = new ArrayList<>();

    private RequestMappingInfo requestMappingInfo;
    private HandlerMethod handlerMethod;
    /**
     * 标记是否已经被处理过
     */
    private boolean isHandled;
    /**
     * 处理后的结果
     */
    private Controller controller;

    public RequestMappingInfo getRequestMappingInfo() {
        return requestMappingInfo;
    }

    public void setRequestMappingInfo(RequestMappingInfo requestMappingInfo) {
        this.requestMappingInfo = requestMappingInfo;
    }

    public HandlerMethod getHandlerMethod() {
        return handlerMethod;
    }

    public void setHandlerMethod(HandlerMethod handlerMethod) {
        this.handlerMethod = handlerMethod;
    }

    public boolean isHandled() {
        return isHandled;
    }

    public void setHandled(boolean handled) {
        isHandled = handled;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * 封装从 SpringMVC 取到的接口信息到{@link #controllerInfoHolders}
     *
     * @param map 接口信息
     */
    public static void buildControllerInfo(Map<RequestMappingInfo, HandlerMethod> map) {
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            RequestMappingInfo requestMappingInfo = (RequestMappingInfo) entry.getKey();
            HandlerMethod handlerMethod = (HandlerMethod) entry.getValue();

            ControllerInfoHolder controllerInfoHolder = new ControllerInfoHolder();
            controllerInfoHolder.setHandlerMethod(handlerMethod);
            controllerInfoHolder.setRequestMappingInfo(requestMappingInfo);
            if (controllerInfoHolders.contains(controllerInfoHolder)) {
                continue;
            }
            controllerInfoHolders.add(controllerInfoHolder);
        }
    }

    @Override
    public String toString() {
        return "ControllerInfoHolder{" +
                "requestMappingInfo=" + requestMappingInfo +
                ", handlerMethod=" + handlerMethod +
                ", isHandled=" + isHandled +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ControllerInfoHolder that = (ControllerInfoHolder) o;

        return requestMappingInfo != null ? requestMappingInfo.equals(that.requestMappingInfo) : that.requestMappingInfo == null;
    }

    @Override
    public int hashCode() {
        return requestMappingInfo != null ? requestMappingInfo.hashCode() : 0;
    }
}
