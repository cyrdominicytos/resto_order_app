package fr.cyr.marmite.app.web.rest;

import fr.cyr.marmite.app.repository.OrderFormulaItemRepository;
import fr.cyr.marmite.app.service.OrderFormulaItemService;
import fr.cyr.marmite.app.service.dto.OrderFormulaItem;
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
 * REST controller for managing {@link fr.cyr.marmite.app.domain.OrderFormulaItem}.
 */
@RestController
@RequestMapping("/api")
public class OrderFormulaItemResource {

    private final Logger log = LoggerFactory.getLogger(OrderFormulaItemResource.class);

    private static final String ENTITY_NAME = "orderFormulaItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderFormulaItemService orderFormulaItemService;

    private final OrderFormulaItemRepository orderFormulaItemRepository;

    public OrderFormulaItemResource(
        OrderFormulaItemService orderFormulaItemService,
        OrderFormulaItemRepository orderFormulaItemRepository
    ) {
        this.orderFormulaItemService = orderFormulaItemService;
        this.orderFormulaItemRepository = orderFormulaItemRepository;
    }

    /**
     * {@code POST  /order-formula-items} : Create a new orderFormulaItem.
     *
     * @param orderFormulaItem the orderFormulaItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderFormulaItem, or with status {@code 400 (Bad Request)} if the orderFormulaItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-formula-items")
    public ResponseEntity<OrderFormulaItem> createOrderFormulaItem(@RequestBody OrderFormulaItem orderFormulaItem)
        throws URISyntaxException {
        log.debug("REST request to save OrderFormulaItem : {}", orderFormulaItem);
        if (orderFormulaItem.getId() != null) {
            throw new BadRequestAlertException("A new orderFormulaItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderFormulaItem result = orderFormulaItemService.save(orderFormulaItem);
        return ResponseEntity
            .created(new URI("/api/order-formula-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-formula-items/:id} : Updates an existing orderFormulaItem.
     *
     * @param id the id of the orderFormulaItem to save.
     * @param orderFormulaItem the orderFormulaItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderFormulaItem,
     * or with status {@code 400 (Bad Request)} if the orderFormulaItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderFormulaItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-formula-items/{id}")
    public ResponseEntity<OrderFormulaItem> updateOrderFormulaItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderFormulaItem orderFormulaItem
    ) throws URISyntaxException {
        log.debug("REST request to update OrderFormulaItem : {}, {}", id, orderFormulaItem);
        if (orderFormulaItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderFormulaItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderFormulaItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OrderFormulaItem result = orderFormulaItemService.update(orderFormulaItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderFormulaItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /order-formula-items/:id} : Partial updates given fields of an existing orderFormulaItem, field will ignore if it is null
     *
     * @param id the id of the orderFormulaItem to save.
     * @param orderFormulaItem the orderFormulaItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderFormulaItem,
     * or with status {@code 400 (Bad Request)} if the orderFormulaItem is not valid,
     * or with status {@code 404 (Not Found)} if the orderFormulaItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderFormulaItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-formula-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderFormulaItem> partialUpdateOrderFormulaItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OrderFormulaItem orderFormulaItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderFormulaItem partially : {}, {}", id, orderFormulaItem);
        if (orderFormulaItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderFormulaItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderFormulaItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderFormulaItem> result = orderFormulaItemService.partialUpdate(orderFormulaItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderFormulaItem.getId().toString())
        );
    }

    /**
     * {@code GET  /order-formula-items} : get all the orderFormulaItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderFormulaItems in body.
     */
    @GetMapping("/order-formula-items")
    public ResponseEntity<List<OrderFormulaItem>> getAllOrderFormulaItems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of OrderFormulaItems");
        Page<OrderFormulaItem> page = orderFormulaItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-formula-items/:id} : get the "id" orderFormulaItem.
     *
     * @param id the id of the orderFormulaItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderFormulaItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-formula-items/{id}")
    public ResponseEntity<OrderFormulaItem> getOrderFormulaItem(@PathVariable Long id) {
        log.debug("REST request to get OrderFormulaItem : {}", id);
        Optional<OrderFormulaItem> orderFormulaItem = orderFormulaItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderFormulaItem);
    }

    /**
     * {@code DELETE  /order-formula-items/:id} : delete the "id" orderFormulaItem.
     *
     * @param id the id of the orderFormulaItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-formula-items/{id}")
    public ResponseEntity<Void> deleteOrderFormulaItem(@PathVariable Long id) {
        log.debug("REST request to delete OrderFormulaItem : {}", id);
        orderFormulaItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
