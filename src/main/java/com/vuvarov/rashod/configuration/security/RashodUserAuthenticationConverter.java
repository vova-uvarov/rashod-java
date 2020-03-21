package com.vuvarov.rashod.configuration.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Map;
import java.util.UUID;

@Slf4j
public class RashodUserAuthenticationConverter extends JwtAccessTokenConverter {

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        OAuth2Authentication authentication = super.extractAuthentication(map);

        RashodAuthentification token = new RashodAuthentification(
                authentication.getOAuth2Request(),
                authentication.getUserAuthentication());

        token.setUserId(UUID.fromString((String) map.getOrDefault("user_id", null)));

        log.debug("Authentication extracted '{}'", token.toString());

        return token;
    }
}
