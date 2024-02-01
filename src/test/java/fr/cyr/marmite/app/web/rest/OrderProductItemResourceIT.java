package fr.cyr.marmite.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.cyr.marmite.app.IntegrationTest;
import fr.cyr.marmite.app.domain.OrderProductItem;
import fr.cyr.marmite.app.repository.OrderProductItemRepository;
import fr.cyr.marmite.app.service.dto.OrderProductItem;
import fr.cyr.marmite.app.service.mapper.OrderProductItemMapper;
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
 * Integration tests for the {@link OrderProductItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrderProductItemResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final String ENTITY_API_URL = "/api/order-product-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderProductItemRepository orderProductItemRepository;

    @Autowired
    private OrderProductItemMapper orderProductItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderProductItemMockMvc;

    private OrderProductItem orderProductItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderProductItem createEntity(EntityManager em) {
        OrderProductItem orderProductItem = new OrderProductItem().quantity(DEFAULT_QUANTITY);
        return orderProductItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderProductItem createUpdatedEntity(EntityManager em) {
        OrderProductItem orderProductItem = new OrderProductItem().quantity(UPDATED_QUANTITY);
        return orderProductItem;
    }

    @BeforeEach
    public void initTest() {
        orderProductItem = createEntity(em);
    }

    @Test
    @Transactional
    void createOrderProductItem() throws Exception {
        int databaseSizeBeforeCreate = orderProductItemRepository.findAll().size();
        // Create the OrderProductItem
        OrderProductItem orderProductItem = orderProductItemMapper.toDto(orderProductItem);
        restOrderProductItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderProductItem))
            )
            .andExpect(status().isCreated());

        // Validate the OrderProductItem in the database
        List<OrderProductItem> orderProductItemList = orderProductItemRepository.findAll();
        assertThat(orderProductItemList).hasSize(databaseSizeBeforeCreate + 1);
        OrderProductItem testOrderProductItem = orderProductItemList.get(orderProductItemList.size() - 1);
        assertThat(testOrderProductItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void createOrderProductItemWithExistingId() throws Exception {
        // Create the OrderProductItem with an existing ID
        orderProductItem.setId(1L);
        OrderProductItem orderProductItem = orderProductItemMapper.toDto(orderProductItem);

        int databaseSizeBeforeCreate = orderProductItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderProductItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderProductItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderProductItem in the database
        List<OrderProductItem> orderProductItemList = orderProductItemRepository.findAll();
        assertThat(orderProductItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOrderProductItems() throws Exception {
        // Initialize the database
        orderProductItemRepository.saveAndFlush(orderProductItem);

        // Get all the orderProductItemList
        restOrderProductItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orderProductItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)));
    }

    @Test
    @Transactional
    void getOrderProductItem() throws Exception {
        // Initialize the database
        orderProductItemRepository.saveAndFlush(orderProductItem);

        // Get the orderProductItem
        restOrderProductItemMockMvc
            .perform(get(ENTITY_API_URL_ID, orderProductItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orderProductItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY));
    }

    @Test
    @Transactional
    void getNonExistingOrderProductItem() throws Exception {
        // Get the orderProductItem
        restOrderProductItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrderProductItem() throws Exception {
        // Initialize the database
        orderProductItemRepository.saveAndFlush(orderProductItem);

        int databaseSizeBeforeUpdate = orderProductItemRepository.findAll().size();

        // Update the orderProductItem
        OrderProductItem updatedOrderProductItem = orderProductItemRepository.findById(orderProductItem.getId()).get();
        // Disconnect from session so that the updates on updatedOrderProductItem are not directly saved in db
        em.detach(updatedOrderProductItem);
        updatedOrderProductItem.quantity(UPDATED_QUANTITY);
        OrderProductItem orderProductItem = orderProductItemMapper.toDto(updatedOrderProductItem);

        restOrderProductItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderProductItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderProductItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrderProductItem in the database
        List<OrderProductItem> orderProductItemList = orderProductItemRepository.findAll();
        assertThat(orderProductItemList).hasSize(databaseSizeBeforeUpdate);
        OrderProductItem testOrderProductItem = orderProductItemList.get(orderProductItemList.size() - 1);
        assertThat(testOrderProductItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void putNonExistingOrderProductItem() throws Exception {
        int databaseSizeBeforeUpdate = orderProductItemRepository.findAll().size();
        orderProductItem.setId(count.incrementAndGet());

        // Create the OrderProductItem
        OrderProductItem orderProductItem = orderProductItemMapper.toDto(orderProductItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderProductItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orderProductItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderProductItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderProductItem in the database
        List<OrderProductItem> orderProductItemList = orderProductItemRepository.findAll();
        assertThat(orderProductItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrderProductItem() throws Exception {
        int databaseSizeBeforeUpdate = orderProductItemRepository.findAll().size();
        orderProductItem.setId(count.incrementAndGet());

        // Create the OrderProductItem
        OrderProductItem orderProductItem = orderProductItemMapper.toDto(orderProductItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderProductItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orderProductItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderProductItem in the database
        List<OrderProductItem> orderProductItemList = orderProductItemRepository.findAll();
        assertThat(orderProductItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrderProductItem() throws Exception {
        int databaseSizeBeforeUpdate = orderProductItemRepository.findAll().size();
        orderProductItem.setId(count.incrementAndGet());

        // Create the OrderProductItem
        OrderProductItem orderProductItem = orderProductItemMapper.toDto(orderProductItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderProductItemMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(orderProductItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderProductItem in the database
        List<OrderProductItem> orderProductItemList = orderProductItemRepository.findAll();
        assertThat(orderProductItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderProductItemWithPatch() throws Exception {
        // Initialize the database
        orderProductItemRepository.saveAndFlush(orderProductItem);

        int databaseSizeBeforeUpdate = orderProductItemRepository.findAll().size();

        // Update the orderProductItem using partial update
        OrderProductItem partialUpdatedOrderProductItem = new OrderProductItem();
        partialUpdatedOrderProductItem.setId(orderProductItem.getId());

        restOrderProductItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderProductItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderProductItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderProductItem in the database
        List<OrderProductItem> orderProductItemList = orderProductItemRepository.findAll();
        assertThat(orderProductItemList).hasSize(databaseSizeBeforeUpdate);
        OrderProductItem testOrderProductItem = orderProductItemList.get(orderProductItemList.size() - 1);
        assertThat(testOrderProductItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void fullUpdateOrderProductItemWithPatch() throws Exception {
        // Initialize the database
        orderProductItemRepository.saveAndFlush(orderProductItem);

        int databaseSizeBeforeUpdate = orderProductItemRepository.findAll().size();

        // Update the orderProductItem using partial update
        OrderProductItem partialUpdatedOrderProductItem = new OrderProductItem();
        partialUpdatedOrderProductItem.setId(orderProductItem.getId());

        partialUpdatedOrderProductItem.quantity(UPDATED_QUANTITY);

        restOrderProductItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrderProductItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderProductItem))
            )
            .andExpect(status().isOk());

        // Validate the OrderProductItem in the database
        List<OrderProductItem> orderProductItemList = orderProductItemRepository.findAll();
        assertThat(orderProductItemList).hasSize(databaseSizeBeforeUpdate);
        OrderProductItem testOrderProductItem = orderProductItemList.get(orderProductItemList.size() - 1);
        assertThat(testOrderProductItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void patchNonExistingOrderProductItem() throws Exception {
        int databaseSizeBeforeUpdate = orderProductItemRepository.findAll().size();
        orderProductItem.setId(count.incrementAndGet());

        // Create the OrderProductItem
        OrderProductItemDTO orderProductItemDTO = orderProductItemMapper.toDto(orderProductItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderProductItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orderProductItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderProductItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderProductItem in the database
        List<OrderProductItem> orderProductItemList = orderProductItemRepository.findAll();
        assertThat(orderProductItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrderProductItem() throws Exception {
        int databaseSizeBeforeUpdate = orderProductItemRepository.findAll().size();
        orderProductItem.setId(count.incrementAndGet());

        // Create the OrderProductItem
        OrderProductItemDTO orderProductItemDTO = orderProductItemMapper.toDto(orderProductItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderProductItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderProductItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrderProductItem in the database
        List<OrderProductItem> orderProductItemList = orderProductItemRepository.findAll();
        assertThat(orderProductItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrderProductItem() throws Exception {
        int databaseSizeBeforeUpdate = orderProductItemRepository.findAll().size();
        orderProductItem.setId(count.incrementAndGet());

        // Create the OrderProductItem
        OrderProductItemDTO orderProductItemDTO = orderProductItemMapper.toDto(orderProductItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderProductItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orderProductItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrderProductItem in the database
        List<OrderProductItem> orderProductItemList = orderProductItemRepository.findAll();
        assertThat(orderProductItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrderProductItem() throws Exception {
        // Initialize the database
        orderProductItemRepository.saveAndFlush(orderProductItem);

        int databaseSizeBeforeDelete = orderProductItemRepository.findAll().size();

        // Delete the orderProductItem
        restOrderProductItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, orderProductItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OrderProductItem> orderProductItemList = orderProductItemRepository.findAll();
        assertThat(orderProductItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
