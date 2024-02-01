package fr.cyr.marmite.app.service;

import fr.cyr.marmite.app.service.dto.OrdersFormulaItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.cyr.marmite.app.domain.OrdersFormulaItem}.
 */
public interface OrdersFormulaItemService {
    /**
     * Save a ordersFormulaItem.
     *
     * @param ordersFormulaItem the entity to save.
     * @return the persisted entity.
     */
    OrdersFormulaItem save(OrdersFormulaItem ordersFormulaItem);

    /**
     * Updates a ordersFormulaItem.
     *
     * @param ordersFormulaItem the entity to update.
     * @return the persisted entity.
     */
    OrdersFormulaItem update(OrdersFormulaItem ordersFormulaItem);

    /**
     * Partially updates a ordersFormulaItem.
     *
     * @param ordersFormulaItem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrdersFormulaItem> partialUpdate(OrdersFormulaItem ordersFormulaItem);

    /**
     * Get all the ordersFormulaItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrdersFormulaItem> findAll(Pageable pageable);

    /**
     * Get the "id" ordersFormulaItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrdersFormulaItem> findOne(Long id);

    /**
     * Delete the "id" ordersFormulaItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
