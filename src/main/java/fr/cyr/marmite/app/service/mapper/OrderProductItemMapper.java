package fr.cyr.marmite.app.service.mapper;

import fr.cyr.marmite.app.domain.Order;
import fr.cyr.marmite.app.domain.OrderProductItem;
import fr.cyr.marmite.app.domain.Product;
import fr.cyr.marmite.app.service.dto.Order;
import fr.cyr.marmite.app.service.dto.OrderProductItem;
import fr.cyr.marmite.app.service.dto.Product;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderProductItem} and its DTO {@link OrderProductItem}.
 */
@Mapper(componentModel = "spring")
public interface OrderProductItemMapper extends EntityMapper<OrderProductItem, OrderProductItem> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    @Mapping(target = "order", source = "order", qualifiedByName = "orderId")
    OrderProductItem toDto(OrderProductItem s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Product toDtoProductId(Product product);

    @Named("orderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Order toDtoOrderId(Order order);
}
