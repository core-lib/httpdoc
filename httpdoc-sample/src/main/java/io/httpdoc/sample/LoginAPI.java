package io.httpdoc.sample;

import io.httpdoc.sample.user.Gender;
import io.httpdoc.sample.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Random;

/**
 * <p><b>用户登陆控制器</b></p>
 * <p>该示例用于展示<b>HttpDoc</b>框架可以与<b>SpringMVC</b>框架进行无缝集成。</p>
 * <p>并且无需为配合框架多写一行代码，甚至还想偷懒的话，连这些注释都是可以不写的。</p>
 *
 * @order 0
 */
@Controller
@RequestMapping("/login")
public class LoginAPI {

    /**
     * <p><b>用户登录方法</b></p>
     * <p>该示例用于展示通过Java最常用也是代码规范最必不可少的注释就能丰富<b>HttpDoc</b>的界面，</p>
     * <p>并且方法上的参数和返回值都有了各自的含义说明及是否必填，测试时更加通俗易懂。到此，我们都没有多写一行代码或注释。</p>
     *
     * @param username 用户名 <b>*</b>
     * @param password 密码 <b>*</b>
     * @return 用户登录结果
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public LoginResult login(String username, String password) {
        LoginResult result = new LoginResult();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            result.setCode(400);
            result.setMessage("用户名或密码错误");
            return result;
        }

        User user = new User();
        user.setId(Math.abs(new Random().nextLong()));
        user.setUsername(username);
        user.setPassword(password);
        user.setBirthday(new Date());
        user.setAge(18);
        user.setGender(Gender.MALE);

        result.setUser(user);

        return result;
    }

}
