package com.vuvarov.rashod.configuration.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
public class RashodAuthentification extends OAuth2Authentication {

    @Setter
    @Getter
    private UUID userId;

    public RashodAuthentification(OAuth2Request storedRequest, Authentication userAuthentication) {
        super(storedRequest, userAuthentication);
    }
}
