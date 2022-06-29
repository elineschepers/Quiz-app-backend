package be.ucll.quizappbackend.Controllers;


import be.ucll.quizappbackend.OAuth2LoginConfig;
import be.ucll.quizappbackend.Response.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/login")
    public RedirectView login() {
        // Redirect to the login page
        return new RedirectView("/api/oauth2/provider/ucll");
    }

    @GetMapping("/error")
    public String error(Model model) {
        model.addAttribute("error", "An error has occurred");
        return "error";
    }

    @GetMapping("/success")
    public RedirectView success() {
        return new RedirectView("/auth/success");
    }

    @GetMapping("/user")
    public ResponseEntity<Object> user(Model model) {
        OAuth2User user = (OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        model.addAttribute("accountId", user.getName());
        model.addAttribute("firstName", user.getAttribute("firstName"));
        model.addAttribute("lastName", user.getAttribute("lastName"));

        return ResponseHandler.generateResponse(model);
    }
}
