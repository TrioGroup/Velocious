package trioidea.iciciappathon.com.trioidea.DTO;

import android.widget.ListView;

import java.util.List;

/**
 * Created by asus on 19/04/2017.
 */
public class AttributeClass {
    String Title;
    String ProductGroup;
    String Publisher;
    ListPriceObject ListPrice;


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
}
