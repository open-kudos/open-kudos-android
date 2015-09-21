package web.service;

/**
 * Created by chc on 15.8.19.
 */
public class Response {

    private int code;
    private String message;

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
