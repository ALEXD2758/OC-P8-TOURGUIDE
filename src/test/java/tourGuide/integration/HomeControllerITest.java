package tourGuide.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tourGuide.service.InternalTestService;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.webclient.GpsUtilWebClient;
import tourGuide.webclient.RewardsWebClient;
import tourGuide.webclient.TripPricerWebClient;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HomeControllerITest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webContext;

    @Before
    public void setupMockmvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
    }

    @Test
    public void test1_getIndexITest() throws Exception {
        String response = "Greetings from TourGuide!";
        MvcResult result = mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertTrue(result.getResponse().getContentAsString().contains(response));
    }

    @Test
    public void test2_getStartTrackerITest() throws Exception {
        InternalTestService internalTestService = new InternalTestService();
        GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
        TripPricerWebClient tripPricerWebClient = new TripPricerWebClient();
        RewardsWebClient rewardsWebClient = new RewardsWebClient();
        RewardsService rewardsService = new RewardsService(gpsUtilWebClient, rewardsWebClient);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, internalTestService,
                gpsUtilWebClient, tripPricerWebClient);

        tourGuideService.tracker.startTracking();

        mockMvc.perform(get("/location/startTracker"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void test3_getStopTrackerITest() throws Exception {
        InternalTestService internalTestService = new InternalTestService();
        GpsUtilWebClient gpsUtilWebClient = new GpsUtilWebClient();
        TripPricerWebClient tripPricerWebClient = new TripPricerWebClient();
        RewardsWebClient rewardsWebClient = new RewardsWebClient();
        RewardsService rewardsService = new RewardsService(gpsUtilWebClient, rewardsWebClient);
        TourGuideService tourGuideService = new TourGuideService(rewardsService, internalTestService,
                gpsUtilWebClient, tripPricerWebClient);

        tourGuideService.tracker.stopTracking();

        mockMvc.perform(get("/location/stopTracker"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}