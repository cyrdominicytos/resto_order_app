package fr.cyr.marmite.app.repository;

import fr.cyr.marmite.app.domain.Formula;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Formula entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormulaRepository extends JpaRepository<Formula, Long> {}
