package trioidea.iciciappathon.com.trioidea.Services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

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
        TransactionDto[] transactionDtos1 = new TransactionDto[transactionDtos.length];
        try {
            for (int i = 0; i < transactionDtos.length; i++) {
                String response = WebService.getJSON("http://velocious.epizy.com/index.php/welcome/check?time=" + transactionDtos[i].getTime() + "&sender=" + transactionDtos[i].getSenderID() + "&receiver=" + transactionDtos[i].getReceiverId());
                Gson gson = new Gson();
                JSONArray jsonArray = new JSONArray(response);
                   /* Type type = new TypeToken<List<PostsDto>>() {
                    }.getType();
                    ArrayList<PostsDto> postsDto = gson.fromJson(jsonArray.toString(), type);
                    return postsDto;*/
                Type type = new TypeToken<ArrayList<CheckTransactionDTO>>() {
                }.getType();
                Log.e("Balance enquiry", jsonArray.toString());
                ArrayList<CheckTransactionDTO> checkTransactionDTOArrayList = gson.fromJson(jsonArray.toString(), type);
                if(!transactionDtos1[i].isSyncFlag() && checkTransactionDTOArrayList.get(0).getStatus().equalsIgnoreCase("success"))
                {
                    transactionDtos1[i].setSyncFlag(true);
                }
            }
        } catch (Exception e) {
            return null;
        }
        EventResponse eventResponse = new EventResponse((Object) transactionDtos1, EventNumbers.CHECK_TRANSACTION_EVENT);
        return eventResponse;
    }

}
