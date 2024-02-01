package fr.cyr.marmite.app.service.mapper;

import fr.cyr.marmite.app.domain.Order;
import fr.cyr.marmite.app.domain.OrderFormulaItem;
import fr.cyr.marmite.app.service.dto.Order;
import fr.cyr.marmite.app.service.dto.OrderFormulaItem;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderFormulaItem} and its DTO {@link OrderFormulaItem}.
 */
@Mapper(componentModel = "spring")
public interface OrderFormulaItemMapper extends EntityMapper<OrderFormulaItem, OrderFormulaItem> {
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    OrderFormulaItem toDto(OrderFormulaItem s);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Order toDtoOrderId(Order order);
}
