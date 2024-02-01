package fr.cyr.marmite.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.cyr.marmite.app.domain.OrdersProductItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrdersProductItem implements Serializable {

    private Long id;

    private Integer quantity;

    private ProductDTO product;

    private OrdersDTO orders;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public OrdersDTO getOrders() {
        return orders;
    }

    public void setOrders(OrdersDTO orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrdersProductItem)) {
            return false;
        }

        OrdersProductItem ordersProductItem = (OrdersProductItem) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ordersProductItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdersProductItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", product=" + getProduct() +
            ", orders=" + getOrders() +
            "}";
    }
}
