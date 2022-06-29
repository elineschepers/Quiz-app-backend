package be.ucll.quizappbackend;

import be.ucll.quizappbackend.Util.CustomAuthenticationFailureHandler;
import be.ucll.quizappbackend.Util.UCLLOAuthRequestEntityConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
public class OAuth2LoginConfig {

	@EnableWebSecurity
    public static class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()
				.headers().frameOptions().sameOrigin()
				.and()
				.authorizeRequests((authorizeRequests) ->
					authorizeRequests
						.antMatchers("/api/oauth2/callback/**", "/api/oauth2/provider/*", "/api/auth/login", "/api/auth/error", "/api/health").permitAll()
						.anyRequest().authenticated()
				)
				.oauth2Client()
				.and()
				.oauth2Login()
					.loginPage("/api/auth/login")
					.redirectionEndpoint(redirectionEndpointConfig -> {
						redirectionEndpointConfig.baseUri("/api/oauth2/callback/*");
					})
					.authorizationEndpoint(authorizationEndpointConfig -> {
						authorizationEndpointConfig.baseUri("/api/oauth2/provider");
					})
					.tokenEndpoint(tokenEndpoint ->
						tokenEndpoint.accessTokenResponseClient(this.accessTokenResponseClient())
					)
					.defaultSuccessUrl("/api/auth/success")
					.failureHandler(authenticationFailureHandler())
				.and()
				.logout()
					.logoutUrl("/api/auth/logout")
					.logoutSuccessUrl("/auth/goodbye");
		}

		@Bean
		/*
		 * This is used to customize the authentication failure handler. So we can show a custom error message.
		 */
		public AuthenticationFailureHandler authenticationFailureHandler() {
			return new CustomAuthenticationFailureHandler();
		}

		@Bean
		/*
		  A custom request entity converter was needed because the default one did not correctly create the Authentication
		  header for the request. The ClientId and ClientSecret were URL Encoded which UCLL does not support.
		 */
		public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
			DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
			accessTokenResponseClient.setRequestEntityConverter(new UCLLOAuthRequestEntityConverter());

			return accessTokenResponseClient;
		}
	}

	@Bean
	public OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
		return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
	}

	@Bean
	public OAuth2AuthorizedClientRepository authorizedClientRepository(OAuth2AuthorizedClientService authorizedClientService) {
		return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
	}

	@Bean
	public InMemoryClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.ucllClientRegistration());
	}

	/**
	 * This is a custom client registration for the UCLL OAuth2 provider.
	 *
	 * @return ClientRegistration
	 */
	private ClientRegistration ucllClientRegistration() {
		return ClientRegistration
				.withRegistrationId("ucll")
				.clientId(this.clientId)
				.clientSecret(this.clientSecret)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.redirectUri("{baseUrl}/api/oauth2/callback/{registrationId}")
				.authorizationUri(this.authorizationUri)
				.tokenUri(this.tokenUri)
				.userInfoUri(this.userInfoUri)
				.userNameAttributeName(this.userNameAttribute)
				.clientName("UCLL")
				.build();
	}

	@Value("${oauth2.client-id}")
	private String clientId;

	@Value("${oauth2.client-secret}")
	private String clientSecret;

	@Value("${oauth2.token-uri}")
	private String tokenUri;

	@Value("${oauth2.user-info-uri}")
	private String userInfoUri;

	@Value("${oauth2.authorization-uri}")
	private String authorizationUri;

	@Value("${oauth2.user-name-attribute}")
	private String userNameAttribute;
}
