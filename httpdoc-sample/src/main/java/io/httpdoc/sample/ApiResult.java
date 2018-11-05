package io.httpdoc.sample;

/**
 * API结果超类
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/11/5
 */
public class ApiResult {
    /**
     * 状态码
     */
    private int code = 0;
    /**
     * 消息
     */
    private String message = "OK";

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
