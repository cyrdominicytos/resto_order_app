package fr.cyr.marmite.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.cyr.marmite.app.IntegrationTest;
import fr.cyr.marmite.app.domain.OrdersProductItem;
import fr.cyr.marmite.app.repository.OrdersProductItemRepository;
import fr.cyr.marmite.app.service.dto.OrdersProductItem;
import fr.cyr.marmite.app.service.mapper.OrdersProductItemMapper;
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
 * Integration tests for the {@link OrdersProductItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrdersProductItemResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String ENTITY_API_URL = "/api/orders-product-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrdersProductItemRepository ordersProductItemRepository;

    @Autowired
    private OrdersProductItemMapper ordersProductItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrdersProductItemMockMvc;

    private OrdersProductItem ordersProductItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdersProductItem createEntity(EntityManager em) {
        OrdersProductItem ordersProductItem = new OrdersProductItem().quantity(DEFAULT_QUANTITY);
        return ordersProductItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrdersProductItem createUpdatedEntity(EntityManager em) {
        OrdersProductItem ordersProductItem = new OrdersProductItem().quantity(UPDATED_QUANTITY);
        return ordersProductItem;
    }

    @BeforeEach
    public void initTest() {
        ordersProductItem = createEntity(em);
    }

    @Test
    @Transactional
    void createOrdersProductItem() throws Exception {
        int databaseSizeBeforeCreate = ordersProductItemRepository.findAll().size();
        // Create the OrdersProductItem
        OrdersProductItem ordersProductItem = ordersProductItemMapper.toDto(ordersProductItem);
        restOrdersProductItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersProductItem))
            )
            .andExpect(status().isCreated());

        // Validate the OrdersProductItem in the database
        List<OrdersProductItem> ordersProductItemList = ordersProductItemRepository.findAll();
        assertThat(ordersProductItemList).hasSize(databaseSizeBeforeCreate + 1);
        OrdersProductItem testOrdersProductItem = ordersProductItemList.get(ordersProductItemList.size() - 1);
        assertThat(testOrdersProductItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void createOrdersProductItemWithExistingId() throws Exception {
        // Create the OrdersProductItem with an existing ID
        ordersProductItem.setId(1L);
        OrdersProductItem ordersProductItem = ordersProductItemMapper.toDto(ordersProductItem);

        int databaseSizeBeforeCreate = ordersProductItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrdersProductItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersProductItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdersProductItem in the database
        List<OrdersProductItem> ordersProductItemList = ordersProductItemRepository.findAll();
        assertThat(ordersProductItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrdersProductItems() throws Exception {
        // Initialize the database
        ordersProductItemRepository.saveAndFlush(ordersProductItem);

        // Get all the ordersProductItemList
        restOrdersProductItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ordersProductItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    void getOrdersProductItem() throws Exception {
        // Initialize the database
        ordersProductItemRepository.saveAndFlush(ordersProductItem);

        // Get the ordersProductItem
        restOrdersProductItemMockMvc
            .perform(get(ENTITY_API_URL_ID, ordersProductItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ordersProductItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    void getNonExistingOrdersProductItem() throws Exception {
        // Get the ordersProductItem
        restOrdersProductItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrdersProductItem() throws Exception {
        // Initialize the database
        ordersProductItemRepository.saveAndFlush(ordersProductItem);

        int databaseSizeBeforeUpdate = ordersProductItemRepository.findAll().size();

        // Update the ordersProductItem
        OrdersProductItem updatedOrdersProductItem = ordersProductItemRepository.findById(ordersProductItem.getId()).get();
        // Disconnect from session so that the updates on updatedOrdersProductItem are not directly saved in db
        em.detach(updatedOrdersProductItem);
        updatedOrdersProductItem.quantity(UPDATED_QUANTITY);
        OrdersProductItem ordersProductItem = ordersProductItemMapper.toDto(updatedOrdersProductItem);

        restOrdersProductItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordersProductItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordersProductItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrdersProductItem in the database
        List<OrdersProductItem> ordersProductItemList = ordersProductItemRepository.findAll();
        assertThat(ordersProductItemList).hasSize(databaseSizeBeforeUpdate);
        OrdersProductItem testOrdersProductItem = ordersProductItemList.get(ordersProductItemList.size() - 1);
        assertThat(testOrdersProductItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void putNonExistingOrdersProductItem() throws Exception {
        int databaseSizeBeforeUpdate = ordersProductItemRepository.findAll().size();
        ordersProductItem.setId(count.incrementAndGet());

        // Create the OrdersProductItem
        OrdersProductItem ordersProductItem = ordersProductItemMapper.toDto(ordersProductItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersProductItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ordersProductItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordersProductItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdersProductItem in the database
        List<OrdersProductItem> ordersProductItemList = ordersProductItemRepository.findAll();
        assertThat(ordersProductItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrdersProductItem() throws Exception {
        int databaseSizeBeforeUpdate = ordersProductItemRepository.findAll().size();
        ordersProductItem.setId(count.incrementAndGet());

        // Create the OrdersProductItem
        OrdersProductItem ordersProductItem = ordersProductItemMapper.toDto(ordersProductItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersProductItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ordersProductItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdersProductItem in the database
        List<OrdersProductItem> ordersProductItemList = ordersProductItemRepository.findAll();
        assertThat(ordersProductItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrdersProductItem() throws Exception {
        int databaseSizeBeforeUpdate = ordersProductItemRepository.findAll().size();
        ordersProductItem.setId(count.incrementAndGet());

        // Create the OrdersProductItem
        OrdersProductItem ordersProductItem = ordersProductItemMapper.toDto(ordersProductItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersProductItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ordersProductItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdersProductItem in the database
        List<OrdersProductItem> ordersProductItemList = ordersProductItemRepository.findAll();
        assertThat(ordersProductItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrdersProductItemWithPatch() throws Exception {
        // Initialize the database
        ordersProductItemRepository.saveAndFlush(ordersProductItem);

        int databaseSizeBeforeUpdate = ordersProductItemRepository.findAll().size();

        // Update the ordersProductItem using partial update
        OrdersProductItem partialUpdatedOrdersProductItem = new OrdersProductItem();
        partialUpdatedOrdersProductItem.setId(ordersProductItem.getId());

        restOrdersProductItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdersProductItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdersProductItem))
            )
            .andExpect(status().isOk());

        // Validate the OrdersProductItem in the database
        List<OrdersProductItem> ordersProductItemList = ordersProductItemRepository.findAll();
        assertThat(ordersProductItemList).hasSize(databaseSizeBeforeUpdate);
        OrdersProductItem testOrdersProductItem = ordersProductItemList.get(ordersProductItemList.size() - 1);
        assertThat(testOrdersProductItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void fullUpdateOrdersProductItemWithPatch() throws Exception {
        // Initialize the database
        ordersProductItemRepository.saveAndFlush(ordersProductItem);

        int databaseSizeBeforeUpdate = ordersProductItemRepository.findAll().size();

        // Update the ordersProductItem using partial update
        OrdersProductItem partialUpdatedOrdersProductItem = new OrdersProductItem();
        partialUpdatedOrdersProductItem.setId(ordersProductItem.getId());

        partialUpdatedOrdersProductItem.quantity(UPDATED_QUANTITY);

        restOrdersProductItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrdersProductItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrdersProductItem))
            )
            .andExpect(status().isOk());

        // Validate the OrdersProductItem in the database
        List<OrdersProductItem> ordersProductItemList = ordersProductItemRepository.findAll();
        assertThat(ordersProductItemList).hasSize(databaseSizeBeforeUpdate);
        OrdersProductItem testOrdersProductItem = ordersProductItemList.get(ordersProductItemList.size() - 1);
        assertThat(testOrdersProductItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void patchNonExistingOrdersProductItem() throws Exception {
        int databaseSizeBeforeUpdate = ordersProductItemRepository.findAll().size();
        ordersProductItem.setId(count.incrementAndGet());

        // Create the OrdersProductItem
        OrdersProductItemDTO ordersProductItemDTO = ordersProductItemMapper.toDto(ordersProductItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrdersProductItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ordersProductItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordersProductItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdersProductItem in the database
        List<OrdersProductItem> ordersProductItemList = ordersProductItemRepository.findAll();
        assertThat(ordersProductItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrdersProductItem() throws Exception {
        int databaseSizeBeforeUpdate = ordersProductItemRepository.findAll().size();
        ordersProductItem.setId(count.incrementAndGet());

        // Create the OrdersProductItem
        OrdersProductItemDTO ordersProductItemDTO = ordersProductItemMapper.toDto(ordersProductItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersProductItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordersProductItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrdersProductItem in the database
        List<OrdersProductItem> ordersProductItemList = ordersProductItemRepository.findAll();
        assertThat(ordersProductItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrdersProductItem() throws Exception {
        int databaseSizeBeforeUpdate = ordersProductItemRepository.findAll().size();
        ordersProductItem.setId(count.incrementAndGet());

        // Create the OrdersProductItem
        OrdersProductItemDTO ordersProductItemDTO = ordersProductItemMapper.toDto(ordersProductItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrdersProductItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ordersProductItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrdersProductItem in the database
        List<OrdersProductItem> ordersProductItemList = ordersProductItemRepository.findAll();
        assertThat(ordersProductItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrdersProductItem() throws Exception {
        // Initialize the database
        ordersProductItemRepository.saveAndFlush(ordersProductItem);

        int databaseSizeBeforeDelete = ordersProductItemRepository.findAll().size();

        // Delete the ordersProductItem
        restOrdersProductItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, ordersProductItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrdersProductItem> ordersProductItemList = ordersProductItemRepository.findAll();
        assertThat(ordersProductItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
