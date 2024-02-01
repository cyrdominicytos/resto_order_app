package fr.cyr.marmite.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.cyr.marmite.app.IntegrationTest;
import fr.cyr.marmite.app.domain.OrdersFormulaItem;
import fr.cyr.marmite.app.repository.OrdersFormulaItemRepository;
import fr.cyr.marmite.app.service.dto.OrdersFormulaItem;
import fr.cyr.marmite.app.service.mapper.OrdersFormulaItemMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OrdersFormulaItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdersFormulaItemResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String ENTITY_API_URL = "/api/orders-formula-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdersFormulaItemRepository ordersFormulaItemRepository;

    @Autowired
    private OrdersFormulaItemMapper ordersFormulaItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdersFormulaItemMockMvc;

    private OrdersFormulaItem ordersFormulaItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdersFormulaItem createEntity(EntityManager em) {
        OrdersFormulaItem ordersFormulaItem = new OrdersFormulaItem().quantity(DEFAULT_QUANTITY);
        return ordersFormulaItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdersFormulaItem createUpdatedEntity(EntityManager em) {
        OrdersFormulaItem ordersFormulaItem = new OrdersFormulaItem().quantity(UPDATED_QUANTITY);
        return ordersFormulaItem;
    }

    @BeforeEach
    public void initTest() {
        ordersFormulaItem = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdersFormulaItem() throws Exception {
        int databaseSizeBeforeCreate = ordersFormulaItemRepository.findAll().size();
        // Create the OrdersFormulaItem
        OrdersFormulaItem ordersFormulaItem = ordersFormulaItemMapper.toDto(ordersFormulaItem);
        restOrdersFormulaItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersFormulaItem))
            )
            .andExpect(status().isCreated());

        // Validate the OrdersFormulaItem in the database
        List<OrdersFormulaItem> ordersFormulaItemList = ordersFormulaItemRepository.findAll();
        assertThat(ordersFormulaItemList).hasSize(databaseSizeBeforeCreate + 1);
        OrdersFormulaItem testOrdersFormulaItem = ordersFormulaItemList.get(ordersFormulaItemList.size() - 1);
        assertThat(testOrdersFormulaItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void createOrdersFormulaItemWithExistingId() throws Exception {
        // Create the OrdersFormulaItem with an existing ID
        ordersFormulaItem.setId(1L);
        OrdersFormulaItem ordersFormulaItem = ordersFormulaItemMapper.toDto(ordersFormulaItem);

        int databaseSizeBeforeCreate = ordersFormulaItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdersFormulaItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersFormulaItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdersFormulaItem in the database
        List<OrdersFormulaItem> ordersFormulaItemList = ordersFormulaItemRepository.findAll();
        assertThat(ordersFormulaItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdersFormulaItems() throws Exception {
        // Initialize the database
        ordersFormulaItemRepository.saveAndFlush(ordersFormulaItem);

        // Get all the ordersFormulaItemList
        restOrdersFormulaItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordersFormulaItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    void getOrdersFormulaItem() throws Exception {
        // Initialize the database
        ordersFormulaItemRepository.saveAndFlush(ordersFormulaItem);

        // Get the ordersFormulaItem
        restOrdersFormulaItemMockMvc
            .perform(get(ENTITY_API_URL_ID, ordersFormulaItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordersFormulaItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    void getNonExistingOrdersFormulaItem() throws Exception {
        // Get the ordersFormulaItem
        restOrdersFormulaItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrdersFormulaItem() throws Exception {
        // Initialize the database
        ordersFormulaItemRepository.saveAndFlush(ordersFormulaItem);

        int databaseSizeBeforeUpdate = ordersFormulaItemRepository.findAll().size();

        // Update the ordersFormulaItem
        OrdersFormulaItem updatedOrdersFormulaItem = ordersFormulaItemRepository.findById(ordersFormulaItem.getId()).get();
        // Disconnect from session so that the updates on updatedOrdersFormulaItem are not directly saved in db
        em.detach(updatedOrdersFormulaItem);
        updatedOrdersFormulaItem.quantity(UPDATED_QUANTITY);
        OrdersFormulaItem ordersFormulaItem = ordersFormulaItemMapper.toDto(updatedOrdersFormulaItem);

        restOrdersFormulaItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordersFormulaItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordersFormulaItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrdersFormulaItem in the database
        List<OrdersFormulaItem> ordersFormulaItemList = ordersFormulaItemRepository.findAll();
        assertThat(ordersFormulaItemList).hasSize(databaseSizeBeforeUpdate);
        OrdersFormulaItem testOrdersFormulaItem = ordersFormulaItemList.get(ordersFormulaItemList.size() - 1);
        assertThat(testOrdersFormulaItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void putNonExistingOrdersFormulaItem() throws Exception {
        int databaseSizeBeforeUpdate = ordersFormulaItemRepository.findAll().size();
        ordersFormulaItem.setId(count.incrementAndGet());

        // Create the OrdersFormulaItem
        OrdersFormulaItem ordersFormulaItem = ordersFormulaItemMapper.toDto(ordersFormulaItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersFormulaItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordersFormulaItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordersFormulaItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdersFormulaItem in the database
        List<OrdersFormulaItem> ordersFormulaItemList = ordersFormulaItemRepository.findAll();
        assertThat(ordersFormulaItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdersFormulaItem() throws Exception {
        int databaseSizeBeforeUpdate = ordersFormulaItemRepository.findAll().size();
        ordersFormulaItem.setId(count.incrementAndGet());

        // Create the OrdersFormulaItem
        OrdersFormulaItem ordersFormulaItem = ordersFormulaItemMapper.toDto(ordersFormulaItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersFormulaItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordersFormulaItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdersFormulaItem in the database
        List<OrdersFormulaItem> ordersFormulaItemList = ordersFormulaItemRepository.findAll();
        assertThat(ordersFormulaItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdersFormulaItem() throws Exception {
        int databaseSizeBeforeUpdate = ordersFormulaItemRepository.findAll().size();
        ordersFormulaItem.setId(count.incrementAndGet());

        // Create the OrdersFormulaItem
        OrdersFormulaItem ordersFormulaItem = ordersFormulaItemMapper.toDto(ordersFormulaItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersFormulaItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersFormulaItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdersFormulaItem in the database
        List<OrdersFormulaItem> ordersFormulaItemList = ordersFormulaItemRepository.findAll();
        assertThat(ordersFormulaItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdersFormulaItemWithPatch() throws Exception {
        // Initialize the database
        ordersFormulaItemRepository.saveAndFlush(ordersFormulaItem);

        int databaseSizeBeforeUpdate = ordersFormulaItemRepository.findAll().size();

        // Update the ordersFormulaItem using partial update
        OrdersFormulaItem partialUpdatedOrdersFormulaItem = new OrdersFormulaItem();
        partialUpdatedOrdersFormulaItem.setId(ordersFormulaItem.getId());

        partialUpdatedOrdersFormulaItem.quantity(UPDATED_QUANTITY);

        restOrdersFormulaItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdersFormulaItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdersFormulaItem))
            )
            .andExpect(status().isOk());

        // Validate the OrdersFormulaItem in the database
        List<OrdersFormulaItem> ordersFormulaItemList = ordersFormulaItemRepository.findAll();
        assertThat(ordersFormulaItemList).hasSize(databaseSizeBeforeUpdate);
        OrdersFormulaItem testOrdersFormulaItem = ordersFormulaItemList.get(ordersFormulaItemList.size() - 1);
        assertThat(testOrdersFormulaItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void fullUpdateOrdersFormulaItemWithPatch() throws Exception {
        // Initialize the database
        ordersFormulaItemRepository.saveAndFlush(ordersFormulaItem);

        int databaseSizeBeforeUpdate = ordersFormulaItemRepository.findAll().size();

        // Update the ordersFormulaItem using partial update
        OrdersFormulaItem partialUpdatedOrdersFormulaItem = new OrdersFormulaItem();
        partialUpdatedOrdersFormulaItem.setId(ordersFormulaItem.getId());

        partialUpdatedOrdersFormulaItem.quantity(UPDATED_QUANTITY);

        restOrdersFormulaItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdersFormulaItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdersFormulaItem))
            )
            .andExpect(status().isOk());

        // Validate the OrdersFormulaItem in the database
        List<OrdersFormulaItem> ordersFormulaItemList = ordersFormulaItemRepository.findAll();
        assertThat(ordersFormulaItemList).hasSize(databaseSizeBeforeUpdate);
        OrdersFormulaItem testOrdersFormulaItem = ordersFormulaItemList.get(ordersFormulaItemList.size() - 1);
        assertThat(testOrdersFormulaItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void patchNonExistingOrdersFormulaItem() throws Exception {
        int databaseSizeBeforeUpdate = ordersFormulaItemRepository.findAll().size();
        ordersFormulaItem.setId(count.incrementAndGet());

        // Create the OrdersFormulaItem
        OrdersFormulaItemDTO ordersFormulaItemDTO = ordersFormulaItemMapper.toDto(ordersFormulaItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersFormulaItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordersFormulaItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordersFormulaItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdersFormulaItem in the database
        List<OrdersFormulaItem> ordersFormulaItemList = ordersFormulaItemRepository.findAll();
        assertThat(ordersFormulaItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdersFormulaItem() throws Exception {
        int databaseSizeBeforeUpdate = ordersFormulaItemRepository.findAll().size();
        ordersFormulaItem.setId(count.incrementAndGet());

        // Create the OrdersFormulaItem
        OrdersFormulaItemDTO ordersFormulaItemDTO = ordersFormulaItemMapper.toDto(ordersFormulaItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersFormulaItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordersFormulaItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdersFormulaItem in the database
        List<OrdersFormulaItem> ordersFormulaItemList = ordersFormulaItemRepository.findAll();
        assertThat(ordersFormulaItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdersFormulaItem() throws Exception {
        int databaseSizeBeforeUpdate = ordersFormulaItemRepository.findAll().size();
        ordersFormulaItem.setId(count.incrementAndGet());

        // Create the OrdersFormulaItem
        OrdersFormulaItemDTO ordersFormulaItemDTO = ordersFormulaItemMapper.toDto(ordersFormulaItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersFormulaItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordersFormulaItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdersFormulaItem in the database
        List<OrdersFormulaItem> ordersFormulaItemList = ordersFormulaItemRepository.findAll();
        assertThat(ordersFormulaItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdersFormulaItem() throws Exception {
        // Initialize the database
        ordersFormulaItemRepository.saveAndFlush(ordersFormulaItem);

        int databaseSizeBeforeDelete = ordersFormulaItemRepository.findAll().size();

        // Delete the ordersFormulaItem
        restOrdersFormulaItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordersFormulaItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdersFormulaItem> ordersFormulaItemList = ordersFormulaItemRepository.findAll();
        assertThat(ordersFormulaItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
