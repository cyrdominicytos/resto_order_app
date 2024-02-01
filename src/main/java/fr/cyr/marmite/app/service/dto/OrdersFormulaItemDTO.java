package fr.cyr.marmite.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.cyr.marmite.app.domain.OrdersFormulaItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrdersFormulaItem implements Serializable {

    private Long id;

    private Integer quantity;

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
        if (!(o instanceof OrdersFormulaItem)) {
            return false;
        }

        OrdersFormulaItem ordersFormulaItem = (OrdersFormulaItem) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ordersFormulaItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdersFormulaItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", orders=" + getOrders() +
            "}";
    }
}
