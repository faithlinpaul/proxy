package in.co.ee.proxy.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    private static Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);


    @ExceptionHandler({ConfigurationException.class})
    void configurationErrors(HttpServletResponse response, Exception ex) throws IOException {
        logger.error("Configuration Error:", ex);
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }
}
