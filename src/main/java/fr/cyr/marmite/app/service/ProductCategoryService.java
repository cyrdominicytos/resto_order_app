package fr.cyr.marmite.app.service;

import fr.cyr.marmite.app.service.dto.ProductCategory;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.cyr.marmite.app.domain.ProductCategory}.
 */
public interface ProductCategoryService {
    /**
     * Save a productCategory.
     *
     * @param productCategory the entity to save.
     * @return the persisted entity.
     */
    ProductCategory save(ProductCategory productCategory);

    /**
     * Updates a productCategory.
     *
     * @param productCategory the entity to update.
     * @return the persisted entity.
     */
    ProductCategory update(ProductCategory productCategory);

    /**
     * Partially updates a productCategory.
     *
     * @param productCategory the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductCategory> partialUpdate(ProductCategory productCategory);

    /**
     * Get all the productCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductCategory> findAll(Pageable pageable);

    /**
     * Get the "id" productCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductCategory> findOne(Long id);

    /**
     * Delete the "id" productCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
