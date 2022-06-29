/**
 * Copyright (c) 2022
 * <p>
 * long description for the file
 *
 * @summary short description for the file
 * @author Kevin Coorens <kevin.coorens@student.be>
 * <p>
 * Created at     : 17/02/2022 15:53
 */

package be.ucll.quizappbackend.Controllers;

import be.ucll.quizappbackend.Models.Requests.Course;
import be.ucll.quizappbackend.Response.ResponseHandler;
import be.ucll.quizappbackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<Object> getCoursesWithSeries(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        HashMap<String, Object> data = new HashMap<>();
        Map<Integer, Course> map = userService.fetchCoursesOfUser(authorizedClient.getPrincipalName(), authorizedClient.getAccessToken().getTokenValue());
        data.put("courses", map.values());
        return ResponseHandler.generateResponse("", data);
    }

    @PostMapping("/student/{studentId}/enable/")
    public ResponseEntity<Object> enableStudent(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient, @PathVariable String studentId){
        userService.enableStudentForLector(studentId, authorizedClient.getPrincipalName());
        return ResponseHandler.generateResponse("", "success");
    }

    @PostMapping("/student/{studentId}/disable/")
    public ResponseEntity<Object> disableStudent(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient, @PathVariable String studentId){
        userService.disableStudentForLector(studentId, authorizedClient.getPrincipalName());
        return ResponseHandler.generateResponse("", "success");
    }

    @GetMapping("/student/disabled/")
    public ResponseEntity<Object> getDisabledStudents(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient){
        return ResponseHandler.generateResponse("", userService.getDisabledStudents(authorizedClient.getPrincipalName(), authorizedClient.getAccessToken().getTokenValue()));
    }
}
