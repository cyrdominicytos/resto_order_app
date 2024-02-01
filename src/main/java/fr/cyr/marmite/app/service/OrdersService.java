package fr.cyr.marmite.app.service;

import fr.cyr.marmite.app.service.dto.Orders;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.cyr.marmite.app.domain.Orders}.
 */
public interface OrdersService {
    /**
     * Save a orders.
     *
     * @param orders the entity to save.
     * @return the persisted entity.
     */
    Orders save(Orders orders);

    /**
     * Updates a orders.
     *
     * @param orders the entity to update.
     * @return the persisted entity.
     */
    Orders update(Orders orders);

    /**
     * Partially updates a orders.
     *
     * @param orders the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Orders> partialUpdate(Orders orders);

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Orders> findAll(Pageable pageable);

    /**
     * Get all the orders with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Orders> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" orders.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Orders> findOne(Long id);

    /**
     * Delete the "id" orders.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
