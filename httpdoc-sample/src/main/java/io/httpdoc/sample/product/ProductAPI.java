package io.httpdoc.sample.product;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Random;

/**
 * <p><b>产品管理接口</b></p>
 * <p>该示例用于展示如何给<b>Controller</b>取一个别名用于界面上的显示并且做一个概要描述，同样我们有两种方式：</p>
 * <ul>
 * <li>1. 通过注释，在注释下方添加一个<b>@name</b>标签</li>
 * <li>2. 通过注解，在类上标注一个<b>@Name</b>注解</li>
 * </ul>
 * <p>在示例中我们使用第一种方式，即添加<b>@name 产品接口</b>的注释标签来达到我们的目的，这样我们在界面上就看到<b>产品接口</b>而不是<b>ProductAPI</b>，</p>
 * <p>并且后面还有一行小字写着<b>产品的基础增/删/改/查接口</b>，当接口非常多的时候或者别的开发人员或测试人员看起来就清晰多了。</p>
 * <p>这个特性还可以用于<b>Model</b>类上。</p>
 *
 * @tag 基础模块
 * @tag 产品模块
 * @name 产品接口
 * @summary 产品的基础增/删/改/查接口
 * @order 2
 */
@Controller
@RequestMapping("/products")
public class ProductAPI {

    /**
     * <p><b>产品创建接口</b></p>
     * <p>该示例用于展示如何调整<b>Controller</b>内部的方法显示顺序，缺省情况下，方法的显示顺序采用<b>method + " " + path</b>的字符串排序规则作为顺序。</p>
     * <p>假如有排序的需求，可以通过两种方式对<b>Controller</b>的内部方法显示进行排序：</p>
     * <ul>
     * <li>1. 通过注释，在方法的注释中加上<b>@order</b>标签</li>
     * <li>2. 通过注解，在方法上标注<b>@Order</b>注解</li>
     * </ul>
     * <p>两种方式指定的序号都必须是一个正负整数，且排序规则为从小到大。同样的，我们可以采用这两种方式给<b>Controller</b>和<b>Model</b>以及<b>Model</b>类中的字段进行显示排序。</p>
     *
     * @param product 产品对象
     * @return 产品创建结果
     * @order 1
     * @summary @order 1
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public ProductCreateResult create(@RequestBody Product product) {
        ProductCreateResult result = new ProductCreateResult();

        if (StringUtils.isEmpty(product.getName())) {
            result.setCode(400);
            result.setMessage("产品名称不能为空");
            return result;
        }

        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            result.setCode(400);
            result.setMessage("产品价格不能为空或小于等于0");
            return result;
        }

        result.setProductId(Math.abs(new Random().nextLong()));
        return result;
    }

    /**
     * <p><b>产品更新接口</b></p>
     * <p>该示例用于展示当<b>Model</b>类中有间接或直接的自引用/递归属性时框架会渲染到指定的最大深度时自动停止</p>
     * <p>例如<b>Product</b>模型中的<b>category</b>属性，即产品类别是常见的自引用/递归属性，因为其<b>parent</b>属性的类型仍然是<b>Category</b></p>
     * <p>缺省情况下，框架渲染最大深度为5，但可以通过右上角的<b>Global Settings</b> -> <b>Schema</b> -> <b>max depth</b>进行设置以满足不同的需求。</p>
     * <p>同样的，全局设置里面的其他设置也在这里做一个简单描述：</p>
     * <ul>
     * <li>1. Basic: 基础设置，用于设定接口测试的协议，域名，端口以及根路径</li>
     * <li>2. Schema: 模型设置，用于设定模型展示的样式，包括缩进，最大深度，是否显示注释，缺省JSON-Editor样式</li>
     * <li>3. XMLHttpRequest: 请求设置，用于设定用户名，密码，是否异步，超时时间，跨域凭证传输</li>
     * <li>4. Query: 查询参数，用于设定请求的固定查询参数</li>
     * <li>5. Header: 头部参数，用于设定请求的固定头部参数</li>
     * <li>6. Cookie: Cookie参数，用于设定请求的固定Cookie参数</li>
     * </ul>
     * @param id 产品ID
     * @param product 产品对象
     * @return 产品创建结果
     */
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {"application/json", "application/xml"}, produces = {"application/json", "application/xml"})
    public ProductUpdateResult update(@PathVariable("id") Long id, @RequestBody Product product) {
        ProductUpdateResult result = new ProductUpdateResult();

        if (StringUtils.isEmpty(product.getName())) {
            result.setCode(400);
            result.setMessage("产品名称不能为空");
            return result;
        }

        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            result.setCode(400);
            result.setMessage("产品价格不能为空或小于等于0");
            return result;
        }

        return result;
    }

    /**
     * <p><b>产品按名称获取方法</b></p>
     * <p>该示例用于展示由于排序参数的作用，让本来排在<b>POST /products</b>上面的而现在排在其下面。</p>
     * <p>另外展示不仅可以改变<b>API</b>元素的相对位置，我们还可以通过两种方式隐藏某些不希望展示在文档界面上的<b>Controller</b>和方法以及<b>Model</b>字段。</p>
     * <p>在该<b>Controller</b>中实际上有三个开放方法：</p>
     * <ul>
     * <li>1. POST /products</li>
     * <li>2. GET /products/{name}</li>
     * <li>3. DELETE /products/{id}</li>
     * </ul>
     * <p>但实际上显示了其中的1和2，并没有显示3，这是由于<b>DELETE /products/{id}</b>方法的注释上有一个<b>@skip</b>标签，框架跳过了这个方法的解析。</p>
     * <p>同样的，也可以采用标注注解<b>@Skip</b>的方式来命令框架跳过某个方法的解析。另外，<b>Controller</b>以及<b>Model</b>字段也可以采用这两种方式来隐藏。</p>
     *
     * @param name 产品名称
     * @return 产品获取结果
     * @order 2
     * @summary @order 2
     */
    @ResponseBody
    @RequestMapping(value = "/{name}", method = RequestMethod.GET, produces = {"application/json", "application/xml"})
    public ProductQueryResult query(@PathVariable("name") String name) {
        ProductQueryResult result = new ProductQueryResult();

        Product product = new Product();
        product.setId(Math.abs(new Random().nextLong()));
        product.setName(name);
        product.setPrice(new BigDecimal(998));
        result.setProduct(product);

        return result;
    }

    /**
     * 方法被隐藏，但实际上方法完全可以使用，只是在界面上不显示了。
     *
     * @param id 产品ID
     * @return 产品删除结果
     * @skip
     */
    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {"application/json", "application/xml"})
    public ProductDeleteResult delete(@PathVariable("id") Long id) {
        return new ProductDeleteResult();
    }
}
