package fr.cyr.marmite.app.service.impl;

import fr.cyr.marmite.app.domain.OrdersFormulaItem;
import fr.cyr.marmite.app.repository.OrdersFormulaItemRepository;
import fr.cyr.marmite.app.service.OrdersFormulaItemService;
import fr.cyr.marmite.app.service.dto.OrdersFormulaItem;
import fr.cyr.marmite.app.service.mapper.OrdersFormulaItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrdersFormulaItem}.
 */
@Service
@Transactional
public class OrdersFormulaItemServiceImpl implements OrdersFormulaItemService {

    private final Logger log = LoggerFactory.getLogger(OrdersFormulaItemServiceImpl.class);

    private final OrdersFormulaItemRepository ordersFormulaItemRepository;

    private final OrdersFormulaItemMapper ordersFormulaItemMapper;

    public OrdersFormulaItemServiceImpl(
        OrdersFormulaItemRepository ordersFormulaItemRepository,
        OrdersFormulaItemMapper ordersFormulaItemMapper
    ) {
        this.ordersFormulaItemRepository = ordersFormulaItemRepository;
        this.ordersFormulaItemMapper = ordersFormulaItemMapper;
    }

    @Override
    public OrdersFormulaItem save(OrdersFormulaItem ordersFormulaItem) {
        log.debug("Request to save OrdersFormulaItem : {}", ordersFormulaItem);
        OrdersFormulaItem ordersFormulaItem = ordersFormulaItemMapper.toEntity(ordersFormulaItem);
        ordersFormulaItem = ordersFormulaItemRepository.save(ordersFormulaItem);
        return ordersFormulaItemMapper.toDto(ordersFormulaItem);
    }

    @Override
    public OrdersFormulaItem update(OrdersFormulaItem ordersFormulaItem) {
        log.debug("Request to update OrdersFormulaItem : {}", ordersFormulaItem);
        OrdersFormulaItem ordersFormulaItem = ordersFormulaItemMapper.toEntity(ordersFormulaItem);
        ordersFormulaItem = ordersFormulaItemRepository.save(ordersFormulaItem);
        return ordersFormulaItemMapper.toDto(ordersFormulaItem);
    }

    @Override
    public Optional<OrdersFormulaItem> partialUpdate(OrdersFormulaItem ordersFormulaItem) {
        log.debug("Request to partially update OrdersFormulaItem : {}", ordersFormulaItem);

        return ordersFormulaItemRepository
            .findById(ordersFormulaItem.getId())
            .map(existingOrdersFormulaItem -> {
                ordersFormulaItemMapper.partialUpdate(existingOrdersFormulaItem, ordersFormulaItem);

                return existingOrdersFormulaItem;
            })
            .map(ordersFormulaItemRepository::save)
            .map(ordersFormulaItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrdersFormulaItem> findAll(Pageable pageable) {
        log.debug("Request to get all OrdersFormulaItems");
        return ordersFormulaItemRepository.findAll(pageable).map(ordersFormulaItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdersFormulaItem> findOne(Long id) {
        log.debug("Request to get OrdersFormulaItem : {}", id);
        return ordersFormulaItemRepository.findById(id).map(ordersFormulaItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrdersFormulaItem : {}", id);
        ordersFormulaItemRepository.deleteById(id);
    }
}
