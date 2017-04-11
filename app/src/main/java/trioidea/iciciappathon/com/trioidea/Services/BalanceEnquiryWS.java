package trioidea.iciciappathon.com.trioidea.Services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import trioidea.iciciappathon.com.trioidea.DTO.AllUsersInfoDTO;
import trioidea.iciciappathon.com.trioidea.DTO.BalanceEnquiryDTO;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;

/**
 * Created by asus on 11/04/2017.
 */
public class BalanceEnquiryWS {
    public EventResponse balanceEnquiry(String userName,String token,long accountNumber) {
        String response = WebService.getJSON("https://retailbanking.mybluemix.net/banking/icicibank/balanceenquiry?client_id="+userName+"&token="+token+"&accountno="+accountNumber);
        Gson gson = new Gson();
        try {
            JSONArray jsonArray = new JSONArray(response);
                   /* Type type = new TypeToken<List<PostsDto>>() {
                    }.getType();
                    ArrayList<PostsDto> postsDto = gson.fromJson(jsonArray.toString(), type);
                    return postsDto;*/
            Type type = new TypeToken<BalanceEnquiryDTO>() {
            }.getType();
            Log.e("Balance enquiry",jsonArray.toString());
            BalanceEnquiryDTO balanceEnquiryDTOArrayList = gson.fromJson(jsonArray.toString(), type);
            EventResponse eventResponse = new EventResponse((Object)balanceEnquiryDTOArrayList, EventNumbers.BALANCE_ENQUIRY);
            return eventResponse;
        } catch (Exception e) {
            return null;
        }
    }
}
