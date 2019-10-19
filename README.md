# HttpDoc [![](https://www.jitpack.io/v/core-lib/httpdoc.svg)](https://www.jitpack.io/#core-lib/httpdoc)
##### 基于Java标准doc注释构建的代码零侵入的HTTP RESTful API在线测试，文档阅览以及SDK导出框架，支持Spring-Boot和Spring-MVC
JSON-Editor: [httpdoc-ui](http://106.13.16.25/httpdoc-sample/httpdoc-ui/index.html) TextArea: [httpdoc-ui-v1](http://106.13.16.25/httpdoc-sample/httpdoc-ui-v1/index.html)

## 功能特性
* 基础功能无需为配合HttpDoc框架而多写一句代码，甚至连doc注释都不必写，即可拥有项目的API文档和测试界面。
* 遵循 [RFC 2616 HTTP/1.1](https://tools.ietf.org/html/rfc2616) 规范，适配主流后台WEB框架。
* 拓展多个 Java Doc 注释标签，满足不同的文档阅览及在线测试需求。
* 一键生成SDK，支持多个平台，让前后台以及跨平台对接变得更简单。
* WEB服务器无关，同时支持 Spring Boot 命令方式启动。
* 支持 Maven Gradle 或JAR包依赖。

## 环境依赖
JDK 1.7 +

## 集成步骤
### Maven
1. 引入依赖
```xml
<project>
    <!-- 设置 jitpack.io 仓库 -->
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <!-- 添加 HttpDoc 依赖 -->
        <dependency>
            <groupId>com.github.core-lib.httpdoc</groupId>
            <artifactId>httpdoc-spring-mvc</artifactId>
            <version>v1.8.1</version>
        </dependency>
        
        <!-- 添加JDK的tools.jar依赖用于解析源码注释，采用这种方式部署到Tomcat时需要往Tomcat的lib目录增加该tools.jar -->
        <dependency>
            <groupId>com.sun</groupId>
            <artifactId>tools</artifactId>
            <version>1.8</version>
            <scope>system</scope>
            <systemPath>${env.JAVA_HOME}/lib/tools.jar</systemPath>
        </dependency>
        <!-- 当然还有很多种方式来依赖tools.jar，例如上传到自己的私服或从别的仓库中依赖进来 -->
    </dependencies>
</project>
```

2. 配置插件
```xml
<!-- 由于框架基于源码注释解析来实现，所以保留源码是基础，如果只想要在线测试而没有文档阅览的需求，可不必添加该插件。-->
<!-- 如果项目是多模块项目，需要被解析的源码类分散在多模块中，则其他模块也需要配置该插件，或在父项目的pom.xml中配置该插件。-->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-resources-plugin</artifactId>
    <version>3.1.0</version>
    <configuration>
        <encoding>UTF-8</encoding>
    </configuration>
    <executions>
        <execution>
            <id>copy-src</id>
            <phase>process-sources</phase>
            <goals>
                <goal>copy-resources</goal>
            </goals>
            <configuration>
                <outputDirectory>${project.build.directory}/classes</outputDirectory>
                <resources>
                    <resource>
                        <directory>${basedir}/src/main/java</directory>
                    </resource>
                </resources>
            </configuration>
        </execution>
    </executions>
</plugin>
```

3. 配置参数
* SpringMVC
    * web.xml 中增加一个servlet和servlet-mapping标签
    ```xml
    <web-app>
        <servlet>
            <servlet-name>httpdoc</servlet-name>
            <servlet-class>io.httpdoc.web.HttpdocServletSupport</servlet-class>
            <init-param>
                <param-name>packages</param-name>
                <param-value>io.httpdoc.sample</param-value>
            </init-param>
            <init-param>
                <param-name>httpdoc</param-name>
                <param-value>项目名称</param-value>
            </init-param>
            <init-param>
                <param-name>version</param-name>
                <param-value>项目版本</param-value>
            </init-param>
            <init-param>
                <param-name>description</param-name>
                <param-value>
                    <![CDATA[
                        项目描述(可以内嵌HTML标签)
                    ]]>
                </param-value>
            </init-param>
            <init-param>
                <param-name>dateFormat</param-name>
                <param-value>yyyy-MM-dd HH:mm:ss</param-value>
            </init-param>
            <load-on-startup>1</load-on-startup>
        </servlet>
        
        <servlet-mapping>
            <servlet-name>httpdoc</servlet-name>
            <url-pattern>/httpdoc.json</url-pattern>
        </servlet-mapping>
    </web-app>
    ```
    * spring-servlet.xml 中增加一个标签以允许浏览器访问HttpDoc的页面静态资源
    ```xml
    <mvc:resources mapping="/httpdoc-ui/**" location="classpath:/META-INF/resources/httpdoc-ui/"/>
    ```
    
* Spring Boot
    * 如果是Spring Boot项目则不需要上面SpringMVC的两个配置，实际上 Spring Boot 项目也没有web.xml文件来做配置。
    * 只需要将httpdoc-spring-mvc依赖替换成下面的依赖并在项目入口主类上标注一个@EnableHttpdoc() 注解即可，对应的参数也可以在注解上设置。
    ```xml
    <dependency>
      <groupId>com.github.core-lib.httpdoc</groupId>
      <artifactId>httpdoc-spring-boot</artifactId>
      <version>v1.8.1</version>
    </dependency>
    ```
    ```java
    @SpringBootApplication
    @EnableHttpdoc(
          packages = {"io.httpdoc.sample"},
          httpdoc = "服务名称", 
          version = "服务版本", 
          description = "服务描述-支持HTML语法。"
    )
    public class HttpdocApplication {   
        public static void main(String[] args) {
            SpringApplication.run(HttpdocApplication.class, args);
        }
    }
    ```
    
## 参数说明
| 参数名称 | 参数说明 | 缺省值 |
| :------- | :------- | :----- |
| packages     | 源码包名    | 必填参数，支持配置多个，通过英文逗号，空格及换行符拆分，支持递归搜索 |
| httpdoc      | 项目名称    | HttpDoc |
| version      | 项目版本    | 1.0.0 |
| description  | 项目描述    | 可以用<![CDATA[]]>套起来并使用HTML标签语法 |
| protocol     | 访问协议    | http或https，缺省为request.getProtocol(); |
| hostname     | 主机名      | request.getServerName(); |
| port         | 端口号      | request.getServerPort(); |
| context      | 容器路径    | request.getContextPath(); |
| dateFormat   | 日期格式    | yyyy-MM-dd HH:mm:ss |
| translator   | 文档翻译器  | 自动匹配当前项目的WEB框架 |
| interpreter  | 文档解释器  | 源码解释器 |
| serializer   | 文档序列化器| JSON序列化器，所以项目中需要依赖jackson-databind |

## 在线示例
项目中的httpdoc-sample模块就是一个HttpDoc + SpringMVC的一个标准示例，可checkout后查看源码和编译运行查看效果，也可立即预览：JSON-Editor: [httpdoc-ui](http://106.13.16.25/httpdoc-sample/httpdoc-ui/index.html) TextArea: [httpdoc-ui-v1](http://106.13.16.25/httpdoc-sample/httpdoc-ui-v1/index.html)

## 变更记录
* v1.8.1
    * 适配应用被反向代理后前端测试域名获取不正确问题
* v1.8.0
    * 修复BaseURL与接口相对路径问题
* v1.7.9
    * 修复BaseURL与接口相对路径问题
* v1.7.8
    * UI 默认采用 grid 方式展示API参数
* v1.7.7
    * 升级[LoadKit](https://github.com/core-lib/loadkit)依赖版本解决ANT表达式无法正确匹配**/*通配符的问题
* v1.7.6
    * ObjC 生成时采用 BOOL 代替 bool
* v1.7.5
    * 测试界面 空对象/空值 参数不参与json序列化
* v1.7.4
    * iOS SDK 生成bug修复
* v1.7.3
    * boolean 类型字段 getter 方法从 get 改为 is 开头
* v1.7.2 
    * retrofit2 客户端 增加 RxJava 的支持
* v1.7.1
    * 整理 pom 依赖
    * 解决SDK导出中文乱码问题
    * 升级 Jestful 依赖版本
* v1.7.0
    * 修复 Retrofit 及 Retrofit 2 生成 API 方法时没有带上参数注释问题
* v1.6.9
    * 增加对 retrofit 1 的支持
    * 修改 retrofit 2 模块的包名
* v1.6.8
    * 可通过参数配置SDK导出形式
    * 开放更多第三方拓展能力
* v1.6.7
    * 修复当没有源码时重复构建文档的bug
    * 重构多个 Translator 实现 使之可以更容易拓展
* v1.6.6
    * 修复生成Model中包含java关键字的问题
    * Controller中增加别名支持
* v1.6.5
    * 按照bean属性定义的顺序排列
    * 采用LoadKit替代Detector
* v1.6.4
    * 优化SDK导出的压缩包结构
* v1.6.3
    * 增加packages参数用于筛选需要解析的源码包，支持多个源码包和递归搜索。
    * 优化源码提取逻辑，适配更多启动模式。
* v1.6.2
    * 升级detector框架依赖
* v1.6.1
    * UI 展示的优化
* v1.6.0
    * 采用detector框架的方式进行多模块集成
* v1.5.9
    * 采用SoftReference来引用文档对象，避免空闲时占用过多JVM内存。
    * 增加Exporter接口用于导出SDK
    * 界面上可以下载SDK
* v1.5.8
    * 增加Schema的全局设置
    * 适配递归Schema的问题
* v1.5.7
    * 增加JSONEditor的前端实现
    * 增加@style标签用于控制参数展示的样式
* v1.5.6
    * 修复带中文或空格路径的解析失败bug
    * 适配Unix系统路径分隔符
    * 注释读取日志显示
* v1.5.5
    * 增加Lifecycle接口让实现类可以监听initial和destroy事件以及用户配置信息
* v1.5.4
    * 极大提升源码注释解析速度
    * 修复spring-boot模块的依赖，增加spring-mvc依赖
* v1.5.3
    * 优化项目依赖让项目集成更简单
    * 默认采用JSON文档序列化器
* v1.5.2
    * 第一个正式版发布
* v1.5.1
    * 增加示例模块
    * 增加README.md

## 协议声明
[Apache-2.0](http://www.apache.org/licenses/LICENSE-2.0)

## 联系作者
QQ 646742615 不会钓鱼的兔子
