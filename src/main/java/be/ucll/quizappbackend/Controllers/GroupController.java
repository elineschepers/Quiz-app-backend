/**
 * Copyright (c) 2022
 * <p>
 * long description for the file
 *
 * @summary short description for the file
 * @author Kevin Coorens <kevin.coorens@student.be>
 * <p>
 * Created at     : 17/02/2022 12:15
 */

package be.ucll.quizappbackend.Controllers;

import be.ucll.quizappbackend.Models.Requests.Course;
import be.ucll.quizappbackend.Models.Requests.Student;
import be.ucll.quizappbackend.Services.UcllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    @Autowired
    private UcllService ucllService;

    @GetMapping("/students/")
    public Map<String, Student> getStudents(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        return ucllService.fetchStudents(authorizedClient.getAccessToken().getTokenValue());
    }

    @GetMapping("/courses/")
    public Collection<Course> getCourses(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        return ucllService.fetchCourses(authorizedClient.getAccessToken().getTokenValue()).values();
    }
}
