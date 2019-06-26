package com.chillimport.server.controller;

import com.chillimport.server.FileManager;
import com.chillimport.server.builders.SensorBuilder;
import com.chillimport.server.utility.SensorThingsServiceFactory;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.dao.SensorDao;
import de.fraunhofer.iosb.ilt.sta.model.EntityType;
import de.fraunhofer.iosb.ilt.sta.model.Sensor;
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
public class SensorControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private String sensorString;

    @Mock
    private SensorThingsServiceFactory sensorThingsServiceFactory;

    @InjectMocks
    private SensorController sensorController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(sensorController).build();

        sensorString = "{\"name\":\"KITSensorMax\",\"description\":\"Max Sensor am KIT\",\"encoding_TYPE\":\"application/json\",\"metadata\":\"\"}";

    }


    @Test
    public void create() throws Exception {

        when(sensorThingsServiceFactory.build()).thenReturn(new SensorThingsService(FileManager.getServerURL()));

        this.mvc.perform(post("/sensor/create").contentType("application/json").content(sensorString)).andDo(print()).andExpect(status().isOk());
    }


    @Test
    public void getSingle() throws Exception {

        SensorThingsService sensorThingsServiceMock = mock(SensorThingsService.class);
        SensorDao sensorDaoMock = mock(SensorDao.class);

        SensorBuilder builder = new SensorBuilder();
        builder.withName("MockSensor");
        builder.withDescription("descr");
        builder.withEncodingType("encType");
        builder.withMetadata("metadata");
        builder.withId(1l);
        Sensor sensorMock = builder.build();

        when(sensorDaoMock.find(1)).thenReturn(sensorMock);
        when(sensorThingsServiceMock.sensors()).thenReturn(sensorDaoMock);

        when(sensorThingsServiceFactory.build()).thenReturn(sensorThingsServiceMock);


        MvcResult result = this.mvc.perform(get("/sensor/single").param("id", "1")).andDo(print()).andExpect(status().isOk()).andReturn();

        Assert.assertEquals(result.getResponse().getContentAsString(),
                            "{\"name\":\"MockSensor\",\"description\":\"descr\",\"frostId\":\"1\",\"encoding_TYPE\":\"encType\",\"metadata\":\"metadata\"}");


    }

    @Test
    public void getAll() throws Exception {

        SensorThingsService sensorThingsServiceMock = mock(SensorThingsService.class);
        SensorDao sensorDaoMock = mock(SensorDao.class);
        Query<Sensor> queryMock = mock(Query.class);

        SensorBuilder builder = new SensorBuilder();
        builder.withName("MockSensor");
        builder.withDescription("descr");
        builder.withEncodingType("encType");
        builder.withMetadata("metadata");
        builder.withId(1l);
        Sensor sensorMock1 = builder.build();
        builder.withId(2l);
        Sensor sensorMock2 = builder.build();

        EntityList<Sensor> sensorMocks = new EntityList<>(EntityType.SENSOR);
        sensorMocks.add(sensorMock1);
        sensorMocks.add(sensorMock2);
        when(queryMock.list()).thenReturn(sensorMocks);
        when(sensorDaoMock.query()).thenReturn(queryMock);

        when(sensorThingsServiceMock.sensors()).thenReturn(sensorDaoMock);
        when(sensorThingsServiceFactory.build()).thenReturn(sensorThingsServiceMock);

        MvcResult result = this.mvc.perform(get("/sensor/all")).andDo(print()).andExpect(status().isOk()).andReturn();

        Assert.assertEquals(result.getResponse().getContentAsString(),
                            "[{\"name\":\"MockSensor\",\"description\":\"descr\",\"frostId\":\"1\",\"encoding_TYPE\":\"encType\",\"metadata\":\"metadata\"},{\"name\":\"MockSensor\",\"description\":\"descr\",\"frostId\":\"2\",\"encoding_TYPE\":\"encType\",\"metadata\":\"metadata\"}]");
    }

    @Test
    public void malformedURLEx() throws Exception {

        when(sensorThingsServiceFactory.build()).thenThrow(MalformedURLException.class);


        this.mvc.perform(post("/sensor/create").contentType("application/json").content(sensorString)).andDo(print()).andExpect(status().isNotFound()).andExpect(
                content().string(
                        "Malformed URL for Frost-Server."));
        this.mvc.perform(get("/sensor/single").param("id", "1")).andDo(print()).andExpect(status().isNotFound()).andExpect(content().string(
                "Malformed URL for Frost-Server."));
        this.mvc.perform(get("/sensor/all")).andDo(print()).andExpect(status().isNotFound()).andExpect(content().string(
                "Malformed URL for Frost-Server."));

    }

    @Test
    public void uRISyntaxEx() throws Exception {

        when(sensorThingsServiceFactory.build()).thenThrow(URISyntaxException.class);

        this.mvc.perform(post("/sensor/create").contentType("application/json").content(sensorString)).andDo(print()).andExpect(status().isNotFound()).andExpect(
                content().string(
                        "Wrong URI for Frost-Server."));
        this.mvc.perform(get("/sensor/single").param("id", "1")).andDo(print()).andExpect(status().isNotFound()).andExpect(content().string(
                "Wrong URI for Frost-Server."));
        this.mvc.perform(get("/sensor/all")).andDo(print()).andExpect(status().isNotFound()).andExpect(content().string("Wrong URI for Frost-Server."));

    }

    @Test
    public void serviceFailureEx() throws Exception {
        SensorThingsService sensorThingsServiceMock = mock(SensorThingsService.class);
        SensorDao sensorDaoMock = mock(SensorDao.class);
        Query<Sensor> queryMock = mock(Query.class);

        when(sensorDaoMock.find(1)).thenThrow(ServiceFailureException.class);
        when(queryMock.list()).thenThrow(ServiceFailureException.class);
        when(sensorDaoMock.query()).thenReturn(queryMock);
        when(sensorThingsServiceMock.sensors()).thenReturn(sensorDaoMock);
        when(sensorThingsServiceFactory.build()).thenReturn(sensorThingsServiceMock);

        this.mvc.perform(get("/sensor/single").param("id",
                                                     "1")).andDo(print()).andExpect(status().isInternalServerError()).andExpect(content().string(
                "Failed to find Sensor on server."));
        this.mvc.perform(get("/sensor/all")).andDo(print()).andExpect(status().isInternalServerError()).andExpect(content().string(
                "Failed to find Sensors on server."));


    }
}