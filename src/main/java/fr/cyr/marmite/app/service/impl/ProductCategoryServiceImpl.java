package fr.cyr.marmite.app.service.impl;

import fr.cyr.marmite.app.domain.ProductCategory;
import fr.cyr.marmite.app.repository.ProductCategoryRepository;
import fr.cyr.marmite.app.service.ProductCategoryService;
import fr.cyr.marmite.app.service.dto.ProductCategory;
import fr.cyr.marmite.app.service.mapper.ProductCategoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductCategory}.
 */
@Service
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final Logger log = LoggerFactory.getLogger(ProductCategoryServiceImpl.class);

    private final ProductCategoryRepository productCategoryRepository;

    private final ProductCategoryMapper productCategoryMapper;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository, ProductCategoryMapper productCategoryMapper) {
        this.productCategoryRepository = productCategoryRepository;
        this.productCategoryMapper = productCategoryMapper;
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        log.debug("Request to save ProductCategory : {}", productCategory);
        ProductCategory productCategory = productCategoryMapper.toEntity(productCategory);
        productCategory = productCategoryRepository.save(productCategory);
        return productCategoryMapper.toDto(productCategory);
    }

    @Override
    public ProductCategory update(ProductCategory productCategory) {
        log.debug("Request to update ProductCategory : {}", productCategory);
        ProductCategory productCategory = productCategoryMapper.toEntity(productCategory);
        productCategory = productCategoryRepository.save(productCategory);
        return productCategoryMapper.toDto(productCategory);
    }

    @Override
    public Optional<ProductCategory> partialUpdate(ProductCategory productCategory) {
        log.debug("Request to partially update ProductCategory : {}", productCategory);

        return productCategoryRepository
            .findById(productCategory.getId())
            .map(existingProductCategory -> {
                productCategoryMapper.partialUpdate(existingProductCategory, productCategory);

                return existingProductCategory;
            })
            .map(productCategoryRepository::save)
            .map(productCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductCategory> findAll(Pageable pageable) {
        log.debug("Request to get all ProductCategories");
        return productCategoryRepository.findAll(pageable).map(productCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductCategory> findOne(Long id) {
        log.debug("Request to get ProductCategory : {}", id);
        return productCategoryRepository.findById(id).map(productCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductCategory : {}", id);
        productCategoryRepository.deleteById(id);
    }
}
