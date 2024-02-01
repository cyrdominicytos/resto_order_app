package fr.cyr.marmite.app.service.impl;

import fr.cyr.marmite.app.domain.OrderFormulaItem;
import fr.cyr.marmite.app.repository.OrderFormulaItemRepository;
import fr.cyr.marmite.app.service.OrderFormulaItemService;
import fr.cyr.marmite.app.service.dto.OrderFormulaItem;
import fr.cyr.marmite.app.service.mapper.OrderFormulaItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderFormulaItem}.
 */
@Service
@Transactional
public class OrderFormulaItemServiceImpl implements OrderFormulaItemService {

    private final Logger log = LoggerFactory.getLogger(OrderFormulaItemServiceImpl.class);

    private final OrderFormulaItemRepository orderFormulaItemRepository;

    private final OrderFormulaItemMapper orderFormulaItemMapper;

    public OrderFormulaItemServiceImpl(
        OrderFormulaItemRepository orderFormulaItemRepository,
        OrderFormulaItemMapper orderFormulaItemMapper
    ) {
        this.orderFormulaItemRepository = orderFormulaItemRepository;
        this.orderFormulaItemMapper = orderFormulaItemMapper;
    }

    @Override
    public OrderFormulaItem save(OrderFormulaItem orderFormulaItem) {
        log.debug("Request to save OrderFormulaItem : {}", orderFormulaItem);
        OrderFormulaItem orderFormulaItem = orderFormulaItemMapper.toEntity(orderFormulaItem);
        orderFormulaItem = orderFormulaItemRepository.save(orderFormulaItem);
        return orderFormulaItemMapper.toDto(orderFormulaItem);
    }

    @Override
    public OrderFormulaItem update(OrderFormulaItem orderFormulaItem) {
        log.debug("Request to update OrderFormulaItem : {}", orderFormulaItem);
        OrderFormulaItem orderFormulaItem = orderFormulaItemMapper.toEntity(orderFormulaItem);
        orderFormulaItem = orderFormulaItemRepository.save(orderFormulaItem);
        return orderFormulaItemMapper.toDto(orderFormulaItem);
    }

    @Override
    public Optional<OrderFormulaItem> partialUpdate(OrderFormulaItem orderFormulaItem) {
        log.debug("Request to partially update OrderFormulaItem : {}", orderFormulaItem);

        return orderFormulaItemRepository
            .findById(orderFormulaItem.getId())
            .map(existingOrderFormulaItem -> {
                orderFormulaItemMapper.partialUpdate(existingOrderFormulaItem, orderFormulaItem);

                return existingOrderFormulaItem;
            })
            .map(orderFormulaItemRepository::save)
            .map(orderFormulaItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderFormulaItem> findAll(Pageable pageable) {
        log.debug("Request to get all OrderFormulaItems");
        return orderFormulaItemRepository.findAll(pageable).map(orderFormulaItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderFormulaItem> findOne(Long id) {
        log.debug("Request to get OrderFormulaItem : {}", id);
        return orderFormulaItemRepository.findById(id).map(orderFormulaItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrderFormulaItem : {}", id);
        orderFormulaItemRepository.deleteById(id);
    }
}
