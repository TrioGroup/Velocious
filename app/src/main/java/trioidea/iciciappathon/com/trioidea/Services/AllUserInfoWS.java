package trioidea.iciciappathon.com.trioidea.Services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import trioidea.iciciappathon.com.trioidea.DTO.AllUsersInfoDTO;
import trioidea.iciciappathon.com.trioidea.DTO.AuthenticateDto;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;

/**
 * Created by asus on 10/04/2017.
 */
public class AllUserInfoWS {
    public EventResponse allUserInfo(String userName) {
        String response = WebService.getJSON("https://retailbanking.mybluemix.net/banking/icicibank/participantmapping?client_id=" + userName);
        Gson gson = new Gson();
        try {
            JSONArray jsonArray = new JSONArray(response);
                   /* Type type = new TypeToken<List<PostsDto>>() {
                    }.getType();
                    ArrayList<PostsDto> postsDto = gson.fromJson(jsonArray.toString(), type);
                    return postsDto;*/
            Type type = new TypeToken<List<AllUsersInfoDTO>>() {
            }.getType();
            Log.e("AllUser",jsonArray.toString());
            ArrayList<AllUsersInfoDTO> allUsersInfoDTOs = gson.fromJson(jsonArray.toString(), type);
            EventResponse eventResponse = new EventResponse((Object)allUsersInfoDTOs, EventNumbers.All_USER_INFO);
            return eventResponse;
        } catch (Exception e) {
            return null;
        }
    }
}

