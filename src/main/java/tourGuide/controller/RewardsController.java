package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.exception.UserNameNotFoundException;
import tourGuide.service.InternalTestService;
import tourGuide.service.TourGuideService;

@RestController
public class RewardsController {

	@Autowired
	TourGuideService tourGuideService;

	@Autowired
    InternalTestService internalTestService;

    /** HTML GET request that returns the rewards of the username bounded to the request
     *
     * @param userName string of the username (internalUserX)
     * @return a Json string of all UserRewards
     */
    @GetMapping("/getRewards")
    public String getRewards(@RequestParam String userName) {
        if(!internalTestService.checkIfUserNameExists(userName)) {
            throw new UserNameNotFoundException(userName);
        }
    	return JsonStream.serialize(tourGuideService.getUserRewards(tourGuideService.getUser(userName)));
    }
}