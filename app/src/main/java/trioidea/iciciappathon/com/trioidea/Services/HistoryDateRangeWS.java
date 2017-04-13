package trioidea.iciciappathon.com.trioidea.Services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;

import trioidea.iciciappathon.com.trioidea.DTO.BankAccountSummaryDTO;
import trioidea.iciciappathon.com.trioidea.DTO.HistoryDateRangeDTO;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;

/**
 * Created by asus on 13/04/2017.
 */
public class HistoryDateRangeWS {
    public EventResponse getHistory(String userName, String token, long accountNumber, java.util.Date fromDate, java.util.Date toDate) {

        String response = WebService.getJSON("https://retailbanking.mybluemix.net/banking/icicibank/transactioninterval?client_id="+userName+"&accountno="+accountNumber+"&fromdate="+fromDate+"&todate="+toDate);
        Gson gson = new Gson();
        try {
            JSONArray jsonArray = new JSONArray(response);
                   /* Type type = new TypeToken<List<PostsDto>>() {
                    }.getType();
                    ArrayList<PostsDto> postsDto = gson.fromJson(jsonArray.toString(), type);
                    return postsDto;*/
            Type type = new TypeToken<ArrayList<HistoryDateRangeDTO>>() {
            }.getType();
            Log.e("BankAccountSummary",jsonArray.toString());
            ArrayList<HistoryDateRangeDTO> historyDateRangeDTOs = gson.fromJson(jsonArray.toString(), type);
            EventResponse eventResponse=new EventResponse((Object)historyDateRangeDTOs, EventNumbers.HISTORY_DATE_RANGE);
            return eventResponse;
        } catch (Exception e) {
            return null;
        }

    }
}
