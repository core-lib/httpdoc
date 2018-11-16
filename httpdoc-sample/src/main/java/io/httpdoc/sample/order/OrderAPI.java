package io.httpdoc.sample.order;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Random;

/**
 * <p style="color: #337ab7; font-size: 16px;"><b>美化界面</b></p>
 * <p style="color: #5cb85c;">该示例用于展示基于Java的标准注释模板+简单的<b>html</b>标签来美化文档的界面。</p>
 * <p style="color: #31b0d5;">在前面的例子中我们看到文档中虽然样子都差不多，但是其中不乏有字体加粗和列表显示的例子。其实还能支持更多的<b>html</b>标签语法，</p>
 * <p style="color: #f0ad4e;">而这种语法本身就是Java注释支持的，即便我们要用<b>javadoc</b>命令来生成项目文档时，这些标签也是必不可少的。</p>
 * <p style="color: #d9534f;">而<b>HttpDoc</b>框架能充分利用这种规范来实现代码零侵入和不增加工作量的方式就能使我们拥有一份<b>API</b>文档和一个简单的测试界面。</p>
 * <p style="color: #777;"><i>更多功能请点击链接查看<a target="_blank" href="https://github.com/core-lib/httpdoc">HttpDoc Github</a></i></p>
 *
 * @tag 订单模块
 * @order 4
 */
@Controller
@RequestMapping
public class OrderAPI {

    /**
     * <b>订单创建接口</b>
     * <p>该示例用于展示如何通过注释来调整参数模块JSON-Editor的显示样式，默认情况下，JSON-Editor的显示样式为每个属性占一整行，</p>
     * <p>当一个<b>Model</b>类中的属性比较多时，那一屏的空间都不能完整的显示一个<b>Model</b>，而且一些输入框或下拉框不必显示成一行那么长。</p>
     * <p>这时可以通过给<b>Model</b>类，属性或者方法的参数增加一个注释标签<b>@style</b>来控制界面的布局，可选值为normal | table | grid。</p>
     * <p>另外，方法上的写法为【<b>@style</b> 参数名 布局名称】。例如该示例的order参数：<b>@style</b> order grid，表示采用表格形式显示<b>Order</b>模型。</p>
     * <p>如果作用于<b>Model</b>类或其属性上则不需要名称，只需要【<b>@style</b> 布局名称】即可，例如<b>@style</b> table</p>
     * <p>需要注意的是，当<b>Order</b>类上也有<b>@style</b>注释标签时，该参数的<b>@style</b>优先级更高，同理属性上的<b>@style</b>优先级也比<b>Model</b>类上的高。</p>
     * <p>和其他注释标签一样，<b>@style</b>也有注解的形式<b>@Style</b>，可用于类型，字段，getter方法以及参数上。</p>
     *
     * @param order 订单
     * @return 订单创建结果
     * @style order grid
     */
    @ResponseBody
    @RequestMapping(value = "/order", method = RequestMethod.POST, consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public OrderCreateResult create(@RequestBody Order order) {
        OrderCreateResult result = new OrderCreateResult();

        if (StringUtils.isEmpty(order.getUserId())) {
            result.setCode(400);
            result.setMessage("用户ID为空");
            return result;
        }
        if (StringUtils.isEmpty(order.getAddress())) {
            result.setCode(400);
            result.setMessage("收货地址为空");
            return result;
        }
        if (StringUtils.isEmpty(order.getTelephone())) {
            result.setCode(400);
            result.setMessage("电话号码为空");
            return result;
        }
        if (order.getItems() == null || order.getItems().isEmpty()) {
            result.setCode(400);
            result.setMessage("订单项为空");
            return result;
        }

        result.setAmount(new BigDecimal(998));
        result.setOrderId(Math.abs(new Random().nextLong()));

        return result;
    }

    /**
     * <p><b>致谢</b></p>
     * <p>
     * <a target="_blank" href="http://www.bootcss.com/" title="Bootstrap">
     * <img src="http://www.runoob.com/wp-content/uploads/2013/10/bs.png" style="height: 56px;" />
     * </a>
     * <a target="_blank" href="https://jquery.com/" style="margin-left: 10px;" title="jQuery">
     * <img src="http://jquery.com/jquery-wp-content/themes/jquery/images/logo-jquery.png" style="background-color: #6495ED; height: 56px;" />
     * </a>
     * <a target="_blank" href="https://mustache.github.io/" style="margin-left: 10px; height: 56px;" title="Mustache">
     * <span style="font-size: 36px; line-height: 56px; vertical-align: middle;">{{Mustache}}</span>
     * </a>
     * <a target="_blank" href="https://github.com/jdorn/json-editor" style="margin-left: 10px; height: 56px;" title="JSON-Editor">
     * <span style="font-size: 36px; line-height: 56px; vertical-align: middle;">JSON-Editor</span>
     * </a>
     * </p>
     */
    @ResponseBody
    @RequestMapping("/thanks")
    public String thanks() {
        return "Thanks";
    }

}
