package fr.cyr.marmite.app.repository;

import fr.cyr.marmite.app.domain.OrdersFormulaItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrdersFormulaItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdersFormulaItemRepository extends JpaRepository<OrdersFormulaItem, Long> {}
