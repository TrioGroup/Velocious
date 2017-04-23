package trioidea.iciciappathon.com.trioidea.DTO;

/**
 * Created by asus on 23/04/2017.
 */
public class FlipkartProductInfoList {
    ProductBaseInfoFlipkart productBaseInfoV1;
    ProductCategorySpecificInfo categorySpecificInfoV1;

    public ProductCategorySpecificInfo getCategorySpecificInfoV1() {
        return categorySpecificInfoV1;
    }

    public void setCategorySpecificInfoV1(ProductCategorySpecificInfo categorySpecificInfoV1) {
        this.categorySpecificInfoV1 = categorySpecificInfoV1;
    }

    public ProductBaseInfoFlipkart getProductBaseInfoV1() {
        return productBaseInfoV1;
    }

    public void setProductBaseInfoV1(ProductBaseInfoFlipkart productBaseInfoV1) {
        this.productBaseInfoV1 = productBaseInfoV1;
    }

}
