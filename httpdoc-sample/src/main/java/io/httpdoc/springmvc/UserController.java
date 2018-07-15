package io.httpdoc.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-01 14:16
 **/
@Controller
@RequestMapping("/users")
public class UserController {

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseBody
    public String test(
            @PathVariable String id,
            User user,
            String dateCreated,
            MultipartFile file
    ) {

        return "OK";
    }

}
