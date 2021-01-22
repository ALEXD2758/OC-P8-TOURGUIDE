package tourGuide.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.exception.UserNameNotFoundException;
import tourGuide.exception.UserPreferencesNotFoundException;
import tourGuide.model.UserModel;
import tourGuide.service.TourGuideService;
import tripPricer.Provider;

@RestController
public class HomeController {

	@Autowired
	TourGuideService tourGuideService;

    /** HTML GET request that returns a welcome message
     *
     * @return a string message
     */
    @GetMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    /**
     * HTML GET request that starts the tracker
     */
    @GetMapping("/location/startTracker")
    public void startTracker() {
        tourGuideService.tracker.startTracking();
    }

    /**
     * HTML GET request that stops the tracker
     */
    @GetMapping("/location/stopTracker")
    public void stopTracker() {
        tourGuideService.tracker.stopTracking();
    }
}