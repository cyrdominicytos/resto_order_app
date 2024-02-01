package fr.cyr.marmite.app.repository;

import fr.cyr.marmite.app.domain.OrderProductItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OrderProductItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderProductItemRepository extends JpaRepository<OrderProductItem, Long> {}
