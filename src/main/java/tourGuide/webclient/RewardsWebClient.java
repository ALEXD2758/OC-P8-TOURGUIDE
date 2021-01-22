package tourGuide.webclient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
        int rewardPoints = 0;

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
     /*   Mono<Integer> getUserLocationMono= WebClient.create()
                .get()
                .uri(pathGetRewardPoints + "?attractionId=" + attractionId + "?userId=" + userId)
                .retrieve()
                .bodyToMono(Integer.class);
        return getUserLocationMono.block();
    */


}
