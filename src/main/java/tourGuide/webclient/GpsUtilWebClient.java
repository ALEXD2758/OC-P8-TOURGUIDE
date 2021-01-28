package tourGuide.webclient;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import tourGuide.model.location.Attraction;
import tourGuide.model.location.VisitedLocation;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GpsUtilWebClient {

    // Declare the base url (for docker deployment)
    private final String BASE_URL = "http://gps:8081";
    // Declare the base url (for localhost)
    private final String BASE_URL_LOCALHOST = "http://localhost:8081";
    // Declare the path to UserLocation
    private final String PATH_USER_LOCATION = "/getUserLocation";
    // Declare the path to AllAttractions
    private final String PATH_ALL_ATTRACTIONS = "/getAllAttractions";
    //Declare the AttractionId name to use in the request of the Rest Template Web Client
    private final String USER_ID = "?userId=";


    //Define the User Location URI
    private final String getUserLocationGpsUtilUri() {
        return BASE_URL + PATH_USER_LOCATION;
    }

    //Define the All attractions URI
    private final String getAllAttractionsGpsUtilUri() {
        return BASE_URL + PATH_ALL_ATTRACTIONS;
    }

    public VisitedLocation getUserLocationWebClient(UUID userId) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        VisitedLocation visitedLocation;

        ResponseEntity<VisitedLocation> result  =
                restTemplate.getForEntity(getUserLocationGpsUtilUri() +
                                USER_ID +
                                userId
                        ,VisitedLocation.class);
        visitedLocation = result.getBody();
        return visitedLocation;
    }

    public List<Attraction> getAllAttractionsWebClient() {
        RestTemplate restTemplate = new RestTemplate();
        List<Attraction> attractionList;

        ResponseEntity<List<Attraction>> result =
                restTemplate.exchange(getAllAttractionsGpsUtilUri(),
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Attraction>>() {
                        });
        attractionList= result.getBody();
        return attractionList;
    }
}