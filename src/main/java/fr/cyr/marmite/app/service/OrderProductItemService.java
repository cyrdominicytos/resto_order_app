package fr.cyr.marmite.app.service;

import fr.cyr.marmite.app.service.dto.OrderProductItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.cyr.marmite.app.domain.OrderProductItem}.
 */
public interface OrderProductItemService {
    /**
     * Save a orderProductItem.
     *
     * @param orderProductItem the entity to save.
     * @return the persisted entity.
     */
    OrderProductItem save(OrderProductItem orderProductItem);

    /**
     * Updates a orderProductItem.
     *
     * @param orderProductItem the entity to update.
     * @return the persisted entity.
     */
    OrderProductItem update(OrderProductItem orderProductItem);

    /**
     * Partially updates a orderProductItem.
     *
     * @param orderProductItem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OrderProductItem> partialUpdate(OrderProductItem orderProductItem);

    /**
     * Get all the orderProductItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OrderProductItem> findAll(Pageable pageable);

    /**
     * Get the "id" orderProductItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OrderProductItem> findOne(Long id);

    /**
     * Delete the "id" orderProductItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
