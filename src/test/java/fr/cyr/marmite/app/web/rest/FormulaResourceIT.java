package fr.cyr.marmite.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.cyr.marmite.app.IntegrationTest;
import fr.cyr.marmite.app.domain.Formula;
import fr.cyr.marmite.app.repository.FormulaRepository;
import fr.cyr.marmite.app.service.dto.Formula;
import fr.cyr.marmite.app.service.mapper.FormulaMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FormulaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormulaResourceIT {

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_IMAGE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/formulas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormulaRepository formulaRepository;

    @Autowired
    private FormulaMapper formulaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormulaMockMvc;

    private Formula formula;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formula createEntity(EntityManager em) {
        Formula formula = new Formula()
            .price(DEFAULT_PRICE)
            .name(DEFAULT_NAME)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE)
            .imageType(DEFAULT_IMAGE_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .createdDate(DEFAULT_CREATED_DATE)
            .updatedDate(DEFAULT_UPDATED_DATE);
        return formula;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Formula createUpdatedEntity(EntityManager em) {
        Formula formula = new Formula()
            .price(UPDATED_PRICE)
            .name(UPDATED_NAME)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .imageType(UPDATED_IMAGE_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        return formula;
    }

    @BeforeEach
    public void initTest() {
        formula = createEntity(em);
    }

    @Test
    @Transactional
    void createFormula() throws Exception {
        int databaseSizeBeforeCreate = formulaRepository.findAll().size();
        // Create the Formula
        Formula formula = formulaMapper.toDto(formula);
        restFormulaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formula)))
            .andExpect(status().isCreated());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeCreate + 1);
        Formula testFormula = formulaList.get(formulaList.size() - 1);
        assertThat(testFormula.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testFormula.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFormula.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testFormula.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
        assertThat(testFormula.getImageType()).isEqualTo(DEFAULT_IMAGE_TYPE);
        assertThat(testFormula.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFormula.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testFormula.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    void createFormulaWithExistingId() throws Exception {
        // Create the Formula with an existing ID
        formula.setId(1L);
        Formula formula = formulaMapper.toDto(formula);

        int databaseSizeBeforeCreate = formulaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormulaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formula)))
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = formulaRepository.findAll().size();
        // set the field null
        formula.setPrice(null);

        // Create the Formula, which fails.
        Formula formula = formulaMapper.toDto(formula);

        restFormulaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formula)))
            .andExpect(status().isBadRequest());

        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFormulas() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get all the formulaList
        restFormulaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formula.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))))
            .andExpect(jsonPath("$.[*].imageType").value(hasItem(DEFAULT_IMAGE_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getFormula() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        // Get the formula
        restFormulaMockMvc
            .perform(get(ENTITY_API_URL_ID, formula.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formula.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)))
            .andExpect(jsonPath("$.imageType").value(DEFAULT_IMAGE_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFormula() throws Exception {
        // Get the formula
        restFormulaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormula() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();

        // Update the formula
        Formula updatedFormula = formulaRepository.findById(formula.getId()).get();
        // Disconnect from session so that the updates on updatedFormula are not directly saved in db
        em.detach(updatedFormula);
        updatedFormula
            .price(UPDATED_PRICE)
            .name(UPDATED_NAME)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .imageType(UPDATED_IMAGE_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);
        Formula formula = formulaMapper.toDto(updatedFormula);

        restFormulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formulaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formulaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
        Formula testFormula = formulaList.get(formulaList.size() - 1);
        assertThat(testFormula.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testFormula.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFormula.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testFormula.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testFormula.getImageType()).isEqualTo(UPDATED_IMAGE_TYPE);
        assertThat(testFormula.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFormula.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFormula.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingFormula() throws Exception {
        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();
        formula.setId(count.incrementAndGet());

        // Create the Formula
        Formula formula = formulaMapper.toDto(formula);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formula.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormula() throws Exception {
        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();
        formula.setId(count.incrementAndGet());

        // Create the Formula
        Formula formula = formulaMapper.toDto(formula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formula))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormula() throws Exception {
        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();
        formula.setId(count.incrementAndGet());

        // Create the Formula
        Formula formula = formulaMapper.toDto(formula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(formula)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormulaWithPatch() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();

        // Update the formula using partial update
        Formula partialUpdatedFormula = new Formula();
        partialUpdatedFormula.setId(formula.getId());

        partialUpdatedFormula
            .price(UPDATED_PRICE)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .createdDate(UPDATED_CREATED_DATE);

        restFormulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormula.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormula))
            )
            .andExpect(status().isOk());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
        Formula testFormula = formulaList.get(formulaList.size() - 1);
        assertThat(testFormula.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testFormula.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFormula.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testFormula.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testFormula.getImageType()).isEqualTo(DEFAULT_IMAGE_TYPE);
        assertThat(testFormula.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFormula.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFormula.getUpdatedDate()).isEqualTo(DEFAULT_UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFormulaWithPatch() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();

        // Update the formula using partial update
        Formula partialUpdatedFormula = new Formula();
        partialUpdatedFormula.setId(formula.getId());

        partialUpdatedFormula
            .price(UPDATED_PRICE)
            .name(UPDATED_NAME)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE)
            .imageType(UPDATED_IMAGE_TYPE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .updatedDate(UPDATED_UPDATED_DATE);

        restFormulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormula.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormula))
            )
            .andExpect(status().isOk());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
        Formula testFormula = formulaList.get(formulaList.size() - 1);
        assertThat(testFormula.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testFormula.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFormula.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testFormula.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
        assertThat(testFormula.getImageType()).isEqualTo(UPDATED_IMAGE_TYPE);
        assertThat(testFormula.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFormula.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testFormula.getUpdatedDate()).isEqualTo(UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingFormula() throws Exception {
        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();
        formula.setId(count.incrementAndGet());

        // Create the Formula
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formulaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formulaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormula() throws Exception {
        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();
        formula.setId(count.incrementAndGet());

        // Create the Formula
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formulaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormula() throws Exception {
        int databaseSizeBeforeUpdate = formulaRepository.findAll().size();
        formula.setId(count.incrementAndGet());

        // Create the Formula
        FormulaDTO formulaDTO = formulaMapper.toDto(formula);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormulaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(formulaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Formula in the database
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormula() throws Exception {
        // Initialize the database
        formulaRepository.saveAndFlush(formula);

        int databaseSizeBeforeDelete = formulaRepository.findAll().size();

        // Delete the formula
        restFormulaMockMvc
            .perform(delete(ENTITY_API_URL_ID, formula.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Formula> formulaList = formulaRepository.findAll();
        assertThat(formulaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
