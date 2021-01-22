package tourGuide.webclient;

import tourGuide.model.location.Attraction;
import tourGuide.model.location.VisitedLocation;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GpsUtilWebClient {

    // Declare the base url
    private final String BASE_URL = "http://localhost:8081";
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
        VisitedLocation visitedLocation;

        ResponseEntity<List<Attraction>> result  =
                restTemplate.getForEntity(getAllAttractionsGpsUtilUri()
                        , null, new ParameterizedTypeReference<List<Attraction>>(){});
        List<Attraction> attractionList= result.getBody();
        return attractionList;
    }

  /*  public VisitedLocation getUserLocationWebClient(UUID userId) {
        Mono<VisitedLocation> getUserLocationMono= WebClient.create()
                .get()
                .uri(getUserLocationGpsUtilUri + "?userId=" + userId)
                .retrieve()
                .bodyToMono(VisitedLocation.class);
        return getUserLocationMono.block();
    }

    public List<Attraction> getAllAttractionsWebClient() {
        Flux<Attraction> getAllAttractionsFlux= WebClient.create()
                .get()
                .uri(pathGetAllAtractions )
                .retrieve()
                .bodyToFlux(Attraction.class);
        List<Attraction> attractions = getAllAttractionsFlux.collectList().block();
        return attractions;
    }
    */
}
