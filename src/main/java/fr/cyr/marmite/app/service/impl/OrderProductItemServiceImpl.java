package fr.cyr.marmite.app.service.impl;

import fr.cyr.marmite.app.domain.OrderProductItem;
import fr.cyr.marmite.app.repository.OrderProductItemRepository;
import fr.cyr.marmite.app.service.OrderProductItemService;
import fr.cyr.marmite.app.service.dto.OrderProductItem;
import fr.cyr.marmite.app.service.mapper.OrderProductItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link OrderProductItem}.
 */
@Service
@Transactional
public class OrderProductItemServiceImpl implements OrderProductItemService {

    private final Logger log = LoggerFactory.getLogger(OrderProductItemServiceImpl.class);

    private final OrderProductItemRepository orderProductItemRepository;

    private final OrderProductItemMapper orderProductItemMapper;

    public OrderProductItemServiceImpl(
        OrderProductItemRepository orderProductItemRepository,
        OrderProductItemMapper orderProductItemMapper
    ) {
        this.orderProductItemRepository = orderProductItemRepository;
        this.orderProductItemMapper = orderProductItemMapper;
    }

    @Override
    public OrderProductItem save(OrderProductItem orderProductItem) {
        log.debug("Request to save OrderProductItem : {}", orderProductItem);
        OrderProductItem orderProductItem = orderProductItemMapper.toEntity(orderProductItem);
        orderProductItem = orderProductItemRepository.save(orderProductItem);
        return orderProductItemMapper.toDto(orderProductItem);
    }

    @Override
    public OrderProductItem update(OrderProductItem orderProductItem) {
        log.debug("Request to update OrderProductItem : {}", orderProductItem);
        OrderProductItem orderProductItem = orderProductItemMapper.toEntity(orderProductItem);
        orderProductItem = orderProductItemRepository.save(orderProductItem);
        return orderProductItemMapper.toDto(orderProductItem);
    }

    @Override
    public Optional<OrderProductItem> partialUpdate(OrderProductItem orderProductItem) {
        log.debug("Request to partially update OrderProductItem : {}", orderProductItem);

        return orderProductItemRepository
            .findById(orderProductItem.getId())
            .map(existingOrderProductItem -> {
                orderProductItemMapper.partialUpdate(existingOrderProductItem, orderProductItem);

                return existingOrderProductItem;
            })
            .map(orderProductItemRepository::save)
            .map(orderProductItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderProductItem> findAll(Pageable pageable) {
        log.debug("Request to get all OrderProductItems");
        return orderProductItemRepository.findAll(pageable).map(orderProductItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderProductItem> findOne(Long id) {
        log.debug("Request to get OrderProductItem : {}", id);
        return orderProductItemRepository.findById(id).map(orderProductItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrderProductItem : {}", id);
        orderProductItemRepository.deleteById(id);
    }
}
