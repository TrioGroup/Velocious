package trioidea.iciciappathon.com.trioidea.Services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import trioidea.iciciappathon.com.trioidea.DTO.BalanceEnquiryDTO;
import trioidea.iciciappathon.com.trioidea.DTO.CheckTransactionDTO;
import trioidea.iciciappathon.com.trioidea.DTO.TransactionDto;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;

/**
 * Created by asus on 17/04/2017.
 */
public class CheckTransactionWS {
    public EventResponse checkOnServer(TransactionDto[] transactionDtos) {
        try {
            for (int i = 0; i < transactionDtos.length - 1; i++) {
                String response = WebService.getJSON("http://appathon.16mb.com/index.php/welcome/check?time=" + transactionDtos[i].getTime() + "&sender=" + transactionDtos[i].getSenderID() + "&receiver=" + transactionDtos[i].getReceiverId());
                Gson gson = new Gson();
                JSONObject jsonArray = new JSONObject(response);
                   /* Type type = new TypeToken<List<PostsDto>>() {
                    }.getType();
                    ArrayList<PostsDto> postsDto = gson.fromJson(jsonArray.toString(), type);
                    return postsDto;*/
                Type type = new TypeToken<CheckTransactionDTO>() {
                }.getType();
                Log.e("Balance enquiry", jsonArray.toString());
                CheckTransactionDTO checkTransactionDTOArrayList = gson.fromJson(jsonArray.toString(), type);
                if (!transactionDtos[i].isSyncFlag() && checkTransactionDTOArrayList.getStatus().equalsIgnoreCase("success")) {
                    if (checkTransactionDTOArrayList.getData().equalsIgnoreCase("1"))
                        transactionDtos[i].setSyncFlag(true);
                    else
                        transactionDtos[i].setSyncFlag(false);
                }
            }
        } catch (Exception e) {
            return null;
        }
        EventResponse eventResponse = new EventResponse((Object) transactionDtos, EventNumbers.CHECK_TRANSACTION_EVENT);
        return eventResponse;
    }

}
