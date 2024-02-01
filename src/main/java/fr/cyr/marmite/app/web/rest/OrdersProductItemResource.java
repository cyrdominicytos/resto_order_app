package fr.cyr.marmite.app.web.rest;

import fr.cyr.marmite.app.repository.OrdersProductItemRepository;
import fr.cyr.marmite.app.service.OrdersProductItemService;
import fr.cyr.marmite.app.service.dto.OrdersProductItem;
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
 * REST controller for managing {@link fr.cyr.marmite.app.domain.OrdersProductItem}.
 */
@RestController
@RequestMapping("/api")
public class OrdersProductItemResource {

    private final Logger log = LoggerFactory.getLogger(OrdersProductItemResource.class);

    private static final String ENTITY_NAME = "ordersProductItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdersProductItemService ordersProductItemService;

    private final OrdersProductItemRepository ordersProductItemRepository;

    public OrdersProductItemResource(
        OrdersProductItemService ordersProductItemService,
        OrdersProductItemRepository ordersProductItemRepository
    ) {
        this.ordersProductItemService = ordersProductItemService;
        this.ordersProductItemRepository = ordersProductItemRepository;
    }

    /**
     * {@code POST  /orders-product-items} : Create a new ordersProductItem.
     *
     * @param ordersProductItem the ordersProductItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordersProductItem, or with status {@code 400 (Bad Request)} if the ordersProductItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/orders-product-items")
    public ResponseEntity<OrdersProductItem> createOrdersProductItem(@RequestBody OrdersProductItem ordersProductItem)
        throws URISyntaxException {
        log.debug("REST request to save OrdersProductItem : {}", ordersProductItem);
        if (ordersProductItem.getId() != null) {
            throw new BadRequestAlertException("A new ordersProductItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdersProductItem result = ordersProductItemService.save(ordersProductItem);
        return ResponseEntity
            .created(new URI("/api/orders-product-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /orders-product-items/:id} : Updates an existing ordersProductItem.
     *
     * @param id the id of the ordersProductItem to save.
     * @param ordersProductItem the ordersProductItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordersProductItem,
     * or with status {@code 400 (Bad Request)} if the ordersProductItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordersProductItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/orders-product-items/{id}")
    public ResponseEntity<OrdersProductItem> updateOrdersProductItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdersProductItem ordersProductItem
    ) throws URISyntaxException {
        log.debug("REST request to update OrdersProductItem : {}, {}", id, ordersProductItem);
        if (ordersProductItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordersProductItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordersProductItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdersProductItem result = ordersProductItemService.update(ordersProductItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordersProductItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /orders-product-items/:id} : Partial updates given fields of an existing ordersProductItem, field will ignore if it is null
     *
     * @param id the id of the ordersProductItem to save.
     * @param ordersProductItem the ordersProductItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordersProductItem,
     * or with status {@code 400 (Bad Request)} if the ordersProductItem is not valid,
     * or with status {@code 404 (Not Found)} if the ordersProductItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordersProductItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/orders-product-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrdersProductItem> partialUpdateOrdersProductItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdersProductItem ordersProductItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdersProductItem partially : {}, {}", id, ordersProductItem);
        if (ordersProductItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordersProductItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordersProductItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdersProductItem> result = ordersProductItemService.partialUpdate(ordersProductItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordersProductItem.getId().toString())
        );
    }

    /**
     * {@code GET  /orders-product-items} : get all the ordersProductItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordersProductItems in body.
     */
    @GetMapping("/orders-product-items")
    public ResponseEntity<List<OrdersProductItem>> getAllOrdersProductItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of OrdersProductItems");
        Page<OrdersProductItem> page = ordersProductItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /orders-product-items/:id} : get the "id" ordersProductItem.
     *
     * @param id the id of the ordersProductItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordersProductItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/orders-product-items/{id}")
    public ResponseEntity<OrdersProductItem> getOrdersProductItem(@PathVariable Long id) {
        log.debug("REST request to get OrdersProductItem : {}", id);
        Optional<OrdersProductItem> ordersProductItem = ordersProductItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordersProductItem);
    }

    /**
     * {@code DELETE  /orders-product-items/:id} : delete the "id" ordersProductItem.
     *
     * @param id the id of the ordersProductItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/orders-product-items/{id}")
    public ResponseEntity<Void> deleteOrdersProductItem(@PathVariable Long id) {
        log.debug("REST request to delete OrdersProductItem : {}", id);
        ordersProductItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
