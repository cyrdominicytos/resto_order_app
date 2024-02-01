package fr.cyr.marmite.app.service.mapper;

import fr.cyr.marmite.app.domain.Formula;
import fr.cyr.marmite.app.domain.Orders;
import fr.cyr.marmite.app.domain.Product;
import fr.cyr.marmite.app.domain.User;
import fr.cyr.marmite.app.service.dto.Formula;
import fr.cyr.marmite.app.service.dto.Orders;
import fr.cyr.marmite.app.service.dto.Product;
import fr.cyr.marmite.app.service.dto.User;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Orders} and its DTO {@link Orders}.
 */
@Mapper(componentModel = "spring")
public interface OrdersMapper extends EntityMapper<Orders, Orders> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "products", source = "products", qualifiedByName = "productIdSet")
    @Mapping(target = "formulas", source = "formulas", qualifiedByName = "formulaIdSet")
    Orders toDto(Orders s);

    @Mapping(target = "removeProducts", ignore = true)
    @Mapping(target = "removeFormulas", ignore = true)
    Orders toEntity(Orders orders);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    User toDtoUserId(User user);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Product toDtoProductId(Product product);

    @Named("productIdSet")
    default Set<Product> toDtoProductIdSet(Set<Product> product) {
        return product.stream().map(this::toDtoProductId).collect(Collectors.toSet());
    }

    @Named("formulaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Formula toDtoFormulaId(Formula formula);

    @Named("formulaIdSet")
    default Set<Formula> toDtoFormulaIdSet(Set<Formula> formula) {
        return formula.stream().map(this::toDtoFormulaId).collect(Collectors.toSet());
    }
}
