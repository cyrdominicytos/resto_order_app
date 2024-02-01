package fr.cyr.marmite.app.repository;

import fr.cyr.marmite.app.domain.OrdersProductItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrdersProductItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrdersProductItemRepository extends JpaRepository<OrdersProductItem, Long> {}
