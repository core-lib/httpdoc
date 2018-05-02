package io.httpdoc.core.appender;

/**
 * 控制台拼接器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-02 15:12
 **/
public class ConsoleAppender extends LineAppenderWrapper<ConsoleAppender> {

    public ConsoleAppender() {
        super(new AppendableAppender(System.out));
    }

}
