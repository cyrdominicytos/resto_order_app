package fr.cyr.marmite.app.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.cyr.marmite.app.domain.ProductCategory} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCategory implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Integer order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductCategory)) {
            return false;
        }

        ProductCategory productCategory = (ProductCategory) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productCategory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", order=" + getOrder() +
            "}";
    }
}
