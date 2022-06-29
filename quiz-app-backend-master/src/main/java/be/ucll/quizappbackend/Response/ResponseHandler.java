package be.ucll.quizappbackend.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();

        // if message is not null or not empty, add it to the map
        if (message != null && !message.isEmpty()) {
            map.put("message", message);
        }

        map.put("data", responseObj);

        return new ResponseEntity<Object>(map, status);
    }

    public static ResponseEntity<Object> generateResponse(String message, Object responseObj) {
        return generateResponse(message, HttpStatus.OK, responseObj);
    }

    public static ResponseEntity<Object> generateResponse(Object responseObj) {
        return generateResponse(null, HttpStatus.OK, responseObj);
    }
}
