package fr.cyr.marmite.app.web.rest;

import fr.cyr.marmite.app.repository.OrderProductItemRepository;
import fr.cyr.marmite.app.service.OrderProductItemService;
import fr.cyr.marmite.app.service.dto.OrderProductItem;
import fr.cyr.marmite.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.cyr.marmite.app.domain.OrderProductItem}.
 */
@RestController
@RequestMapping("/api")
public class OrderProductItemResource {

    private final Logger log = LoggerFactory.getLogger(OrderProductItemResource.class);

    private static final String ENTITY_NAME = "orderProductItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderProductItemService orderProductItemService;

    private final OrderProductItemRepository orderProductItemRepository;

    public OrderProductItemResource(
        OrderProductItemService orderProductItemService,
        OrderProductItemRepository orderProductItemRepository
    ) {
        this.orderProductItemService = orderProductItemService;
        this.orderProductItemRepository = orderProductItemRepository;
    }

    /**
     * {@code POST  /order-product-items} : Create a new orderProductItem.
     *
     * @param orderProductItem the orderProductItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderProductItem, or with status {@code 400 (Bad Request)} if the orderProductItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-product-items")
    public ResponseEntity<OrderProductItem> createOrderProductItem(@RequestBody OrderProductItem orderProductItem)
        throws URISyntaxException {
        log.debug("REST request to save OrderProductItem : {}", orderProductItem);
        if (orderProductItem.getId() != null) {
            throw new BadRequestAlertException("A new orderProductItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderProductItem result = orderProductItemService.save(orderProductItem);
        return ResponseEntity
            .created(new URI("/api/order-product-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-product-items/:id} : Updates an existing orderProductItem.
     *
     * @param id the id of the orderProductItem to save.
     * @param orderProductItem the orderProductItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderProductItem,
     * or with status {@code 400 (Bad Request)} if the orderProductItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderProductItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-product-items/{id}")
    public ResponseEntity<OrderProductItem> updateOrderProductItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderProductItem orderProductItem
    ) throws URISyntaxException {
        log.debug("REST request to update OrderProductItem : {}, {}", id, orderProductItem);
        if (orderProductItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderProductItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderProductItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderProductItem result = orderProductItemService.update(orderProductItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderProductItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-product-items/:id} : Partial updates given fields of an existing orderProductItem, field will ignore if it is null
     *
     * @param id the id of the orderProductItem to save.
     * @param orderProductItem the orderProductItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderProductItem,
     * or with status {@code 400 (Bad Request)} if the orderProductItem is not valid,
     * or with status {@code 404 (Not Found)} if the orderProductItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderProductItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-product-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderProductItem> partialUpdateOrderProductItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderProductItem orderProductItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderProductItem partially : {}, {}", id, orderProductItem);
        if (orderProductItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderProductItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderProductItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderProductItem> result = orderProductItemService.partialUpdate(orderProductItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderProductItem.getId().toString())
        );
    }

    /**
     * {@code GET  /order-product-items} : get all the orderProductItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderProductItems in body.
     */
    @GetMapping("/order-product-items")
    public ResponseEntity<List<OrderProductItem>> getAllOrderProductItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of OrderProductItems");
        Page<OrderProductItem> page = orderProductItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-product-items/:id} : get the "id" orderProductItem.
     *
     * @param id the id of the orderProductItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderProductItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-product-items/{id}")
    public ResponseEntity<OrderProductItem> getOrderProductItem(@PathVariable Long id) {
        log.debug("REST request to get OrderProductItem : {}", id);
        Optional<OrderProductItem> orderProductItem = orderProductItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderProductItem);
    }

    /**
     * {@code DELETE  /order-product-items/:id} : delete the "id" orderProductItem.
     *
     * @param id the id of the orderProductItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-product-items/{id}")
    public ResponseEntity<Void> deleteOrderProductItem(@PathVariable Long id) {
        log.debug("REST request to delete OrderProductItem : {}", id);
        orderProductItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
