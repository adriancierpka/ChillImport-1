package com.chillimport.server.controller;

import com.chillimport.server.FileManager;
import com.chillimport.server.builders.ObservedPropertyBuilder;
import com.chillimport.server.utility.SensorThingsServiceFactory;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.dao.ObservedPropertyDao;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.model.ObservedProperty;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.query.Query;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ObservedPropertyControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private String observedPropertyString;

    @Mock
    private SensorThingsServiceFactory sensorThingsServiceFactory;

    @InjectMocks
    private ObservedPropertyController observedPropertyController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(observedPropertyController).build();

        observedPropertyString = "{\"name\":\"TestObsProp\",\"description\":\"testing obs props\",\"definition\":\"https://www.test.de\"}";
    }

    @Test
    public void create() throws Exception {

        when(sensorThingsServiceFactory.build()).thenReturn(new SensorThingsService(FileManager.getServerURL()));

        this.mvc.perform(post("/observedProperty/create").contentType("application/json").content(observedPropertyString)).andDo(print()).andExpect(
                status().isOk());
    }

    @Test
    public void getSingle() throws Exception {

        SensorThingsService sensorThingsServiceMock = mock(SensorThingsService.class);
        ObservedPropertyDao oPropDaoMock = mock(ObservedPropertyDao.class);

        ObservedPropertyBuilder builder = new ObservedPropertyBuilder();
        builder.withName("MockOP");
        builder.withDescription("descr");
        builder.withDefinition("def");
        builder.withId(1l);
        ObservedProperty oPropMock = builder.build();

        when(oPropDaoMock.find(1)).thenReturn(oPropMock);
        when(sensorThingsServiceMock.observedProperties()).thenReturn(oPropDaoMock);

        when(sensorThingsServiceFactory.build()).thenReturn(sensorThingsServiceMock);

        MvcResult result = this.mvc.perform(get("/observedProperty/single").param("id", "1")).andDo(print()).andExpect(status().isOk()).andReturn();

        Assert.assertEquals(result.getResponse().getContentAsString(),
                            "{\"name\":\"MockOP\",\"description\":\"descr\",\"frostId\":\"1\",\"definition\":\"def\"}");
    }

    @Test
    public void getAll() throws Exception {

        SensorThingsService sensorThingsServiceMock = mock(SensorThingsService.class);
        ObservedPropertyDao oPropDao = mock(ObservedPropertyDao.class);
        Query<ObservedProperty> queryMock = mock(Query.class);

        ObservedPropertyBuilder builder = new ObservedPropertyBuilder();
        builder.withName("MockOP");
        builder.withDescription("descr");
        builder.withDefinition("def");
        builder.withId(1l);
        ObservedProperty oPropMock1 = builder.build();
        builder.withId(2l);
        ObservedProperty oPropMock2 = builder.build();

        EntityList<ObservedProperty> oPropMocks = new EntityList<>(EntityType.OBSERVED_PROPERTIES);
        oPropMocks.add(oPropMock1);
        oPropMocks.add(oPropMock2);
        when(queryMock.list()).thenReturn(oPropMocks);
        when(oPropDao.query()).thenReturn(queryMock);
        when(sensorThingsServiceMock.observedProperties()).thenReturn(oPropDao);
        when(sensorThingsServiceFactory.build()).thenReturn(sensorThingsServiceMock);

        MvcResult result = this.mvc.perform(get("/observedProperty/all")).andDo(print()).andExpect(status().isOk()).andReturn();

        Assert.assertEquals(result.getResponse().getContentAsString(),
                            "[{\"name\":\"MockOP\",\"description\":\"descr\",\"frostId\":\"1\",\"definition\":\"def\"},{\"name\":\"MockOP\",\"description\":\"descr\",\"frostId\":\"2\",\"definition\":\"def\"}]");
    }


    @Test
    public void malformedURLEx() throws Exception {

        when(sensorThingsServiceFactory.build()).thenThrow(MalformedURLException.class);


        this.mvc.perform(post("/observedProperty/create").contentType("application/json").content(observedPropertyString)).andDo(print()).andExpect(
                status().isNotFound()).andExpect(content().string(
                "Malformed URL for Frost-Server."));
        this.mvc.perform(get("/observedProperty/single").param("id", "1")).andDo(print()).andExpect(status().isNotFound()).andExpect(content().string(
                "Malformed URL for Frost-Server."));
        this.mvc.perform(get("/observedProperty/all")).andDo(print()).andExpect(status().isNotFound()).andExpect(content().string(
                "Malformed URL for Frost-Server."));

    }


    @Test
    public void uRISyntaxEx() throws Exception {

        when(sensorThingsServiceFactory.build()).thenThrow(URISyntaxException.class);

        this.mvc.perform(post("/observedProperty/create").contentType("application/json").content(observedPropertyString)).andDo(print()).andExpect(
                status().isNotFound()).andExpect(content().string(
                "Wrong URI for Frost-Server."));
        this.mvc.perform(get("/observedProperty/single").param("id", "1")).andDo(print()).andExpect(status().isNotFound()).andExpect(content().string(
                "Wrong URI for Frost-Server."));
        this.mvc.perform(get("/observedProperty/all")).andDo(print()).andExpect(status().isNotFound()).andExpect(content().string(
                "Wrong URI for Frost-Server."));

    }

    @Test
    public void serviceFailureEx() throws Exception {
        SensorThingsService sensorThingsServiceMock = mock(SensorThingsService.class);
        ObservedPropertyDao observedPropertyDaoMock = mock(ObservedPropertyDao.class);
        Query<ObservedProperty> queryMock = mock(Query.class);

        when(observedPropertyDaoMock.find(1)).thenThrow(ServiceFailureException.class);
        when(queryMock.list()).thenThrow(ServiceFailureException.class);
        when(observedPropertyDaoMock.query()).thenReturn(queryMock);
        when(sensorThingsServiceMock.observedProperties()).thenReturn(observedPropertyDaoMock);
        when(sensorThingsServiceFactory.build()).thenReturn(sensorThingsServiceMock);

        this.mvc.perform(get("/observedProperty/single").param("id",
                                                               "1")).andDo(print()).andExpect(status().isInternalServerError()).andExpect(content().string(
                "Failed to find ObservedProperty on server."));
        this.mvc.perform(get("/observedProperty/all")).andDo(print()).andExpect(status().isInternalServerError()).andExpect(content().string(
                "Failed to find ObservedProperties on server."));
    }
}