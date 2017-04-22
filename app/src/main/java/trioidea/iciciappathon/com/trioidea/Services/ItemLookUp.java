package trioidea.iciciappathon.com.trioidea.Services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;
import trioidea.iciciappathon.com.trioidea.DTO.ItemLookUpDto;
import trioidea.iciciappathon.com.trioidea.DTO.SingleItem;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;

/**
 * Created by asus on 21/04/2017.
 */
public class ItemLookUp {
    String signature;

    /*  public static String getSignature(String secret, String message) throws Exception {
          Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
          SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
          sha256_HMAC.init(secret_key);

          String hash = Base64.encodeToString(sha256_HMAC.doFinal(message.getBytes()),Base64.DEFAULT);
          hash = URLEncoder.encode(hash, "UTF-8");

          return hash;
      }*/
    public EventResponse getItem(String itemASIN) {
        EventResponse eventResponse;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        SignedRequestsHelper signedRequestsHelper = new SignedRequestsHelper();
        /*TreeMap<String,String> params=new TreeMap<>();
        params.put("Service","AWSECommerceService");
        params.put("Operation","ItemSearch");
        params.put("SubscriptionId","AKIAIWILA3BYHLAWG5AA");
        params.put("AssociateTag","icici123-20");
        params.put("SearchIndex","All");
        params.put("Keywords","harry");
        params.put("ResponseGroup","ItemAttributes");*/
        String signature = signedRequestsHelper.sign(signedRequestsHelper.createParameterMap("Service=AWSECommerceService&Operation=ItemLookup&SubscriptionId=AKIAIWILA3BYHLAWG5AA&AssociateTag=icici123-20&ItemId="+itemASIN+"&IdType=ASIN&ResponseGroup=Images,ItemAttributes,Offers"));
        /*String encodedString=null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        try {
            String link="Service=AWSECommerceService&Operation=ItemSearch&SubscriptionId=AKIAIWILA3BYHLAWG5AA&AssociateTag=icici123-20&SearchIndex=All&Keywords=harry potter&ResponseGroup=ItemAttributes&Timestamp="+sdf.format(new Date());
            String[] keyValue=link.split("&");
            Arrays.sort(keyValue);
            link=new String();
            link=keyValue[0];
            for(int i=1;i<keyValue.length;i++) {
                Log.e("sorted array", keyValue[i]);
                link=link.concat("&"+keyValue[i]);
            }
            link="GET\nwebservices.amazon.com\n//onca//xml"+link;
            signature=getSignature("8zy0bs2N0FtTbDMDg3WNxCGN7Z5BvmKyMTi/MWfd",link);
            encodedString=URLEncoder.encode(signature,"UTF-8").replace("+","%20").replace("=","%3D");

            //ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
           // byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            //byteBuffer.put(link.getBytes());


        } catch (Exception e) {
            e.printStackTrace();
        }
*/

        //-prints-> 2015-01-22T03:23:26Z
        String response = WebService.getXML(signature);
        String xmlString;  // some XML String previously created
        XmlToJson xmlToJson = new XmlToJson.Builder(response).build();
        JSONObject jsonObject = xmlToJson.toJson();

        // convert to a Json String
        String jsonString = xmlToJson.toString();

        // convert to a formatted Json String
        String formatted = xmlToJson.toFormattedString();
        try {
            JsonElement jsonElement = new JsonParser().parse(jsonString);
            JsonObject jsonObject1 = jsonElement.getAsJsonObject();
            JsonObject jsonObject2 = jsonObject1.getAsJsonObject("ItemLookupResponse");
            JsonObject jsonObject3 = jsonObject2.getAsJsonObject("Items");
            JsonObject jsonArray = jsonObject3.getAsJsonObject("Item");


            Gson gson = new GsonBuilder().create();
            ItemLookUpDto item = gson.fromJson(jsonArray.toString(), new TypeToken<ItemLookUpDto>() {
            }.getType());
            eventResponse=new EventResponse((Object)item, EventNumbers.AMAZON_GET_ITEM);
        } catch (Exception e) {
            e.printStackTrace();
            eventResponse=new EventResponse((Object)null, EventNumbers.AMAZON_GET_ITEM);
        }
        return eventResponse;

    }



}
