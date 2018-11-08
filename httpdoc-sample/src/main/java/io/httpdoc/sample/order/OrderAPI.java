package io.httpdoc.sample.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * <p><b>致谢</b></p>
     * <p>
     * <a target="_blank" href="http://www.bootcss.com/" title="Bootstrap">
     * <img src="http://www.runoob.com/wp-content/uploads/2013/10/bs.png" style="height: 80px;" />
     * </a>
     * <a target="_blank" href="https://jquery.com/" style="margin-left: 10px;" title="jQuery">
     * <img src="http://jquery.com/jquery-wp-content/themes/jquery/images/logo-jquery.png" style="background-color: #6495ED; height: 80px;" />
     * </a>
     * <a target="_blank" href="https://mustache.github.io/" style="margin-left: 10px; height: 80px;" title="Mustache">
     * <span style="font-size: 56px; line-height: 80px; vertical-align: middle;">{{Mustache}}</span>
     * </a>
     * </p>
     */
    @ResponseBody
    @RequestMapping("/thanks")
    public String thanks() {
        return "Thanks";
    }

}
