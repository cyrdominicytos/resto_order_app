package fr.cyr.marmite.app.service;

import fr.cyr.marmite.app.service.dto.OrderFormulaItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.cyr.marmite.app.domain.OrderFormulaItem}.
 */
public interface OrderFormulaItemService {
    /**
     * Save a orderFormulaItem.
     *
     * @param orderFormulaItem the entity to save.
     * @return the persisted entity.
     */
    OrderFormulaItem save(OrderFormulaItem orderFormulaItem);

    /**
     * Updates a orderFormulaItem.
     *
     * @param orderFormulaItem the entity to update.
     * @return the persisted entity.
     */
    OrderFormulaItem update(OrderFormulaItem orderFormulaItem);

    /**
     * Partially updates a orderFormulaItem.
     *
     * @param orderFormulaItem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderFormulaItem> partialUpdate(OrderFormulaItem orderFormulaItem);

    /**
     * Get all the orderFormulaItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderFormulaItem> findAll(Pageable pageable);

    /**
     * Get the "id" orderFormulaItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderFormulaItem> findOne(Long id);

    /**
     * Delete the "id" orderFormulaItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
