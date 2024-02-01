package fr.cyr.marmite.app.service.mapper;

import fr.cyr.marmite.app.domain.Formula;
import fr.cyr.marmite.app.domain.Product;
import fr.cyr.marmite.app.domain.ProductCategory;
import fr.cyr.marmite.app.service.dto.Formula;
import fr.cyr.marmite.app.service.dto.Product;
import fr.cyr.marmite.app.service.dto.ProductCategory;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link Product}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<Product, Product> {
    @Mapping(target = "category", source = "category", qualifiedByName = "productCategoryId")
    @Mapping(target = "formula", source = "formula", qualifiedByName = "formulaId")
    Product toDto(Product s);

    @Named("productCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductCategory toDtoProductCategoryId(ProductCategory productCategory);

    @Named("formulaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    Formula toDtoFormulaId(Formula formula);
}
