package io.httpdoc.sample.user;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

/**
 * 用户管理器,用于管理用户相关的操作，提供针对用户数据的基础增删改查操作。
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/11/5
 * @summary 用户管理API
 * @tag 用户
 * @deprecated 不推荐再使用了
 */
@Controller
@RequestMapping("/api/users")
public class UserAPI {

    /**
     * 创建用户，该API接收用户对象数据，其中用户名称为必填。
     *
     * @param user 用户数据
     * @return 用户创建结果
     * @summary 创建用户
     * @name 创建
     */
    @ResponseBody
    @RequestMapping(
            value = "",
            method = RequestMethod.POST,
            consumes = {"application/json", "application/xml"},
            produces = {"application/json", "application/xml"}
    )
    public UserCreateResult create(@RequestBody User user) {
        UserCreateResult result = new UserCreateResult();
        if (StringUtils.isEmpty(user.getName())) {
            result.setCode(400);
            result.setMessage("请输入用户名称");
        }

        // 保存用户...
        result.setUserId(Math.abs(new Random().nextLong()));

        return result;
    }

}
