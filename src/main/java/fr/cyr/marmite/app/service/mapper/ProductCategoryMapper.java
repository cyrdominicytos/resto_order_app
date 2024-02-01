package fr.cyr.marmite.app.service.mapper;

import fr.cyr.marmite.app.domain.ProductCategory;
import fr.cyr.marmite.app.service.dto.ProductCategory;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductCategory} and its DTO {@link ProductCategory}.
 */
@Mapper(componentModel = "spring")
public interface ProductCategoryMapper extends EntityMapper<ProductCategory, ProductCategory> {}
