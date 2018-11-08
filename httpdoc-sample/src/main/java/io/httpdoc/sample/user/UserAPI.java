package io.httpdoc.sample.user;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * <p><b>用户管理器</b></p>
 * <p>该示例用于展示如何给<b>Controller</b>分组，分别有两种方式：</p>
 * <ul>
 * <li>1. 通过注释，在注释的下方添加一个或多个<b>@tag</b>标签，并在标签后写上分组名称即可，如果要分在多个组则可以多写几个。</li>
 * <li>2. 通过注解，在该<b>Controller</b>的类上标注一个<b>@Tag</b>注解，然后指定一个或多个分组名称即可。由此可知，<b>Controller</b>与分组是多对多的关系。</li>
 * </ul>
 * <p>两种方式效果是一样的，推荐使用第一种方式，这样<b>HttpDoc</b>框架的代码就不会入侵到你的项目里面。</p>
 * <p>后面还会介绍若干个这样的特性，框架都提供了注释和注解两种方案来满足需求，并且都推荐使用注释的方式。</p>
 * <p>在该示例中，我把<b>UserAPI</b>用注释的方式分别归到了 a.基础模块，b.用户模块，在界面上我们可以通过这两个分组都能找到该<b>Controller</b></p>
 *
 * @tag 基础模块
 * @tag 用户模块
 * @order 1
 */
@Controller
@RequestMapping("/users")
public class UserAPI {

    /**
     * <p><b>删除用户方法</b></p>
     * <p>该示例用于展示当方法中有些参数并不是通过前端主动传过来的，而是浏览器的行为或后台的逻辑而来的时候，我们可以通过下列几种方式忽略掉一些不需要展示的参数。</p>
     * <ul>
     * <li>1. 框架自动忽略参数类型：<b>Class</b>，<b>ServletContext</b>，<b>ServletRequest</b>，<b>ServletResponse</b>，<b>HttpServletRequest</b>，<b>HttpServletResponse</b>，<b>HttpSession</b>，<b>HttpHeaders</b>，<b>BindingResult</b>，<b>UriComponentsBuilder</b>，<b>Model</b>，<b>ModelMap</b>，<b>ExtendedModelMap</b></li>
     * <li>2. 通过注释标签<b>@ignore</b></li>
     * <li>3. 通过在需要忽略的参数上添加<b>@Ignore</b>注解</li>
     * </ul>
     * 例如在该示例上使用了第二种也就是注释标签的方式来忽略<b>sid</b>这个来自<b>cookie</b>的参数，所以在界面上并没有显示该参数。
     *
     * @param id  用户ID
     * @param sid sid
     * @return 用户删除结果
     * @ignore sid
     */
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {"application/json", "application/xml"})
    public UserDeleteResult delete(@PathVariable("id") Long id, @CookieValue(value = "sid", required = false) String sid) {
        return new UserDeleteResult();
    }

    /**
     * <p><b>创建用户方法</b></p>
     * <p>该示例用于展示不仅方法的请求参数可以通过注释来提升可读性，当方法的参数是一个复杂对象时，我们还可以对参数对象的字段进行注释。</p>
     * <p>用户对象是一个复杂对象，但是我们看到其中每个字段都是有相应的注释，在代码上我们可以将注释写在字段上也可以写在其<b>getter</b>方法上。</p>
     *
     * @param user 用户对象
     * @return 用户创建结果
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public UserCreateResult create(@RequestBody User user) {
        UserCreateResult result = new UserCreateResult();

        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            result.setCode(400);
            result.setMessage("参数错误，用户名和密码均不能为空");
            return result;
        }

        result.setUserId(Math.abs(new Random().nextLong()));

        return result;
    }

    /**
     * <p><b>用户更新方法</b></p>
     * <p>该示例用于展示如何在界面上给<b>API</b>方法写一个概要描述，出于查找的便利性考虑，界面上的方法都是折叠起来的。</p>
     * <p>当然方法的注释也被隐藏了，我们可以通过在注释上增加一个<b>@summary</b>标签来为方法做一个概要的描述。</p>
     * <p>在界面上可以看到，<b> PUT /users/{id} </b>方法比<b> POST /users </b>方法的右边多了一个概要描述。</p>
     * <p>同样的，我们还可以利用这个注释标签<b>（@summary）</b>给<b>Controller</b>以及<b>Model</b>写一个概要描述。</p>
     *
     * @param id   用户ID
     * @param user 新用户对象
     * @return 用户更新结果
     * @summary 根据用户ID更新用户数据
     */
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public UserUpdateResult update(@PathVariable("id") Long id, @RequestBody User user) {
        return new UserUpdateResult();
    }

    /**
     * <p><b>用户分页搜索方法</b></p>
     * <p>该示例用于展示当某个方法已经过时了，不再推荐使用的时候，可以通过下面两种方式来进行标记。</p>
     * <ul>
     * <li>1. 通过注释标签<b>@deprecated</b>，这方式可以写上过时的理由和替代的方案。</li>
     * <li>2. 通过JDK提供的<b>@Deprecated</b>注解。</li>
     * </ul>
     * <p>在界面上我们可以看到方法的请求路径后面多了一个<b>deprecated</b>徽章，并且鼠标放上取还能显示过时理由及替代方案。</p>
     *
     * @param pageNo   页码， 从1开始
     * @param pageSize 页面容量， 大于0
     * @param keyword  模糊搜索关键字
     * @return 分页用户数据及符合条件的总用户数
     * @deprecated 关键字模糊匹配方式的搜索性能太差，请使用<u>xxx</u>方法。
     */
    @ResponseBody
    @RequestMapping(value = "/{pageNo}/{pageSize}", method = RequestMethod.GET)
    public UserQueryResult query(
            @PathVariable("pageNo") int pageNo,
            @PathVariable("pageSize") int pageSize,
            @MatrixVariable(value = "keyword", pathVar = "pageSize", required = false) String keyword
    ) {
        UserQueryResult result = new UserQueryResult();

        if (pageNo <= 0 || pageSize <= 0) {
            result.setCode(400);
            result.setMessage("页码和页面容量均不能小于或等于0");
            return result;
        }

        result.setTotal(Integer.MAX_VALUE);

        List<User> users = new ArrayList<>();
        for (long i = pageNo * pageSize; i < (pageNo + 1) * pageSize; i++) {
            User user = new User();
            user.setId(i);
            user.setUsername("username");
            user.setPassword("password");
            user.setNickname("nickname");
            user.setGender(Gender.FEMALE);
            user.setBirthday(new Date());
            user.setAge(18);
            users.add(user);
        }
        result.setUsers(users);

        return result;
    }

}
