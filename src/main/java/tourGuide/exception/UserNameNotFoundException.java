package tourGuide.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNameNotFoundException extends RuntimeException {

    private final Logger logger = LoggerFactory.getLogger(UserNameNotFoundException.class);

    public UserNameNotFoundException(String userName) {
        super("Username not found : " + userName);
        logger.error("Username not found : " + userName);
    }
}