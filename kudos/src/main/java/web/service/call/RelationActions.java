package web.service.call;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import web.service.ConnectionService;
import web.service.Response;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by chc on 15.8.24.
 */
public class RelationActions extends ConnectionService {

    private static final String RELATIONS_ADDRESS = "/relations";
    private static final String GET_FOLLOWED_ADDRESS = "/followed";

    public static List<String> getFollowedPeople() throws ExecutionException, InterruptedException {
        Response response = getJSONGet(RELATIONS_ADDRESS + GET_FOLLOWED_ADDRESS);
        if(response.getCode() != 200){
            return null;
        }
        Type listType = new TypeToken<List<String>>(){}.getType();
        return new Gson().fromJson(response.getMessage(),listType);
    }

}
