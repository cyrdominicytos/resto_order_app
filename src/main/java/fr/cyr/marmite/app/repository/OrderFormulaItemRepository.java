package fr.cyr.marmite.app.repository;

import fr.cyr.marmite.app.domain.OrderFormulaItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderFormulaItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderFormulaItemRepository extends JpaRepository<OrderFormulaItem, Long> {}
