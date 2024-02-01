package fr.cyr.marmite.app.service.mapper;

import fr.cyr.marmite.app.domain.Orders;
import fr.cyr.marmite.app.domain.OrdersFormulaItem;
import fr.cyr.marmite.app.service.dto.Orders;
import fr.cyr.marmite.app.service.dto.OrdersFormulaItem;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrdersFormulaItem} and its DTO {@link OrdersFormulaItem}.
 */
@Mapper(componentModel = "spring")
public interface OrdersFormulaItemMapper extends EntityMapper<OrdersFormulaItem, OrdersFormulaItem> {
    @Mapping(target = "orders", source = "orders", qualifiedByName = "ordersId")
    OrdersFormulaItem toDto(OrdersFormulaItem s);

    @Named("ordersId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Orders toDtoOrdersId(Orders orders);
}
