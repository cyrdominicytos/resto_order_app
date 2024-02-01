package fr.cyr.marmite.app.service;

import fr.cyr.marmite.app.service.dto.Formula;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.cyr.marmite.app.domain.Formula}.
 */
public interface FormulaService {
    /**
     * Save a formula.
     *
     * @param formula the entity to save.
     * @return the persisted entity.
     */
    Formula save(Formula formula);

    /**
     * Updates a formula.
     *
     * @param formula the entity to update.
     * @return the persisted entity.
     */
    Formula update(Formula formula);

    /**
     * Partially updates a formula.
     *
     * @param formula the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Formula> partialUpdate(Formula formula);

    /**
     * Get all the formulas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Formula> findAll(Pageable pageable);

    /**
     * Get the "id" formula.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Formula> findOne(Long id);

    /**
     * Delete the "id" formula.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
