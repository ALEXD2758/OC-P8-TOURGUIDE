package tourGuide.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.exception.UUIDException;
import tourGuide.service.TourGuideService;

@RestController
public class HomeController {

    private Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	TourGuideService tourGuideService;

    /** HTML GET request that returns a welcome message
     *
     * @return a string message
     */
    @GetMapping("/")
    public String index() {
        logger.debug("Access to / endpoint");
        return "Greetings from TourGuide!";
    }

    /**
     * HTML GET request that starts the tracker
     */
    @GetMapping("/location/startTracker")
    public void startTracker() {
        logger.debug("Access to /location/startTracker endpoint");
        tourGuideService.tracker.startTracking();
    }

    /**
     * HTML GET request that stops the tracker
     */
    @GetMapping("/location/stopTracker")
    public void stopTracker() {
        logger.debug("Access to /location/stopTracker endpoint");
        tourGuideService.tracker.stopTracking();
    }
}