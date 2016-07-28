package com.musicforall.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * An AuthenticationProvider implementation which retrieves user details from a UserDetailsService.
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    /**
     * The user service to retrieve user details from.
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * The password encoder used to check whether the entered password matches
     * the encrypted password retrieved from the database.
     */
    private PasswordEncoder passwordEncoder;

    /**
     * Performs authentication.
     */
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        final String username = authentication.getName();
        final UserDetails user = userDetailsService.loadUserByUsername(username);

        if (user == null) {
            throw new BadCredentialsException("Username not found.");
        }

        final String password = (String) authentication.getCredentials();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong password.");
        }

        return new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

    /**
     * Sets the PasswordEncoder field.
     * @param passwordEncoder the PasswordEncoder instance
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
