package tourGuide.controller;

import com.jsoniter.output.JsonStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.dto.UserPreferencesDTO;
import tourGuide.exception.UserNameNotFoundException;
import tourGuide.exception.UserPreferencesNotFoundException;
import tourGuide.service.InternalTestService;
import tourGuide.service.TourGuideService;

@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	TourGuideService tourGuideService;

	@Autowired
    InternalTestService internalTestService;

    /** HTML PUT request that updates the trip preferences of a specific user
     *
     * @param userPreferencesDTO the RequestBody of UserPreferencesDTO containing the new preferences
     * @return a string of UserPreferences for the specific user
     */
    @PutMapping("/update/Preferences")
    public String updatePreferences(@RequestParam String userName, @RequestBody UserPreferencesDTO userPreferencesDTO)
            throws UserNameNotFoundException, UserPreferencesNotFoundException {
        logger.debug("Access to /update/Preferences endpoint with username : " + userName);
        logger.debug("Access to /update/Preferences endpoint with UserPreferencesDTO as a body : " + userPreferencesDTO);
        if(!internalTestService.checkIfUserNameExists(userName)) {
            logger.error("This username does not exist" + userName);
            throw new UserNameNotFoundException(userName);
        }
        if(userPreferencesDTO == null){
            logger.error("This UserPreferencesDTO does not exist or is invalid" + userPreferencesDTO);
            throw new UserPreferencesNotFoundException();
        }

        return JsonStream.serialize(new UserPreferencesDTO(userName,
                tourGuideService.userUpdatePreferences(userName, userPreferencesDTO)));
    }
}