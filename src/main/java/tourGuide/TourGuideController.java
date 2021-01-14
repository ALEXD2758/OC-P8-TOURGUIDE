package tourGuide;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.model.UserModel;
import tourGuide.service.TourGuideService;
import tourGuide.tracker.Tracker;
import tripPricer.Provider;

@RestController
public class TourGuideController {

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
    @GetMapping("/gpsUtil/startTracker")
    public void startTracker() {
        tourGuideService.tracker.startTracking();
    }

    /**
     * HTML GET request that stops the tracker
     */
    @GetMapping("/gpsUtil/stopTracker")
    public void stopTracker() {
        tourGuideService.tracker.stopTracking();
    }

    /** HTML GET request that returns a random location of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a Json string of a user location in longitude and latitude
     */
    @GetMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserVisitedLocation(tourGuideService.getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
    }

    /** HTML GET request that returns the 5 closest attractions of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a Json string of nearby attractions.
     */
    @GetMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        UserModel user = tourGuideService.getUser(userName);
    	VisitedLocation visitedLocation = tourGuideService.getUserVisitedLocation(user);
    	return JsonStream.serialize(tourGuideService.getNearestAttractions(visitedLocation, user));
    }

    /** HTML GET request that returns the rewards of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a Json string of all UserRewards
     */
    @GetMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
    	return JsonStream.serialize(tourGuideService.getUserRewards(tourGuideService.getUser(userName)));
    }

    /** HTML GET request that returns the current location of all users
     *
     * @return a Json string of current location of all users.
     */
    @GetMapping("/getAllCurrentLocations")
    public String getAllCurrentLocations() {
    	return JsonStream.serialize(tourGuideService.getAllUsersLocation());
    }

    /** HTML GET request that returns 5 random trip deals of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a string of a list of Provider in a random way
     */
    @GetMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
    	List<Provider> providers = tourGuideService.getTripDeals(tourGuideService.getUser(userName));
    	return JsonStream.serialize(providers);
    }
    /** HTML PUT request that updates the trip preferences of a specific user
     *
     * @param userPreferencesDTO the RequestBody of UserPreferencesDTO containing the new preferences
     * @return a string of UserPreferences for the specific user
     */
    @PutMapping("/update/Preferences")
    public String updatePreferences(@RequestBody UserPreferencesDTO userPreferencesDTO) {
        return JsonStream.serialize(new UserPreferencesDTO(userPreferencesDTO.getUsername(),
                tourGuideService.userUpdatePreferences(userPreferencesDTO)));
    }
}