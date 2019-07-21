package digital.speight.shoppingcalculator.data.model;

public class MultiBuyDiscount {

    private int redemptionQuantity;
    private String discountId;
    private int discount;

    public int getRedemptionQuantity() {
        return redemptionQuantity;
    }

    public void setRedemptionQuantity(int redemptionQuantity) {
        this.redemptionQuantity = redemptionQuantity;
    }

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "MultiBuyDiscount{" +
                "redemptionQuantity=" + redemptionQuantity +
                ", discountId='" + discountId + '\'' +
                ", discount=" + discount +
                '}';
    }

}
