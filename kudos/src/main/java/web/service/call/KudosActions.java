package web.service.call;

import web.service.ConnectionService;
import web.service.Response;

import java.util.concurrent.ExecutionException;

/**
 * Created by chc on 15.8.19.
 */
public class KudosActions extends ConnectionService {

    private static final String USER_CALL_ADDRESS="/kudos/";

    private static final String GET_RECEIVED_KUDOS_AMOUNT_ADDRESS="received-kudos";

    private static final String GIVE_KUDOS_ADDRESS="send-kudos?";
    private static final String RECEIVER_EMAIL_TAG = "receiverEmail=";
    private static final String MESSAGE_TAG = "&message=";
    private static final String AMOUNT_TAG = "&amount=";

    public static int getReceivedKudos() throws ExecutionException, InterruptedException {
        Response response = getJSONGet(USER_CALL_ADDRESS+GET_RECEIVED_KUDOS_AMOUNT_ADDRESS);
        int responseCode = response.getCode();
        if(responseCode != 200){
            return responseCode;
        }
        return Integer.parseInt(response.getMessage());
    }

    public static Response giveKudos(String receiverEmail, int amount, String message) throws ExecutionException, InterruptedException {
        String address = USER_CALL_ADDRESS + GIVE_KUDOS_ADDRESS + RECEIVER_EMAIL_TAG + receiverEmail +
                AMOUNT_TAG + amount + MESSAGE_TAG + message;
        return getJSONPost(address);
    }

}
