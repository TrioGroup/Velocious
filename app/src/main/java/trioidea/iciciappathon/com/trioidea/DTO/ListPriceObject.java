package trioidea.iciciappathon.com.trioidea.DTO;

/**
 * Created by asus on 19/04/2017.
 */
public class ListPriceObject {
    String FormattedPrice;
    String CurrencyCode;
    String Amount;

    public String getFormattedPrice() {
        return FormattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        FormattedPrice = formattedPrice;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }
}
