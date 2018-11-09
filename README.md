# **HttpDoc** [![](https://www.jitpack.io/v/core-lib/httpdoc.svg)](https://www.jitpack.io/#core-lib/httpdoc)
##### 基于Java标准doc注释构建的代码零侵入的HTTP RESTful API在线阅览文档及测试界面框架

## **功能特性**
* 基础功能无需为配合HttpDoc框架而多写一句代码，甚至连doc注释都不必写，即可拥有项目的API文档和测试界面。
* 遵循 [RFC 2616 HTTP/1.1](https://tools.ietf.org/html/rfc2616) 规范，适配主流后台WEB框架。
* 拓展多个 Java Doc 注释标签，满足不同的文档阅览及在线测试需求。
* 一键生成SDK，支持多个平台，让前后台以及跨平台对接变得更简单。
* WEB服务器无关，同时支持 Spring Boot 命令方式启动。
* 支持 Maven Gradle 或JAR包依赖。

## **环境依赖**
JDK 1.7 +

## **集成步骤**
### Maven
1. 引入依赖
```xml
<project>
    <!-- 设置 jitpack.io 仓库 -->
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://www.jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <!-- 添加 HttpDoc 依赖 -->
        <dependency>
            <groupId>com.github.core-lib.httpdoc</groupId>
            <artifactId>httpdoc-spring-mvc</artifactId>
            <version>v1.5.3</version>
        </dependency>
        
        <!-- 添加 jdk 的 tools.jar 依赖, 用于解析源码注释。如果采用这种方式部署到Tomcat中需要往Tomcat的lib目录增加该tools.jar -->
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
      <version>v1.5.2</version>
    </dependency>
    ```
    
## 参数说明
* httpdoc       项目名称，缺省为HttpDoc
* version       项目版本，缺省为1.0.0
* description   项目描述，可以用<![CDATA[]]>套起来并使用HTML标签语法
* protocol      访问协议，http或https，缺省为request.getProtocol();
* hostname      主机名，缺省为request.getServerName();
* port          端口号，缺省为request.getServerPort();
* context       容器路径，缺省为request.getContextPath();
* dateFormat    日期格式，缺省为yyyy-MM-dd HH:mm:ss
* translator    文档翻译器，缺省为自动匹配当前项目的WEB框架
* interpreter   文档解释器，缺省为源码解释器
* serializer    文档序列化器，缺省为JSON序列化器，所以项目中需要依赖jackson-databind
    
## 在线示例
项目中的httpdoc-sample模块就是一个HttpDoc + SpringMVC的一个标准示例，可checkout后查看源码和编译运行查看效果，也可立即预览：[httpdoc-sample](http://47.106.196.10:8090/httpdoc-sample/httpdoc-ui/index.html)

## 变更记录
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