package trioidea.iciciappathon.com.trioidea.DTO;

import java.io.Serializable;

/**
 * Created by asus on 19/04/2017.
 */
public class SingleItem implements Serializable {
    String ASIN;
    ImageDto MediumImage;
    AttributeClass ItemAttributes;
    OffersDto Offers;

    public OffersDto getOfferSummary() {
        return Offers;
    }

    public void setOfferSummary(OffersDto offerSummary) {
        Offers = offerSummary;
    }

    public ImageDto getMediumImage() {
        return MediumImage;
    }

    public void setMediumImage(ImageDto mediumImage) {
        MediumImage = mediumImage;
    }

    public String getASIN() {
        return ASIN;
    }

    public void setASIN(String ASIN) {
        this.ASIN = ASIN;
    }

    public AttributeClass getItemAttributes() {
        return ItemAttributes;
    }

    public void setItemAttributes(AttributeClass itemAttributes) {
        ItemAttributes = itemAttributes;
    }
}
