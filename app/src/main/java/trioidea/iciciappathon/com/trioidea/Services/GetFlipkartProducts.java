package trioidea.iciciappathon.com.trioidea.Services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import trioidea.iciciappathon.com.trioidea.DTO.FlipkartProductInfoList;
import trioidea.iciciappathon.com.trioidea.DTO.FundTransferDto;
import trioidea.iciciappathon.com.trioidea.DTO.ItemLookUpDto;
import trioidea.iciciappathon.com.trioidea.DTO.TransactionDto;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;

/**
 * Created by asus on 23/04/2017.
 */
public class GetFlipkartProducts {
    EventResponse eventResponse;
    public EventResponse getProducts(String query) {
        try {
            String response = WebService.getFlipkartJSON("https://affiliate-api.flipkart.net/affiliate/1.0/search.json?query="+query+"&resultCount=10");
            JsonElement jsonElement = new JsonParser().parse(response);
            JsonObject jsonObject1 = jsonElement.getAsJsonObject();
            JsonArray jsonArray = jsonObject1.getAsJsonArray("productInfoList");


            Gson gson = new GsonBuilder().create();
            List<FlipkartProductInfoList> item = gson.fromJson(jsonArray.toString(), new TypeToken<List<FlipkartProductInfoList>>() {}.getType());
            eventResponse=new EventResponse((Object)item, EventNumbers.FLIPKART_GET_ITEM);
        } catch (Exception e) {
            e.printStackTrace();
            eventResponse=new EventResponse((Object)null, EventNumbers.FLIPKART_GET_ITEM);
        }
        return eventResponse;
    }
}
