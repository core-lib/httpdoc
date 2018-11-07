package io.httpdoc.sample;

/**
 * API结果超类
 *
 * @author 杨昌沛 646742615@qq.com
 * 2018/11/5
 */
public class ApiResult {
    /**
     * 状态码，0代表成功，非0则代表失败，且不同的状态码对应不同的错误类型
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
