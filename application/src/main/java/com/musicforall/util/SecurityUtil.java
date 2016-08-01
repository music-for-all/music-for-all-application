package com.musicforall.util;

import com.musicforall.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Andrey on 7/22/16.
 */
public final class SecurityUtil {

    private SecurityUtil() {
    }

    /**
     * Gets the identity of the authenticated principal.
     * @return the authenticated user
     */
    public static User currentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}