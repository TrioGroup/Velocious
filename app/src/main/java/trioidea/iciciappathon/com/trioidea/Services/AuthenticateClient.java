package trioidea.iciciappathon.com.trioidea.Services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import rx.Observable;
import trioidea.iciciappathon.com.trioidea.DTO.AuthenticateDto;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;

/**
 * Created by asus on 09/04/2017.
 */
public class AuthenticateClient {

    public EventResponse authenticateClientWB(String userName,String password) {

        String response = WebService.getJSON("https://corporateapiprojectwar.mybluemix.net/corporate_banking/mybank/authenticate_client?client_id="+userName+"&password="+password);
        Gson gson = new Gson();
        try {
            JSONArray jsonObject = new JSONArray(response);
                   /* Type type = new TypeToken<List<PostsDto>>() {
                    }.getType();
                    ArrayList<PostsDto> postsDto = gson.fromJson(jsonArray.toString(), type);
                    return postsDto;*/
            Type type = new TypeToken<ArrayList<AuthenticateDto>>() {
            }.getType();
            Log.e("AllUser",jsonObject.toString());
            ArrayList<AuthenticateDto> authenticateDto = gson.fromJson(jsonObject.toString(), type);
            EventResponse eventResponse=new EventResponse((Object)authenticateDto, EventNumbers.AUTHENTICATE_USER);
            return eventResponse;
        } catch (Exception e) {
            return null;
        }

    }
}