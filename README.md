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

## **部署步骤**
### Maven
1. 引入依赖
```xml
<!-- 设置 jitpack.io 仓库 -->
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://www.jitpack.io</url>
    </repository>
</repositories>

<!-- 版本配置 -->
<properties>
    <httpdoc.version>v1.5.2</httpdoc.version>
    <jackson.version>2.9.5</jackson.version>
</properties>

<!-- HttpDoc依赖开始 -->
<dependency>
    <groupId>io.httpdoc</groupId>
    <artifactId>httpdoc-web</artifactId>
    <version>${httpdoc.version}</version>
</dependency>
<dependency>
    <groupId>io.httpdoc</groupId>
    <artifactId>httpdoc-jackson</artifactId>
    <version>${httpdoc.version}</version>
</dependency>
<dependency>
    <groupId>io.httpdoc</groupId>
    <artifactId>httpdoc-spring-mvc</artifactId>
    <version>${httpdoc.version}</version>
</dependency>
<dependency>
    <groupId>io.httpdoc</groupId>
    <artifactId>httpdoc-ui</artifactId>
    <version>${httpdoc.version}</version>
</dependency>
<!-- HttpDoc依赖结束 -->

<!-- Jackson依赖开始 如果项目中已有则不需要重复依赖了 -->
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-yaml</artifactId>
    <version>${jackson.version}</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
    <version>${jackson.version}</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>${jackson.version}</version>
</dependency>
<!-- Jackson依赖结束 如果项目中已有则不需要重复依赖了 -->

<!-- Java tools.jar 依赖, 用于解析源码注释。 -->
<dependency>
    <groupId>com.sun</groupId>
    <artifactId>tools</artifactId>
    <version>1.8</version>
    <scope>system</scope>
    <systemPath>${env.JAVA_HOME}/lib/tools.jar</systemPath>
</dependency>

<!-- 如果不想从本地依赖tools.jar也可以上传到自己的私服或从仓库中依赖进来 -->
<repositories>
    <repository>
        <id>nuiton</id>
        <url>http://maven.nuiton.org/release/</url>
    </repository>
</repositories>
<dependency>
    <groupId>com.sun</groupId>
    <artifactId>tools</artifactId>
    <version>1.7.0.13</version>
</dependency>
<!-- 但是这个依赖版本是1.7的，如果项目用了JDK 1.7+ 则有些新语法问题会造成源码注释读取不成功，但不会出错 -->
```

2. 配置插件
```xml
<!-- 由于框架基于源码注释解析来实现，所以保留源码是基础，如果只想要在线测试而没有文档阅览的需求，可不必添加该插件。 -->
<!-- 如果项目是多模块项目，需要被解析的源码类分散在多模块中，则其他模块也需要配置该插件，或直接在父项目的pom.xml中配置该插件。 -->
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
        ```
    * spring-servlet.xml 中增加一个标签以允许浏览器访问HttpDoc的页面静态资源
        ```xml
          <mvc:resources mapping="/httpdoc-ui/**" location="classpath:/META-INF/resources/httpdoc-ui/"/>
        ```
        
* Spring Boot
    * 如果是Spring Boot项目则不需要上面的两个配置。
    * 只需要在项目入口主类上标注一个@EnableHttpdoc() 注解即可，对应的参数也可以在注解上设置。
    
## 在线示例
项目中的httpdoc-sample其实就是一个HttpDoc + SpringMVC的一个标准示例，可checkout后编译查看源码和运行看效果，也可立即预览：

## 变更记录
* v1.5.0 增加示例模块
* v1.5.1 增加README.md
* v1.5.2 第一个正式版发布

## 协议声明
项目遵循 [Apache-2.0](http://www.apache.org/licenses/LICENSE-2.0) 协议
