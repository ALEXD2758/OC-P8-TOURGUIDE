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
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.helper.InternalTestHelper;
import tourGuide.model.UserModel;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerITest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webContext;

    @Before
    public void setupMockmvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
    }

    @Test
    public void putUpdatePreferencesITest() throws Exception {

        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO();
        userPreferencesDTO.setNumberOfAdults(2);
        userPreferencesDTO.setTripDuration(3);
        userPreferencesDTO.setNumberOfChildren(2);
        userPreferencesDTO.setCurrency("USD");

        UUID userUUID = UUID.fromString("987b1312-768d-41e1-90c1-e62da7c93739");
        UserModel userModel = new UserModel(userUUID, "internalUser0", "1243456",
                "internalUser0@Gmail.com");

        InternalTestHelper internalTestHelper = new InternalTestHelper();
        internalTestHelper.setInternalUserNumber(1);

        String userName = "internalUser0";
        String questionBody = "{\n" +
                "\"attractionProximity\": 21447,\n" +
                "\"currency\": \"USD\",\n" +
                "\"lowerPricePoint\": 0.0,\n" +
                "\"highPricePoint\": 300.0,\n" +
                "\"tripDuration\": 1,\n" +
                "\"ticketQuantity\": 1,\n" +
                "\"numberOfAdults\": 1,\n" +
                "\"numberOfChildren\": 0\n" +
                "}";

        MvcResult result = mockMvc.perform(put("/update/Preferences")
                .param("userName", userName)
                .content(questionBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        Assert.assertFalse(userPreferencesDTO.getNumberOfAdults() == userModel.getUserPreferences().getNumberOfAdults());
    }
}