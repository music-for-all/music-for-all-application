package com.musicforall.services.social;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * Created by Andrey on 8/11/16.
 */
public class SimpleSocialUserDetailService implements SocialUserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleSocialUserDetailService.class);

    private final UserDetailsService userDetailsService;

    public SimpleSocialUserDetailService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException, DataAccessException {
        LOG.debug("Loading user by user id: {}", userId);

        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        LOG.debug("Found user details: {}", userDetails);

        return (SocialUserDetails) userDetails;
    }
}
