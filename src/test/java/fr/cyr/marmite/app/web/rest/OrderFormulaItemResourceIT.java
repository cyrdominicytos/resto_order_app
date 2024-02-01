package fr.cyr.marmite.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.cyr.marmite.app.IntegrationTest;
import fr.cyr.marmite.app.domain.OrderFormulaItem;
import fr.cyr.marmite.app.repository.OrderFormulaItemRepository;
import fr.cyr.marmite.app.service.dto.OrderFormulaItem;
import fr.cyr.marmite.app.service.mapper.OrderFormulaItemMapper;
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
 * Integration tests for the {@link OrderFormulaItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderFormulaItemResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String ENTITY_API_URL = "/api/order-formula-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderFormulaItemRepository orderFormulaItemRepository;

    @Autowired
    private OrderFormulaItemMapper orderFormulaItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderFormulaItemMockMvc;

    private OrderFormulaItem orderFormulaItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderFormulaItem createEntity(EntityManager em) {
        OrderFormulaItem orderFormulaItem = new OrderFormulaItem().quantity(DEFAULT_QUANTITY);
        return orderFormulaItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderFormulaItem createUpdatedEntity(EntityManager em) {
        OrderFormulaItem orderFormulaItem = new OrderFormulaItem().quantity(UPDATED_QUANTITY);
        return orderFormulaItem;
    }

    @BeforeEach
    public void initTest() {
        orderFormulaItem = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderFormulaItem() throws Exception {
        int databaseSizeBeforeCreate = orderFormulaItemRepository.findAll().size();
        // Create the OrderFormulaItem
        OrderFormulaItem orderFormulaItem = orderFormulaItemMapper.toDto(orderFormulaItem);
        restOrderFormulaItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderFormulaItem))
            )
            .andExpect(status().isCreated());

        // Validate the OrderFormulaItem in the database
        List<OrderFormulaItem> orderFormulaItemList = orderFormulaItemRepository.findAll();
        assertThat(orderFormulaItemList).hasSize(databaseSizeBeforeCreate + 1);
        OrderFormulaItem testOrderFormulaItem = orderFormulaItemList.get(orderFormulaItemList.size() - 1);
        assertThat(testOrderFormulaItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void createOrderFormulaItemWithExistingId() throws Exception {
        // Create the OrderFormulaItem with an existing ID
        orderFormulaItem.setId(1L);
        OrderFormulaItem orderFormulaItem = orderFormulaItemMapper.toDto(orderFormulaItem);

        int databaseSizeBeforeCreate = orderFormulaItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderFormulaItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderFormulaItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderFormulaItem in the database
        List<OrderFormulaItem> orderFormulaItemList = orderFormulaItemRepository.findAll();
        assertThat(orderFormulaItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderFormulaItems() throws Exception {
        // Initialize the database
        orderFormulaItemRepository.saveAndFlush(orderFormulaItem);

        // Get all the orderFormulaItemList
        restOrderFormulaItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderFormulaItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    void getOrderFormulaItem() throws Exception {
        // Initialize the database
        orderFormulaItemRepository.saveAndFlush(orderFormulaItem);

        // Get the orderFormulaItem
        restOrderFormulaItemMockMvc
            .perform(get(ENTITY_API_URL_ID, orderFormulaItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderFormulaItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    void getNonExistingOrderFormulaItem() throws Exception {
        // Get the orderFormulaItem
        restOrderFormulaItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderFormulaItem() throws Exception {
        // Initialize the database
        orderFormulaItemRepository.saveAndFlush(orderFormulaItem);

        int databaseSizeBeforeUpdate = orderFormulaItemRepository.findAll().size();

        // Update the orderFormulaItem
        OrderFormulaItem updatedOrderFormulaItem = orderFormulaItemRepository.findById(orderFormulaItem.getId()).get();
        // Disconnect from session so that the updates on updatedOrderFormulaItem are not directly saved in db
        em.detach(updatedOrderFormulaItem);
        updatedOrderFormulaItem.quantity(UPDATED_QUANTITY);
        OrderFormulaItem orderFormulaItem = orderFormulaItemMapper.toDto(updatedOrderFormulaItem);

        restOrderFormulaItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderFormulaItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderFormulaItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderFormulaItem in the database
        List<OrderFormulaItem> orderFormulaItemList = orderFormulaItemRepository.findAll();
        assertThat(orderFormulaItemList).hasSize(databaseSizeBeforeUpdate);
        OrderFormulaItem testOrderFormulaItem = orderFormulaItemList.get(orderFormulaItemList.size() - 1);
        assertThat(testOrderFormulaItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void putNonExistingOrderFormulaItem() throws Exception {
        int databaseSizeBeforeUpdate = orderFormulaItemRepository.findAll().size();
        orderFormulaItem.setId(count.incrementAndGet());

        // Create the OrderFormulaItem
        OrderFormulaItem orderFormulaItem = orderFormulaItemMapper.toDto(orderFormulaItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderFormulaItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderFormulaItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderFormulaItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderFormulaItem in the database
        List<OrderFormulaItem> orderFormulaItemList = orderFormulaItemRepository.findAll();
        assertThat(orderFormulaItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderFormulaItem() throws Exception {
        int databaseSizeBeforeUpdate = orderFormulaItemRepository.findAll().size();
        orderFormulaItem.setId(count.incrementAndGet());

        // Create the OrderFormulaItem
        OrderFormulaItem orderFormulaItem = orderFormulaItemMapper.toDto(orderFormulaItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderFormulaItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderFormulaItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderFormulaItem in the database
        List<OrderFormulaItem> orderFormulaItemList = orderFormulaItemRepository.findAll();
        assertThat(orderFormulaItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderFormulaItem() throws Exception {
        int databaseSizeBeforeUpdate = orderFormulaItemRepository.findAll().size();
        orderFormulaItem.setId(count.incrementAndGet());

        // Create the OrderFormulaItem
        OrderFormulaItem orderFormulaItem = orderFormulaItemMapper.toDto(orderFormulaItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderFormulaItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderFormulaItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderFormulaItem in the database
        List<OrderFormulaItem> orderFormulaItemList = orderFormulaItemRepository.findAll();
        assertThat(orderFormulaItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderFormulaItemWithPatch() throws Exception {
        // Initialize the database
        orderFormulaItemRepository.saveAndFlush(orderFormulaItem);

        int databaseSizeBeforeUpdate = orderFormulaItemRepository.findAll().size();

        // Update the orderFormulaItem using partial update
        OrderFormulaItem partialUpdatedOrderFormulaItem = new OrderFormulaItem();
        partialUpdatedOrderFormulaItem.setId(orderFormulaItem.getId());

        restOrderFormulaItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderFormulaItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderFormulaItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderFormulaItem in the database
        List<OrderFormulaItem> orderFormulaItemList = orderFormulaItemRepository.findAll();
        assertThat(orderFormulaItemList).hasSize(databaseSizeBeforeUpdate);
        OrderFormulaItem testOrderFormulaItem = orderFormulaItemList.get(orderFormulaItemList.size() - 1);
        assertThat(testOrderFormulaItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void fullUpdateOrderFormulaItemWithPatch() throws Exception {
        // Initialize the database
        orderFormulaItemRepository.saveAndFlush(orderFormulaItem);

        int databaseSizeBeforeUpdate = orderFormulaItemRepository.findAll().size();

        // Update the orderFormulaItem using partial update
        OrderFormulaItem partialUpdatedOrderFormulaItem = new OrderFormulaItem();
        partialUpdatedOrderFormulaItem.setId(orderFormulaItem.getId());

        partialUpdatedOrderFormulaItem.quantity(UPDATED_QUANTITY);

        restOrderFormulaItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderFormulaItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderFormulaItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderFormulaItem in the database
        List<OrderFormulaItem> orderFormulaItemList = orderFormulaItemRepository.findAll();
        assertThat(orderFormulaItemList).hasSize(databaseSizeBeforeUpdate);
        OrderFormulaItem testOrderFormulaItem = orderFormulaItemList.get(orderFormulaItemList.size() - 1);
        assertThat(testOrderFormulaItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void patchNonExistingOrderFormulaItem() throws Exception {
        int databaseSizeBeforeUpdate = orderFormulaItemRepository.findAll().size();
        orderFormulaItem.setId(count.incrementAndGet());

        // Create the OrderFormulaItem
        OrderFormulaItemDTO orderFormulaItemDTO = orderFormulaItemMapper.toDto(orderFormulaItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderFormulaItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderFormulaItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderFormulaItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderFormulaItem in the database
        List<OrderFormulaItem> orderFormulaItemList = orderFormulaItemRepository.findAll();
        assertThat(orderFormulaItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderFormulaItem() throws Exception {
        int databaseSizeBeforeUpdate = orderFormulaItemRepository.findAll().size();
        orderFormulaItem.setId(count.incrementAndGet());

        // Create the OrderFormulaItem
        OrderFormulaItemDTO orderFormulaItemDTO = orderFormulaItemMapper.toDto(orderFormulaItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderFormulaItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderFormulaItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderFormulaItem in the database
        List<OrderFormulaItem> orderFormulaItemList = orderFormulaItemRepository.findAll();
        assertThat(orderFormulaItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderFormulaItem() throws Exception {
        int databaseSizeBeforeUpdate = orderFormulaItemRepository.findAll().size();
        orderFormulaItem.setId(count.incrementAndGet());

        // Create the OrderFormulaItem
        OrderFormulaItemDTO orderFormulaItemDTO = orderFormulaItemMapper.toDto(orderFormulaItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderFormulaItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderFormulaItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderFormulaItem in the database
        List<OrderFormulaItem> orderFormulaItemList = orderFormulaItemRepository.findAll();
        assertThat(orderFormulaItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderFormulaItem() throws Exception {
        // Initialize the database
        orderFormulaItemRepository.saveAndFlush(orderFormulaItem);

        int databaseSizeBeforeDelete = orderFormulaItemRepository.findAll().size();

        // Delete the orderFormulaItem
        restOrderFormulaItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderFormulaItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderFormulaItem> orderFormulaItemList = orderFormulaItemRepository.findAll();
        assertThat(orderFormulaItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
