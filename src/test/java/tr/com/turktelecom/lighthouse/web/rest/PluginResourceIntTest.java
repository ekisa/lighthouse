package tr.com.turktelecom.lighthouse.web.rest;

import tr.com.turktelecom.lighthouse.Application;
import tr.com.turktelecom.lighthouse.domain.Plugin;
import tr.com.turktelecom.lighthouse.repository.PluginRepository;
//import tr.com.turktelecom.lighthouse.repository.search.PluginSearchRepository;

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
 * Test class for the PluginResource REST controller.
 *
 * @see PluginResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PluginResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private PluginRepository pluginRepository;

//    @Inject private PluginSearchRepository pluginSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPluginMockMvc;

    private Plugin plugin;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PluginResource pluginResource = new PluginResource();
//        ReflectionTestUtils.setField(pluginResource, "pluginSearchRepository", pluginSearchRepository);
        ReflectionTestUtils.setField(pluginResource, "pluginRepository", pluginRepository);
        this.restPluginMockMvc = MockMvcBuilders.standaloneSetup(pluginResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        plugin = new Plugin();
        plugin.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPlugin() throws Exception {
        int databaseSizeBeforeCreate = pluginRepository.findAll().size();

        // Create the Plugin

        restPluginMockMvc.perform(post("/api/plugins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(plugin)))
                .andExpect(status().isCreated());

        // Validate the Plugin in the database
        List<Plugin> plugins = pluginRepository.findAll();
        assertThat(plugins).hasSize(databaseSizeBeforeCreate + 1);
        Plugin testPlugin = plugins.get(plugins.size() - 1);
        assertThat(testPlugin.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllPlugins() throws Exception {
        // Initialize the database
        pluginRepository.saveAndFlush(plugin);

        // Get all the plugins
        restPluginMockMvc.perform(get("/api/plugins?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(plugin.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getPlugin() throws Exception {
        // Initialize the database
        pluginRepository.saveAndFlush(plugin);

        // Get the plugin
        restPluginMockMvc.perform(get("/api/plugins/{id}", plugin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(plugin.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlugin() throws Exception {
        // Get the plugin
        restPluginMockMvc.perform(get("/api/plugins/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlugin() throws Exception {
        // Initialize the database
        pluginRepository.saveAndFlush(plugin);

		int databaseSizeBeforeUpdate = pluginRepository.findAll().size();

        // Update the plugin
        plugin.setName(UPDATED_NAME);

        restPluginMockMvc.perform(put("/api/plugins")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(plugin)))
                .andExpect(status().isOk());

        // Validate the Plugin in the database
        List<Plugin> plugins = pluginRepository.findAll();
        assertThat(plugins).hasSize(databaseSizeBeforeUpdate);
        Plugin testPlugin = plugins.get(plugins.size() - 1);
        assertThat(testPlugin.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deletePlugin() throws Exception {
        // Initialize the database
        pluginRepository.saveAndFlush(plugin);

		int databaseSizeBeforeDelete = pluginRepository.findAll().size();

        // Get the plugin
        restPluginMockMvc.perform(delete("/api/plugins/{id}", plugin.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Plugin> plugins = pluginRepository.findAll();
        assertThat(plugins).hasSize(databaseSizeBeforeDelete - 1);
    }
}
