package fr.cyr.marmite.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.cyr.marmite.app.domain.Order} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private Long id;

    @NotNull
    private Double amount;

    private Instant createdDate;

    private Instant recoveryDate;

    private Instant updatedDate;

    private UserDTO user;

    private Set<ProductDTO> products = new HashSet<>();

    private Set<FormulaDTO> formulas = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getRecoveryDate() {
        return recoveryDate;
    }

    public void setRecoveryDate(Instant recoveryDate) {
        this.recoveryDate = recoveryDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Set<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductDTO> products) {
        this.products = products;
    }

    public Set<FormulaDTO> getFormulas() {
        return formulas;
    }

    public void setFormulas(Set<FormulaDTO> formulas) {
        this.formulas = formulas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }

        Order order = (Order) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", recoveryDate='" + getRecoveryDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            ", user=" + getUser() +
            ", products=" + getProducts() +
            ", formulas=" + getFormulas() +
            "}";
    }
}
