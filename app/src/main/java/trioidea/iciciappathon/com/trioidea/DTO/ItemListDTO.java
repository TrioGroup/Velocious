package trioidea.iciciappathon.com.trioidea.DTO;

import android.graphics.Paint;

import java.util.ArrayList;

import trioidea.iciciappathon.com.trioidea.R;

/**
 * Created by asus on 23/04/2017.
 */
public class ItemListDTO {
    String Id;
    String title;
    String publisher;
    String price;
    String offerPrice;
    String imageUrl;
    String site;
    String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public ItemListDTO() {
    }

    public ItemListDTO(ArrayList<FlipkartProductInfoList> flipkartProductInfoLists) {

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }
}