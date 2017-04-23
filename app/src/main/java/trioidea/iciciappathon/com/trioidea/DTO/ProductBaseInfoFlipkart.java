package trioidea.iciciappathon.com.trioidea.DTO;

import java.util.List;

/**
 * Created by asus on 23/04/2017.
 */
public class ProductBaseInfoFlipkart {
    String productId;
    String title;
    String productDescription;
    String productBrand;
    FlipkartPriceDTO maximumRetailPrice;
    FlipkartPriceDTO flipkartSellingPrice;
    FlipkartPriceDTO flipkartSpecialPrice;
    String inStock;
    imageUrlDTO imageUrls;
    List<String> offers;


    public imageUrlDTO getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(imageUrlDTO imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public FlipkartPriceDTO getMaximumRetailPrice() {
        return maximumRetailPrice;
    }

    public void setMaximumRetailPrice(FlipkartPriceDTO maximumRetailPrice) {
        this.maximumRetailPrice = maximumRetailPrice;
    }

    public FlipkartPriceDTO getFlipkartSellingPrice() {
        return flipkartSellingPrice;
    }

    public void setFlipkartSellingPrice(FlipkartPriceDTO flipkartSellingPrice) {
        this.flipkartSellingPrice = flipkartSellingPrice;
    }

    public FlipkartPriceDTO getFlipkartSpecialPrice() {
        return flipkartSpecialPrice;
    }

    public void setFlipkartSpecialPrice(FlipkartPriceDTO flipkartSpecialPrice) {
        this.flipkartSpecialPrice = flipkartSpecialPrice;
    }

    public String getInStock() {
        return inStock;
    }

    public void setInStock(String inStock) {
        this.inStock = inStock;
    }

    public List<String> getOffers() {
        return offers;
    }

    public void setOffers(List<String> offers) {
        this.offers = offers;
    }

    public class imageUrlDTO {
        String unknown;

        public String getUnknown() {
            return unknown;
        }

        public void setUnknown(String unknown) {
            this.unknown = unknown;
        }
    }
}
