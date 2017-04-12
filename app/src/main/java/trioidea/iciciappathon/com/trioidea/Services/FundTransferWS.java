package trioidea.iciciappathon.com.trioidea.Services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;

import trioidea.iciciappathon.com.trioidea.DTO.BalanceEnquiryDTO;
import trioidea.iciciappathon.com.trioidea.DTO.FundTransferDto;
import trioidea.iciciappathon.com.trioidea.DTO.TransactionDto;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;

/**
 * Created by asus on 11/04/2017.
 */
public class FundTransferWS {
    // client_id, tokenId, transactionDtoArrayList[j].getSenderID(), transactionDtoArrayList[j].getReceiverId(), transactionDtoArrayList[j].getAmount()
    public EventResponse fundTransfer(String userName, String token, TransactionDto[] transactionDtos) {
        FundTransferDto[] fundTransferDtos = new FundTransferDto[transactionDtos.length];
        for (int i = 0; i < transactionDtos.length; i++) {
            String response = WebService.getJSON("https://retailbanking.mybluemix.net/banking/icicibank/fundTransfer?client_id=" + userName + "&token=" + token + "&srcAccount=" + transactionDtos[i].getSenderID() + "&destAccount=" + transactionDtos[i].getReceiverId() + "&amt=" + transactionDtos[i].getAmount());
            Gson gson = new Gson();
            try {
                JSONArray jsonArray = new JSONArray(response);
                   /* Type type = new TypeToken<List<PostsDto>>() {
                    }.getType();
                    ArrayList<PostsDto> postsDto = gson.fromJson(jsonArray.toString(), type);
                    return postsDto;*/
                Type type = new TypeToken<FundTransferDto>() {
                }.getType();
                Log.e("Fund Transfer", jsonArray.toString());
                FundTransferDto fundTransferDto = gson.fromJson(jsonArray.toString(), type);
                fundTransferDtos[i] = fundTransferDto;
            } catch (Exception e) {
                return null;
            }
        }
        EventResponse eventResponse = new EventResponse((Object) fundTransferDtos, EventNumbers.FUND_TRANSFER);
        return eventResponse;
    }
}
