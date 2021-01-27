package tourGuide.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tourGuide.helper.InternalTestHelper;
import tourGuide.service.InternalTestService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.webclient.GpsUtilWebClient;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocationControllerITest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webContext;

    @Before
    public void setupMockmvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
    }

    @Test
    public void getLocationITest() throws Exception {
        InternalTestHelper internalTestHelper = new InternalTestHelper();
        internalTestHelper.setInternalUserNumber(1);

        String userName = "internalUser0";

        MvcResult result = mockMvc.perform(get("/getLocation")
                .param("userName", userName))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertTrue(result.getResponse().getContentAsString().contains("latitude"));
        Assert.assertTrue(result.getRequest().getParameter("userName").contains(userName));
    }

    @Test
    public void getNearbyAttractionsITest() throws Exception {
        InternalTestHelper internalTestHelper = new InternalTestHelper();
        internalTestHelper.setInternalUserNumber(1);

        String userName = "internalUser0";

        MvcResult result = mockMvc.perform(get("/getNearbyAttractions")
                .param("userName", userName))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertTrue(result.getResponse().getContentAsString().contains("latitude"));
        Assert.assertTrue(result.getRequest().getParameter("userName").contains(userName));
    }

    @Test
    public void getAllCurrentLocationsITest() throws Exception {
        InternalTestHelper internalTestHelper = new InternalTestHelper();
        internalTestHelper.setInternalUserNumber(1);

        String userName = "internalUser0";

        MvcResult result = mockMvc.perform(get("/getAllCurrentLocations")
                .param("userName", userName))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertTrue(result.getResponse().getContentAsString().contains("latitude"));
        Assert.assertTrue(result.getRequest().getParameter("userName").contains(userName));
    }
}
