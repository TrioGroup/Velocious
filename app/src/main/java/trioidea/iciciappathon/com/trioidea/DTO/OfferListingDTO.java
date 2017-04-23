package trioidea.iciciappathon.com.trioidea.DTO;

/**
 * Created by asus on 22/04/2017.
 */
public class OfferListingDTO {
    String OfferListingId;
    ListPriceObject Price;
    ListPriceObject SalePrice;
    ListPriceObject AmountSaved;
    String PercentageSaved;
    String Availability;
    AvailabilityDTO AvailabilityAttributes;

    public String getOfferListingId() {
        return OfferListingId;
    }

    public void setOfferListingId(String offerListingId) {
        OfferListingId = offerListingId;
    }

    public ListPriceObject getPrice() {
        return Price;
    }

    public void setPrice(ListPriceObject price) {
        Price = price;
    }

    public ListPriceObject getSalePrice() {
        return SalePrice;
    }

    public void setSalePrice(ListPriceObject salePrice) {
        SalePrice = salePrice;
    }

    public ListPriceObject getAmountSaved() {
        return AmountSaved;
    }

    public void setAmountSaved(ListPriceObject amountSaved) {
        AmountSaved = amountSaved;
    }

    public String getPercentageSaved() {
        return PercentageSaved;
    }

    public void setPercentageSaved(String percentageSaved) {
        PercentageSaved = percentageSaved;
    }

    public String getAvailability() {
        return Availability;
    }

    public void setAvailability(String availability) {
        Availability = availability;
    }

    public AvailabilityDTO getAvailabilityAttributes() {
        return AvailabilityAttributes;
    }

    public void setAvailabilityAttributes(AvailabilityDTO availabilityAttributes) {
        AvailabilityAttributes = availabilityAttributes;
    }
}

