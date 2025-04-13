package com.msb.linkerbackend.security.oauth;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GithubOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    public static final String EMAIL = "email";
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Delegate to the default service to load the basic profile
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =
                new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = new HashMap<>(oauth2User.getAttributes());
        attributes.computeIfAbsent(EMAIL, key -> {
            String token = userRequest.getAccessToken().getTokenValue();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<List<Map<String, Object>>> emailResponse = restTemplate.exchange(
                    "https://api.github.com/user/emails",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<>() {
                    }
            );

            var emailList = emailResponse.getBody();
            if (emailList != null) {
                return emailList.stream()
                        .filter(e -> Boolean.TRUE.equals(e.get("primary")) && Boolean.TRUE.equals(e.get("verified")))
                        .map(e -> (String) e.get(EMAIL))
                        .findFirst()
                        .orElse(null);
            }
            return null;
        });



        // Return a new DefaultOAuth2User with our enriched attributes
        return new DefaultOAuth2User(
                oauth2User.getAuthorities(),
                attributes,
                "login"   // GitHub’s username attribute key
        );
    }
}
