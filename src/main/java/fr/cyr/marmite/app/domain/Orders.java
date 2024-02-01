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
 * A Orders.
 */
@Entity
@Table(name = "orders")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Orders implements Serializable {

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

    @OneToMany(mappedBy = "orders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "orders" }, allowSetters = true)
    private Set<OrdersFormulaItem> ordersFormulaItems = new HashSet<>();

    @OneToMany(mappedBy = "orders")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "orders" }, allowSetters = true)
    private Set<OrdersProductItem> ordersProductItems = new HashSet<>();

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(
        name = "rel_orders__products",
        joinColumns = @JoinColumn(name = "orders_id"),
        inverseJoinColumns = @JoinColumn(name = "products_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "formula", "ordersses" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_orders__formulas",
        joinColumns = @JoinColumn(name = "orders_id"),
        inverseJoinColumns = @JoinColumn(name = "formulas_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products", "ordersses" }, allowSetters = true)
    private Set<Formula> formulas = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Orders id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Orders amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Orders createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getRecoveryDate() {
        return this.recoveryDate;
    }

    public Orders recoveryDate(Instant recoveryDate) {
        this.setRecoveryDate(recoveryDate);
        return this;
    }

    public void setRecoveryDate(Instant recoveryDate) {
        this.recoveryDate = recoveryDate;
    }

    public Instant getUpdatedDate() {
        return this.updatedDate;
    }

    public Orders updatedDate(Instant updatedDate) {
        this.setUpdatedDate(updatedDate);
        return this;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Set<OrdersFormulaItem> getOrdersFormulaItems() {
        return this.ordersFormulaItems;
    }

    public void setOrdersFormulaItems(Set<OrdersFormulaItem> ordersFormulaItems) {
        if (this.ordersFormulaItems != null) {
            this.ordersFormulaItems.forEach(i -> i.setOrders(null));
        }
        if (ordersFormulaItems != null) {
            ordersFormulaItems.forEach(i -> i.setOrders(this));
        }
        this.ordersFormulaItems = ordersFormulaItems;
    }

    public Orders ordersFormulaItems(Set<OrdersFormulaItem> ordersFormulaItems) {
        this.setOrdersFormulaItems(ordersFormulaItems);
        return this;
    }

    public Orders addOrdersFormulaItems(OrdersFormulaItem ordersFormulaItem) {
        this.ordersFormulaItems.add(ordersFormulaItem);
        ordersFormulaItem.setOrders(this);
        return this;
    }

    public Orders removeOrdersFormulaItems(OrdersFormulaItem ordersFormulaItem) {
        this.ordersFormulaItems.remove(ordersFormulaItem);
        ordersFormulaItem.setOrders(null);
        return this;
    }

    public Set<OrdersProductItem> getOrdersProductItems() {
        return this.ordersProductItems;
    }

    public void setOrdersProductItems(Set<OrdersProductItem> ordersProductItems) {
        if (this.ordersProductItems != null) {
            this.ordersProductItems.forEach(i -> i.setOrders(null));
        }
        if (ordersProductItems != null) {
            ordersProductItems.forEach(i -> i.setOrders(this));
        }
        this.ordersProductItems = ordersProductItems;
    }

    public Orders ordersProductItems(Set<OrdersProductItem> ordersProductItems) {
        this.setOrdersProductItems(ordersProductItems);
        return this;
    }

    public Orders addOrdersProductItems(OrdersProductItem ordersProductItem) {
        this.ordersProductItems.add(ordersProductItem);
        ordersProductItem.setOrders(this);
        return this;
    }

    public Orders removeOrdersProductItems(OrdersProductItem ordersProductItem) {
        this.ordersProductItems.remove(ordersProductItem);
        ordersProductItem.setOrders(null);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Orders user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Orders products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Orders addProducts(Product product) {
        this.products.add(product);
        product.getOrdersses().add(this);
        return this;
    }

    public Orders removeProducts(Product product) {
        this.products.remove(product);
        product.getOrdersses().remove(this);
        return this;
    }

    public Set<Formula> getFormulas() {
        return this.formulas;
    }

    public void setFormulas(Set<Formula> formulas) {
        this.formulas = formulas;
    }

    public Orders formulas(Set<Formula> formulas) {
        this.setFormulas(formulas);
        return this;
    }

    public Orders addFormulas(Formula formula) {
        this.formulas.add(formula);
        formula.getOrdersses().add(this);
        return this;
    }

    public Orders removeFormulas(Formula formula) {
        this.formulas.remove(formula);
        formula.getOrdersses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Orders)) {
            return false;
        }
        return id != null && id.equals(((Orders) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Orders{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", recoveryDate='" + getRecoveryDate() + "'" +
            ", updatedDate='" + getUpdatedDate() + "'" +
            "}";
    }
}
