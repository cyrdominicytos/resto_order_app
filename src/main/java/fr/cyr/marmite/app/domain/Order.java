package fr.cyr.marmite.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "recovery_date")
    private Instant recoveryDate;

    @Column(name = "updated_date")
    private Instant updatedDate;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(
        name = "rel_jhi_order__products",
        joinColumns = @JoinColumn(name = "jhi_order_id"),
        inverseJoinColumns = @JoinColumn(name = "products_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "formula", "orders" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_jhi_order__formulas",
        joinColumns = @JoinColumn(name = "jhi_order_id"),
        inverseJoinColumns = @JoinColumn(name = "formulas_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products", "orders" }, allowSetters = true)
    private Set<Formula> formulas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Order amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Order createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getRecoveryDate() {
        return this.recoveryDate;
    }

    public Order recoveryDate(Instant recoveryDate) {
        this.setRecoveryDate(recoveryDate);
        return this;
    }

    public void setRecoveryDate(Instant recoveryDate) {
        this.recoveryDate = recoveryDate;
    }

    public Instant getUpdatedDate() {
        return this.updatedDate;
    }

    public Order updatedDate(Instant updatedDate) {
        this.setUpdatedDate(updatedDate);
        return this;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Order user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Order products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Order addProducts(Product product) {
        this.products.add(product);
        product.getOrders().add(this);
        return this;
    }

    public Order removeProducts(Product product) {
        this.products.remove(product);
        product.getOrders().remove(this);
        return this;
    }

    public Set<Formula> getFormulas() {
        return this.formulas;
    }

    public void setFormulas(Set<Formula> formulas) {
        this.formulas = formulas;
    }

    public Order formulas(Set<Formula> formulas) {
        this.setFormulas(formulas);
        return this;
    }

    public Order addFormulas(Formula formula) {
        this.formulas.add(formula);
        formula.getOrders().add(this);
        return this;
    }

    public Order removeFormulas(Formula formula) {
        this.formulas.remove(formula);
        formula.getOrders().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
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
            "}";
    }
}
