package fr.cyr.marmite.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.cyr.marmite.app.domain.OrderProductItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderProductItem implements Serializable {

    private Long id;

    private Integer quantity;

    private ProductDTO product;

    private OrderDTO order;

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

    public OrderDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderProductItem)) {
            return false;
        }

        OrderProductItem orderProductItem = (OrderProductItem) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderProductItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderProductItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", product=" + getProduct() +
            ", order=" + getOrder() +
            "}";
    }
}
