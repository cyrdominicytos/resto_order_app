package fr.cyr.marmite.app.repository;

import fr.cyr.marmite.app.domain.Orders;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Orders entity.
 *
 * When extending this class, extend OrdersRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface OrdersRepository extends OrdersRepositoryWithBagRelationships, JpaRepository<Orders, Long> {
    @Query("select orders from Orders orders where orders.user.login = ?#{principal.username}")
    List<Orders> findByUserIsCurrentUser();

    default Optional<Orders> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Orders> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Orders> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
