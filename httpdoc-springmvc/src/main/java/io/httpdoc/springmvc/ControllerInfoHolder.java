package io.httpdoc.springmvc;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

/**
 * SpringMVC Controller 信息持有
 *
 * @author 钟宝林
 * @date 2018-04-17 14:08
 **/
public class ControllerInfoHolder {

    private RequestMappingInfo requestMappingInfo;
    private HandlerMethod handlerMethod;
    /**
     * 标记是否已经被处理过
     */
    private boolean isHandled;

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

    @Override
    public String toString() {
        return "ControllerInfoHolder{" +
                "requestMappingInfo=" + requestMappingInfo +
                ", handlerMethod=" + handlerMethod +
                ", isHandled=" + isHandled +
                '}';
    }
}
