package tourGuide.webclient;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tourGuide.exception.UUIDException;

import java.util.Arrays;
import java.util.UUID;

public class RewardsWebClient {

    // Declare the base url
    private final String BASE_URL = "http://localhost:8082";
    // Declare the path
    private final String PATH = "/getRewardPoints";
    //Declare the AttractionId name to use in the request of the Rest Template Web Client
    private final String ATTRACTION_ID = "?attractionId=";
    //Declare the UserId name to use in the request of the Rest Template Web Client
    private final String USER_ID = "&userId=";


    //Define the rewardsCentral URI
    private final String getRewardsCentralUri() {
      return BASE_URL + PATH;
    }

    public int getRewardPointsWebClient(UUID attractionId, UUID userId) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        int rewardPoints;

        ResponseEntity<Integer> result  =
                restTemplate.getForEntity(getRewardsCentralUri() +
                        ATTRACTION_ID +
                                attractionId +
                        USER_ID +
                                userId
                        ,Integer.class);

        rewardPoints = result.getBody();
        return rewardPoints;
    }
}