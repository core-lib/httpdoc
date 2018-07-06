package io.httpdoc.spring.mvc;

import io.httpdoc.core.Document;
import io.httpdoc.core.conversion.StandardConverter;
import io.httpdoc.core.exception.DocumentTranslationException;
import io.httpdoc.core.translation.Translation;
import io.httpdoc.core.translation.Translator;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 钟宝林
 * @date 2018-05-09 18:00
 **/
@RequestMapping("/spring-mvc")
@Controller
public class SpringMVCHttpdocController {
    private static Translator translator = new SpringMVCTranslator();

    @RequestMapping(value = "httpdoc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> document(HttpServletRequest request) throws DocumentTranslationException {
        Translation translation = new Translation();
        ServletContext servletContext = request.getServletContext();
        ServletContextHolder servletContextHolder = new ServletContextHolder();
        servletContextHolder.setServletContext(servletContext);
        translation.setContainer(servletContextHolder);
        Document document = translator.translate(translation);
        return new StandardConverter().convert(document);
    }

}
