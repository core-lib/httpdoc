package io.httpdoc.springmvc;

import io.httpdoc.core.annotation.Alias;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
            @Alias("ID") @PathVariable String id,
            User user,
            String dateCreated,
            MultipartFile file
    ) {

        return "OK";
    }

    @RequestMapping(value = "/{路径}/{矩阵}", method = RequestMethod.GET)
    @ResponseBody
    public String matrix(
            @PathVariable("路径") String path,
            @PathVariable("矩阵") String matrix,
            @MatrixVariable(value = "name", pathVar = "路径") String[][] name,
            @RequestParam("查询") String query
    ) {

        return "@:OK";
    }

}
