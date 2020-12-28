package tourGuide;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jsoniter.output.JsonStream;

import gpsUtil.location.VisitedLocation;
import tourGuide.service.TourGuideService;
import tourGuide.user.User;
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

    /** HTML GET request that returns a random location of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a Json string of a user location in longitude and latitude
     */
    @GetMapping("/getLocation")
    public String getLocation(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(tourGuideService.getUser(userName));
		return JsonStream.serialize(visitedLocation.location);
    }

    /** HTML GET request that returns the 5 closest attractions of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a Json string of nearby attractions.
     */
    //  TODO: Change this method to no longer return a List of Attractions.
 	//  Instead: Get the closest five tourist attractions to the user - no matter how far away they are.
 	//  Return a new JSON object that contains:
    	// Name of Tourist attraction, 
        // Tourist attractions lat/long, 
        // The user's location lat/long, 
        // The distance in miles between the user's location and each of the attractions.
        // The reward points for visiting each Attraction.
        //    Note: Attraction reward points can be gathered from RewardsCentral
    @GetMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
    	VisitedLocation visitedLocation = tourGuideService.getUserLocation(tourGuideService.getUser(userName));
    	return JsonStream.serialize(tourGuideService.getNearByAttractions(visitedLocation));
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
    	// TODO: Get a list of every user's most recent location as JSON
    	//- Note: do not use gpsUtil to query for their current location,
    	//        but rather gathers the user's current location from their stored location history.
    	//
    	// Return object should be the just a JSON mapping of userId to Locations similar to:
    	//     {
    	//        "019b04a9-067a-4c76-8817-ee75088c3822": {"longitude":-48.188821,"latitude":74.84371} 
    	//        ...
    	//     }
    	
    	return JsonStream.serialize("");
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
}