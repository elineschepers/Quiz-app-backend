package be.ucll.quizappbackend.Util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.util.MultiValueMap;

import java.util.Base64;

public class UCLLOAuthRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {

    private OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

    public UCLLOAuthRequestEntityConverter() {
        defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
    }

    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest req) {
        RequestEntity<?> entity = defaultConverter.convert(req);

        MultiValueMap<String, String> headers = HttpHeaders.writableHttpHeaders(entity.getHeaders());

        String clientId = req.getClientRegistration().getClientId();
        String clientSecret = req.getClientRegistration().getClientSecret();

        // This is a bypass because Spring URL encodes the client_id and client_secret which is not supported by UCLL OAuth
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes()));

        return new RequestEntity<>(entity.getBody(), headers, entity.getMethod(), entity.getUrl());
    }
}
