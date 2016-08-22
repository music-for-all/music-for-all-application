package com.musicforall.util;

import com.musicforall.model.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Andrey on 7/22/16.
 */
public final class SecurityUtil {

    private SecurityUtil() {
    }

    /**
     * Gets the identity of the authenticated principal.
     *
     * @return the authenticated user, or <code>null</code> if no user is authenticated
     */
    public static User currentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return (User) authentication.getPrincipal();
    }
}