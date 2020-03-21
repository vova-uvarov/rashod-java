package com.vuvarov.rashod.util;

import com.vuvarov.rashod.configuration.security.RashodAuthentification;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {

    public static UUID currentUserId() {
        if (isAnonymousUser()) {
            return null;
        }
        RashodAuthentification authentification = extractAuthentification();
        return authentification == null ? null : authentification.getUserId();
    }

    public static boolean isAnonymousUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication==null || authentication instanceof AnonymousAuthenticationToken || authentication.getPrincipal().equals("anonymousUser");
    }

    private static RashodAuthentification extractAuthentification() {
        return (RashodAuthentification) SecurityContextHolder.getContext().getAuthentication();
    }
}
