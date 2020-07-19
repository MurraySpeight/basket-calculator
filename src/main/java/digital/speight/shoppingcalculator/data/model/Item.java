package digital.speight.shoppingcalculator.data.model;

import java.util.Objects;

public class Item {

    private String id;
    private Long price;
    private Integer discount;
    private MultiBuyDiscount multiBuyDiscount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public MultiBuyDiscount getMultiBuyDiscount() {
        return multiBuyDiscount;
    }

    public void setMultiBuyDiscount(MultiBuyDiscount multiBuyDiscount) {
        this.multiBuyDiscount = multiBuyDiscount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var item = (Item) o;
        return id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", multiBuyDiscount=" + multiBuyDiscount +
                '}';
    }

}
