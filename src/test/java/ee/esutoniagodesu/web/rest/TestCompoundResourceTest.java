package ee.esutoniagodesu.web.rest;

import ee.esutoniagodesu.bean.ProjectDAO;
import ee.esutoniagodesu.pojo.test.compound.TestCompoundSubmitDTO;
import ee.esutoniagodesu.pojo.test.compound.TestCompoundSubmitDefaults;
import ee.esutoniagodesu.repository.project.*;
import ee.esutoniagodesu.service.TestCompoundService;
import ee.esutoniagodesu.web.rest.test.TestCompoundResource;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the StatsResource REST controller.
 *
 * @see ee.esutoniagodesu.web.rest.StatsResource
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCompoundResourceTest extends WebappTestEnvironment {

    private static final String USERNAME = "admin";

    //------------------------------ spring ------------------------------

    @Inject
    private ProjectDAO dao;
    @Inject
    private ReportDB reportDB;
    @Inject
    private JMDictDB jmDictDB;
    @Inject
    private JMDictEnDB jmDictEnDB;
    @Inject
    private KanjiDB kanjiDB;
    @Inject
    private CoreDB coreDB;

    @Inject
    private TestCompoundService service;

    @PostConstruct
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(service, "dao", dao);
        ReflectionTestUtils.setField(service, "reportDB", reportDB);
        ReflectionTestUtils.setField(service, "jmDictDB", jmDictDB);
        ReflectionTestUtils.setField(service, "jmDictEnDB", jmDictEnDB);
        ReflectionTestUtils.setField(service, "kanjiDB", kanjiDB);
        ReflectionTestUtils.setField(service, "coreDB", coreDB);

        TestCompoundResource resource = new TestCompoundResource();
        ReflectionTestUtils.setField(resource, "service", service);

        mockMvc = MockMvcBuilders.standaloneSetup(resource).build();
        setSession(USERNAME);
    }

    @Before
    public void initTest() {

    }

    /**
     * Saadab kõik eeltäidetud vormid testigeneratorile läbi http.
     */
    @Test
    public void t1_submit_defaults() throws Exception {
        //service.submit(dto, null);
        submit_dto(TestCompoundSubmitDefaults.getIloHeisig6());
        submit_dto(TestCompoundSubmitDefaults.getCore6KHeisig6());
        submit_dto(TestCompoundSubmitDefaults.getCore10KJLPT());
    }

    private void submit_dto(TestCompoundSubmitDTO dto) throws Exception {
        mockMvc.perform(
            post(TestCompoundResource.BASE_URL + "/submit")
                .with(request -> {
                    request.setRemoteUser(USERNAME);
                    return request;
                })
                .session(session)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto)))
            .andExpect(status().isOk());

    }
}