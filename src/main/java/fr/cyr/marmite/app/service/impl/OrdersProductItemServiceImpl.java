package fr.cyr.marmite.app.service.impl;

import fr.cyr.marmite.app.domain.OrdersProductItem;
import fr.cyr.marmite.app.repository.OrdersProductItemRepository;
import fr.cyr.marmite.app.service.OrdersProductItemService;
import fr.cyr.marmite.app.service.dto.OrdersProductItem;
import fr.cyr.marmite.app.service.mapper.OrdersProductItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdersProductItem}.
 */
@Service
@Transactional
public class OrdersProductItemServiceImpl implements OrdersProductItemService {

    private final Logger log = LoggerFactory.getLogger(OrdersProductItemServiceImpl.class);

    private final OrdersProductItemRepository ordersProductItemRepository;

    private final OrdersProductItemMapper ordersProductItemMapper;

    public OrdersProductItemServiceImpl(
        OrdersProductItemRepository ordersProductItemRepository,
        OrdersProductItemMapper ordersProductItemMapper
    ) {
        this.ordersProductItemRepository = ordersProductItemRepository;
        this.ordersProductItemMapper = ordersProductItemMapper;
    }

    @Override
    public OrdersProductItem save(OrdersProductItem ordersProductItem) {
        log.debug("Request to save OrdersProductItem : {}", ordersProductItem);
        OrdersProductItem ordersProductItem = ordersProductItemMapper.toEntity(ordersProductItem);
        ordersProductItem = ordersProductItemRepository.save(ordersProductItem);
        return ordersProductItemMapper.toDto(ordersProductItem);
    }

    @Override
    public OrdersProductItem update(OrdersProductItem ordersProductItem) {
        log.debug("Request to update OrdersProductItem : {}", ordersProductItem);
        OrdersProductItem ordersProductItem = ordersProductItemMapper.toEntity(ordersProductItem);
        ordersProductItem = ordersProductItemRepository.save(ordersProductItem);
        return ordersProductItemMapper.toDto(ordersProductItem);
    }

    @Override
    public Optional<OrdersProductItem> partialUpdate(OrdersProductItem ordersProductItem) {
        log.debug("Request to partially update OrdersProductItem : {}", ordersProductItem);

        return ordersProductItemRepository
            .findById(ordersProductItem.getId())
            .map(existingOrdersProductItem -> {
                ordersProductItemMapper.partialUpdate(existingOrdersProductItem, ordersProductItem);

                return existingOrdersProductItem;
            })
            .map(ordersProductItemRepository::save)
            .map(ordersProductItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrdersProductItem> findAll(Pageable pageable) {
        log.debug("Request to get all OrdersProductItems");
        return ordersProductItemRepository.findAll(pageable).map(ordersProductItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdersProductItem> findOne(Long id) {
        log.debug("Request to get OrdersProductItem : {}", id);
        return ordersProductItemRepository.findById(id).map(ordersProductItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdersProductItem : {}", id);
        ordersProductItemRepository.deleteById(id);
    }
}
