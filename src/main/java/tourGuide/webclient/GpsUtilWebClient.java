package tourGuide.webclient;

import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class GpsUtilWebClient {

    // Declare the value of the path present in application.properties
    @Value("${gps.util.port")
    private int GPSUTIL_PORT;

    //Declare the variable serverPort to refer to GPS_UTIL_PORT
    private final int serverPort = GPSUTIL_PORT;


        private String baseURL = "localhost:8081";
        private String path = "/getUserLocation";
        private String path1 = "localhost:8081/getUserLocation";

    //Define the gpsUtil URI
    private String getGpsUtilUri() {
        return baseURL + path;
        //return "http://localhost:" + serverPort + "/getUserLocation";
    }

    public VisitedLocation getUserLocationWebClient(UUID userId) {
        Mono<VisitedLocation> getUserLocationFlux= WebClient.create()
                .get()
                .uri(path1 + "?userId=" + userId)
                .retrieve()
                .bodyToMono(VisitedLocation.class);
        return getUserLocationFlux.block();
    }
}
