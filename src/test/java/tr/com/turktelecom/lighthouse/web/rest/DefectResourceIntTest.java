package tr.com.turktelecom.lighthouse.web.rest;

import tr.com.turktelecom.lighthouse.Application;
import tr.com.turktelecom.lighthouse.domain.Defect;
import tr.com.turktelecom.lighthouse.repository.DefectRepository;
import tr.com.turktelecom.lighthouse.repository.search.DefectSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DefectResource REST controller.
 *
 * @see DefectResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DefectResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_EXPLANATION = "AAAAA";
    private static final String UPDATED_EXPLANATION = "BBBBB";
    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    @Inject
    private DefectRepository defectRepository;

    @Inject
    private DefectSearchRepository defectSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDefectMockMvc;

    private Defect defect;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DefectResource defectResource = new DefectResource();
        ReflectionTestUtils.setField(defectResource, "defectSearchRepository", defectSearchRepository);
        ReflectionTestUtils.setField(defectResource, "defectRepository", defectRepository);
        this.restDefectMockMvc = MockMvcBuilders.standaloneSetup(defectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        defect = new Defect();
        defect.setTitle(DEFAULT_TITLE);
        defect.setExplanation(DEFAULT_EXPLANATION);
        defect.setCode(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createDefect() throws Exception {
        int databaseSizeBeforeCreate = defectRepository.findAll().size();

        // Create the Defect

        restDefectMockMvc.perform(post("/api/defects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(defect)))
                .andExpect(status().isCreated());

        // Validate the Defect in the database
        List<Defect> defects = defectRepository.findAll();
        assertThat(defects).hasSize(databaseSizeBeforeCreate + 1);
        Defect testDefect = defects.get(defects.size() - 1);
        assertThat(testDefect.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testDefect.getExplanation()).isEqualTo(DEFAULT_EXPLANATION);
        assertThat(testDefect.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void getAllDefects() throws Exception {
        // Initialize the database
        defectRepository.saveAndFlush(defect);

        // Get all the defects
        restDefectMockMvc.perform(get("/api/defects?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(defect.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION.toString())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getDefect() throws Exception {
        // Initialize the database
        defectRepository.saveAndFlush(defect);

        // Get the defect
        restDefectMockMvc.perform(get("/api/defects/{id}", defect.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(defect.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.explanation").value(DEFAULT_EXPLANATION.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDefect() throws Exception {
        // Get the defect
        restDefectMockMvc.perform(get("/api/defects/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDefect() throws Exception {
        // Initialize the database
        defectRepository.saveAndFlush(defect);

		int databaseSizeBeforeUpdate = defectRepository.findAll().size();

        // Update the defect
        defect.setTitle(UPDATED_TITLE);
        defect.setExplanation(UPDATED_EXPLANATION);
        defect.setCode(UPDATED_CODE);

        restDefectMockMvc.perform(put("/api/defects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(defect)))
                .andExpect(status().isOk());

        // Validate the Defect in the database
        List<Defect> defects = defectRepository.findAll();
        assertThat(defects).hasSize(databaseSizeBeforeUpdate);
        Defect testDefect = defects.get(defects.size() - 1);
        assertThat(testDefect.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testDefect.getExplanation()).isEqualTo(UPDATED_EXPLANATION);
        assertThat(testDefect.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void deleteDefect() throws Exception {
        // Initialize the database
        defectRepository.saveAndFlush(defect);

		int databaseSizeBeforeDelete = defectRepository.findAll().size();

        // Get the defect
        restDefectMockMvc.perform(delete("/api/defects/{id}", defect.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Defect> defects = defectRepository.findAll();
        assertThat(defects).hasSize(databaseSizeBeforeDelete - 1);
    }
}
