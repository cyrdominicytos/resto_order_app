package fr.cyr.marmite.app.service.impl;

import fr.cyr.marmite.app.domain.Orders;
import fr.cyr.marmite.app.repository.OrdersRepository;
import fr.cyr.marmite.app.service.OrdersService;
import fr.cyr.marmite.app.service.dto.Orders;
import fr.cyr.marmite.app.service.mapper.OrdersMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Orders}.
 */
@Service
@Transactional
public class OrdersServiceImpl implements OrdersService {

    private final Logger log = LoggerFactory.getLogger(OrdersServiceImpl.class);

    private final OrdersRepository ordersRepository;

    private final OrdersMapper ordersMapper;

    public OrdersServiceImpl(OrdersRepository ordersRepository, OrdersMapper ordersMapper) {
        this.ordersRepository = ordersRepository;
        this.ordersMapper = ordersMapper;
    }

    @Override
    public Orders save(Orders orders) {
        log.debug("Request to save Orders : {}", orders);
        Orders orders = ordersMapper.toEntity(orders);
        orders = ordersRepository.save(orders);
        return ordersMapper.toDto(orders);
    }

    @Override
    public Orders update(Orders orders) {
        log.debug("Request to update Orders : {}", orders);
        Orders orders = ordersMapper.toEntity(orders);
        orders = ordersRepository.save(orders);
        return ordersMapper.toDto(orders);
    }

    @Override
    public Optional<Orders> partialUpdate(Orders orders) {
        log.debug("Request to partially update Orders : {}", orders);

        return ordersRepository
            .findById(orders.getId())
            .map(existingOrders -> {
                ordersMapper.partialUpdate(existingOrders, orders);

                return existingOrders;
            })
            .map(ordersRepository::save)
            .map(ordersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Orders> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        return ordersRepository.findAll(pageable).map(ordersMapper::toDto);
    }

    public Page<Orders> findAllWithEagerRelationships(Pageable pageable) {
        return ordersRepository.findAllWithEagerRelationships(pageable).map(ordersMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Orders> findOne(Long id) {
        log.debug("Request to get Orders : {}", id);
        return ordersRepository.findOneWithEagerRelationships(id).map(ordersMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Orders : {}", id);
        ordersRepository.deleteById(id);
    }
}
