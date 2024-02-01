package fr.cyr.marmite.app.web.rest;

import fr.cyr.marmite.app.repository.OrdersFormulaItemRepository;
import fr.cyr.marmite.app.service.OrdersFormulaItemService;
import fr.cyr.marmite.app.service.dto.OrdersFormulaItem;
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
 * REST controller for managing {@link fr.cyr.marmite.app.domain.OrdersFormulaItem}.
 */
@RestController
@RequestMapping("/api")
public class OrdersFormulaItemResource {

    private final Logger log = LoggerFactory.getLogger(OrdersFormulaItemResource.class);

    private static final String ENTITY_NAME = "ordersFormulaItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrdersFormulaItemService ordersFormulaItemService;

    private final OrdersFormulaItemRepository ordersFormulaItemRepository;

    public OrdersFormulaItemResource(
        OrdersFormulaItemService ordersFormulaItemService,
        OrdersFormulaItemRepository ordersFormulaItemRepository
    ) {
        this.ordersFormulaItemService = ordersFormulaItemService;
        this.ordersFormulaItemRepository = ordersFormulaItemRepository;
    }

    /**
     * {@code POST  /orders-formula-items} : Create a new ordersFormulaItem.
     *
     * @param ordersFormulaItem the ordersFormulaItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ordersFormulaItem, or with status {@code 400 (Bad Request)} if the ordersFormulaItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/orders-formula-items")
    public ResponseEntity<OrdersFormulaItem> createOrdersFormulaItem(@RequestBody OrdersFormulaItem ordersFormulaItem)
        throws URISyntaxException {
        log.debug("REST request to save OrdersFormulaItem : {}", ordersFormulaItem);
        if (ordersFormulaItem.getId() != null) {
            throw new BadRequestAlertException("A new ordersFormulaItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrdersFormulaItem result = ordersFormulaItemService.save(ordersFormulaItem);
        return ResponseEntity
            .created(new URI("/api/orders-formula-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /orders-formula-items/:id} : Updates an existing ordersFormulaItem.
     *
     * @param id the id of the ordersFormulaItem to save.
     * @param ordersFormulaItem the ordersFormulaItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordersFormulaItem,
     * or with status {@code 400 (Bad Request)} if the ordersFormulaItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ordersFormulaItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/orders-formula-items/{id}")
    public ResponseEntity<OrdersFormulaItem> updateOrdersFormulaItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdersFormulaItem ordersFormulaItem
    ) throws URISyntaxException {
        log.debug("REST request to update OrdersFormulaItem : {}, {}", id, ordersFormulaItem);
        if (ordersFormulaItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordersFormulaItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordersFormulaItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrdersFormulaItem result = ordersFormulaItemService.update(ordersFormulaItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordersFormulaItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /orders-formula-items/:id} : Partial updates given fields of an existing ordersFormulaItem, field will ignore if it is null
     *
     * @param id the id of the ordersFormulaItem to save.
     * @param ordersFormulaItem the ordersFormulaItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ordersFormulaItem,
     * or with status {@code 400 (Bad Request)} if the ordersFormulaItem is not valid,
     * or with status {@code 404 (Not Found)} if the ordersFormulaItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the ordersFormulaItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/orders-formula-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrdersFormulaItem> partialUpdateOrdersFormulaItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrdersFormulaItem ordersFormulaItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrdersFormulaItem partially : {}, {}", id, ordersFormulaItem);
        if (ordersFormulaItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ordersFormulaItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ordersFormulaItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrdersFormulaItem> result = ordersFormulaItemService.partialUpdate(ordersFormulaItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ordersFormulaItem.getId().toString())
        );
    }

    /**
     * {@code GET  /orders-formula-items} : get all the ordersFormulaItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ordersFormulaItems in body.
     */
    @GetMapping("/orders-formula-items")
    public ResponseEntity<List<OrdersFormulaItem>> getAllOrdersFormulaItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of OrdersFormulaItems");
        Page<OrdersFormulaItem> page = ordersFormulaItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /orders-formula-items/:id} : get the "id" ordersFormulaItem.
     *
     * @param id the id of the ordersFormulaItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ordersFormulaItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/orders-formula-items/{id}")
    public ResponseEntity<OrdersFormulaItem> getOrdersFormulaItem(@PathVariable Long id) {
        log.debug("REST request to get OrdersFormulaItem : {}", id);
        Optional<OrdersFormulaItem> ordersFormulaItem = ordersFormulaItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ordersFormulaItem);
    }

    /**
     * {@code DELETE  /orders-formula-items/:id} : delete the "id" ordersFormulaItem.
     *
     * @param id the id of the ordersFormulaItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/orders-formula-items/{id}")
    public ResponseEntity<Void> deleteOrdersFormulaItem(@PathVariable Long id) {
        log.debug("REST request to delete OrdersFormulaItem : {}", id);
        ordersFormulaItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
