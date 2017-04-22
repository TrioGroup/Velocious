package trioidea.iciciappathon.com.trioidea.DTO;

import java.util.List;

/**
 * Created by asus on 21/04/2017.
 */
public class AttributeOfItemLookUp {
    String Title;
    String ProductGroup;
    String Publisher;
    ListPriceObject ListPrice;
    List<FeaturesDTO> Feature;
    String Warranty;


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public ListPriceObject getListPrice() {
        return ListPrice;
    }

    public void setListPrice(ListPriceObject listPrice) {
        ListPrice = listPrice;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getProductGroup() {
        return ProductGroup;
    }

    public void setProductGroup(String productGroup) {
        ProductGroup = productGroup;
    }


    public String getWarranty() {
        return Warranty;
    }

    public void setWarranty(String warranty) {
        Warranty = warranty;
    }

    public List<FeaturesDTO> getFeature() {
        return Feature;
    }

    public void setFeature(List<FeaturesDTO> feature) {
        Feature = feature;
    }
}
