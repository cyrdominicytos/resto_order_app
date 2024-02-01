package fr.cyr.marmite.app.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fr.cyr.marmite.app.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private Long id;

    @NotNull
    private Double price;

    @NotNull
    private String name;

    @Lob
    private byte[] photo;

    private String photoContentType;
    private String imageType;

    private String description;

    private Boolean isSupplement;

    private Instant createdDate;

    private Instant updatedDate;

    private ProductCategoryDTO category;

    private FormulaDTO formula;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsSupplement() {
        return isSupplement;
    }

    public void setIsSupplement(Boolean isSupplement) {
        this.isSupplement = isSupplement;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public ProductCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ProductCategoryDTO category) {
        this.category = category;
    }

    public FormulaDTO getFormula() {
        return formula;
    }

    public void setFormula(FormulaDTO formula) {
        this.formula = formula;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }

        Product product = (Product) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", name='" + getName() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", imageType='" + getImageType() + "'" +
            ", description='" + getDescription() + "'" +
            ", isSupplement='" + getIsSupplement() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            ", category=" + getCategory() +
            ", formula=" + getFormula() +
            "}";
    }
}
