package tourGuide.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import tourGuide.dto.UserPreferencesDTO;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserPreferencesNotFoundException extends RuntimeException {

    private final Logger logger = LoggerFactory.getLogger(UserPreferencesNotFoundException.class);


    public UserPreferencesNotFoundException() {
        super("User Preferences is empty");
        logger.error("User Preferences is empty");
    }
}