package trioidea.iciciappathon.com.trioidea.DTO;

/**
 * Created by asus on 21/04/2017.
 */
public class ItemLookUpDto {
    String ASIN;
    ImageSetsDTO ImageSets;
    AttributeOfItemLookUp ItemAttributes;

    public ImageSetsDTO getImageSets() {
        return ImageSets;
    }

    public void setImageSets(ImageSetsDTO imageSets) {
        ImageSets = imageSets;
    }

    public String getASIN() {
        return ASIN;
    }

    public void setASIN(String ASIN) {
        this.ASIN = ASIN;
    }

    public AttributeOfItemLookUp getItemAttributes() {
        return ItemAttributes;
    }

    public void setItemAttributes(AttributeOfItemLookUp itemAttributes) {
        ItemAttributes = itemAttributes;
    }
}
