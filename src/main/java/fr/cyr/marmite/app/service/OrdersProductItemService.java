package fr.cyr.marmite.app.service;

import fr.cyr.marmite.app.service.dto.OrdersProductItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.cyr.marmite.app.domain.OrdersProductItem}.
 */
public interface OrdersProductItemService {
    /**
     * Save a ordersProductItem.
     *
     * @param ordersProductItem the entity to save.
     * @return the persisted entity.
     */
    OrdersProductItem save(OrdersProductItem ordersProductItem);

    /**
     * Updates a ordersProductItem.
     *
     * @param ordersProductItem the entity to update.
     * @return the persisted entity.
     */
    OrdersProductItem update(OrdersProductItem ordersProductItem);

    /**
     * Partially updates a ordersProductItem.
     *
     * @param ordersProductItem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdersProductItem> partialUpdate(OrdersProductItem ordersProductItem);

    /**
     * Get all the ordersProductItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrdersProductItem> findAll(Pageable pageable);

    /**
     * Get the "id" ordersProductItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdersProductItem> findOne(Long id);

    /**
     * Delete the "id" ordersProductItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
