package fr.cyr.marmite.app.service.mapper;

import fr.cyr.marmite.app.domain.Orders;
import fr.cyr.marmite.app.domain.OrdersProductItem;
import fr.cyr.marmite.app.domain.Product;
import fr.cyr.marmite.app.service.dto.Orders;
import fr.cyr.marmite.app.service.dto.OrdersProductItem;
import fr.cyr.marmite.app.service.dto.Product;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrdersProductItem} and its DTO {@link OrdersProductItem}.
 */
@Mapper(componentModel = "spring")
public interface OrdersProductItemMapper extends EntityMapper<OrdersProductItem, OrdersProductItem> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "orders", source = "orders", qualifiedByName = "ordersId")
    OrdersProductItem toDto(OrdersProductItem s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Product toDtoProductId(Product product);

    @Named("ordersId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Orders toDtoOrdersId(Orders orders);
}
