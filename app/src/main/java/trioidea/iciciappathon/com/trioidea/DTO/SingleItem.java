package trioidea.iciciappathon.com.trioidea.DTO;

/**
 * Created by asus on 19/04/2017.
 */
public class SingleItem {
    String ASIN;
    ImageDto MediumImage;
    AttributeClass ItemAttributes;

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
