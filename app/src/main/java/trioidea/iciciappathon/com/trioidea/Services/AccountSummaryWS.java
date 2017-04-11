package trioidea.iciciappathon.com.trioidea.Services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;

import trioidea.iciciappathon.com.trioidea.DTO.AuthenticateDto;
import trioidea.iciciappathon.com.trioidea.DTO.BankAccountSummaryDTO;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;

/**
 * Created by asus on 11/04/2017.
 */
public class AccountSummaryWS {
    public EventResponse getAccountSummary(String userName, String token, long custId,long accountNumber) {

        String response = WebService.getJSON("https://retailbanking.mybluemix.net/banking/icicibank/account_summary?client_id="+userName+"&token="+token+"&custid="+custId+"&accountno="+accountNumber);
        Gson gson = new Gson();
        try {
            JSONArray jsonArray = new JSONArray(response);
                   /* Type type = new TypeToken<List<PostsDto>>() {
                    }.getType();
                    ArrayList<PostsDto> postsDto = gson.fromJson(jsonArray.toString(), type);
                    return postsDto;*/
            Type type = new TypeToken<BankAccountSummaryDTO>() {
            }.getType();
            Log.e("BankAccountSummary",jsonArray.toString());
            BankAccountSummaryDTO bankAccountSummaryDTO = gson.fromJson(jsonArray.toString(), type);
            EventResponse eventResponse=new EventResponse((Object)bankAccountSummaryDTO, EventNumbers.ACCOUNT_SUMMARY);
            return eventResponse;
        } catch (Exception e) {
            return null;
        }

    }
}
