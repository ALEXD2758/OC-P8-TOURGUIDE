package tourGuide.webclient;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tourGuide.model.trip.Provider;

import java.util.List;
import java.util.UUID;

public class TripPricerWebClient {

    // Declare the base url
    private final String BASE_URL = "http://localhost:8083";
    // Declare the path
    private final String PATH = "/getPrice";
    //Declare the apiKey name to use in the request of the Rest Template Web Client
    private final String API_KEY = "?apiKey=";
    //Declare the AttractionId name to use in the request of the Rest Template Web Client
    private final String ATTRACTION_ID = "&attractionId=";
    //Declare the adults name to use in the request of the Rest Template Web Client
    private final String ADULTS = "&adults=";
    //Declare the children name to use in the request of the Rest Template Web Client
    private final String CHILDREN = "&children=";
    //Declare the nightsStay name to use in the request of the Rest Template Web Client
    private final String NIGHT_STAY = "&nightsStay=";
    //Declare the rewardsPoints name to use in the request of the Rest Template Web Client
    private final String REWARDS_POINTS = "&rewardsPoints=";


    //Define the trip Pricer URI
    private final String getPriceTripPricerUri() {
        return BASE_URL + PATH;
    }

    public List<Provider> getPriceWebClient(String apiKey, UUID attractionId, int adults, int children, int nightsStay,
                                            int rewardsPoints) {
        RestTemplate restTemplate = new RestTemplate();
        List<Provider> listProvider;

        ResponseEntity<List<Provider>> result =
                restTemplate.exchange(getPriceTripPricerUri()
                                + API_KEY + apiKey
                                + ATTRACTION_ID + attractionId
                                + ADULTS + adults
                                + CHILDREN + children
                                + NIGHT_STAY + nightsStay
                                + REWARDS_POINTS + rewardsPoints,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Provider>>() {
                        });
        listProvider= result.getBody();
        return listProvider;
    }
}