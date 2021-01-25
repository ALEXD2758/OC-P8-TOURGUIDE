package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.exception.UserNameNotFoundException;
import tourGuide.model.UserModel;
import tourGuide.model.UserRewardModel;
import tourGuide.service.InternalTestService;
import tourGuide.service.TourGuideService;
import tourGuide.model.trip.Provider;

import java.util.List;

@RestController
public class TripPricerController {

    private Logger logger = LoggerFactory.getLogger(TripPricerController.class);

	@Autowired
	TourGuideService tourGuideService;

    @Autowired
    InternalTestService internalTestService;

    /** HTML GET request that returns 5 random trip deals of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a string of a list of Provider in a random way
     */
    @GetMapping("/getTripDeals")
    public String getTripDeals(@RequestParam String userName) {
        logger.debug("Access to /getTripDeals endpoint with username : " + userName);
        if(!internalTestService.checkIfUserNameExists(userName)) {
            logger.error("This username does not exist" + userName);
            throw new UserNameNotFoundException(userName);
        }
    	List<Provider> providers = tourGuideService.getTripDeals(tourGuideService.getUser(userName));
    	return JsonStream.serialize(providers);
    }
}