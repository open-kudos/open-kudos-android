package web.service.call;

import android.util.Log;
import org.apache.commons.lang3.StringEscapeUtils;
import web.service.ConnectionService;
import web.service.Response;

import java.util.concurrent.ExecutionException;

/**
 * Created by chc on 15.8.19.
 */
public class ChallengesActions extends ConnectionService {

    private static final String CHALLENGE_ADDRESS="/challenges/";

    private static final String CHALLENGE_CREATE_ADDRESS = "create?";

    private static final String PARTICIPANT_TAG = "participant=";
    private static final String REFEREE_TAG = "&referee=";
    private static final String NAME_TAG = "&name=";
    private static final String FINISH_DATE_TAG = "&finishDate=";
    private static final String AMOUNT_TAG = "&amount=";

    public static Response createChallenge(String participantEmail, String refereeEmail, String kudosAmount, String finishDate, String name) throws ExecutionException, InterruptedException {
        String address = CHALLENGE_ADDRESS + CHALLENGE_CREATE_ADDRESS +
                PARTICIPANT_TAG + participantEmail +
                REFEREE_TAG + refereeEmail +
                NAME_TAG + name +
                AMOUNT_TAG + kudosAmount+
                FINISH_DATE_TAG + finishDate;
        String newAddress = address.replace(" ","%20");
        Log.d("createChallenge", newAddress);
        return getJSONPost(newAddress);
    }

}
