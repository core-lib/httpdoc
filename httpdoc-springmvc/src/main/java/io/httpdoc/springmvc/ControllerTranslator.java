package io.httpdoc.springmvc;

import io.httpdoc.core.Controller;

import java.util.List;

/**
 * SpringMVC {@link Controller} 翻译器
 *
 * @author 钟宝林
 * @date 2018-05-11 16:54
 **/
public interface ControllerTranslator {

    List<Controller> translator(TranslateContext translateContext);

}
