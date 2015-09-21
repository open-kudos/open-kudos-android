package web.service.call;

import web.service.ConnectionService;
import web.service.Response;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by chc on 15.8.19.
 */
public class AuthenticateActions extends ConnectionService {

    private static final String LOGIN_CALL_ADDRESS="/login?";
    private static final String LOGOUT_CALL_ADDRESS="/logout";

    private static final String EMAIL_TAG="email=";
    private static final String PASSWORD_TAG="&password=";

    private static final String REGISTER_CALL_ADDRESS="/register?";
    private static final String NAME_TAG = "&name=";
    private static final String SURNAME_TAG = "&surname=";
    private static final String CONFIRM_PASSWORD_TAG = "&confirmPassword=";

    public static Response login(String email, String password) throws IOException, ExecutionException, InterruptedException {
        String url = LOGIN_CALL_ADDRESS+EMAIL_TAG + email + PASSWORD_TAG + password;
        return getJSONPost(url);
    }

    public static Response register(String email, String password, String confirmPassword) throws ExecutionException, InterruptedException {
        String url = REGISTER_CALL_ADDRESS +EMAIL_TAG+ email + NAME_TAG + "X" + SURNAME_TAG + "Y" + PASSWORD_TAG + password + CONFIRM_PASSWORD_TAG + confirmPassword;
        return getJSONPost(url);
    }

    public static Response logout() throws ExecutionException, InterruptedException {
        return getJSONGet(LOGOUT_CALL_ADDRESS);
    }

}
