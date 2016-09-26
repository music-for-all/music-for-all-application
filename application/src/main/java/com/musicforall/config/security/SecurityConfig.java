package com.musicforall.config.security;

import com.musicforall.common.Constants;
import com.musicforall.config.SocialConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import({SocialConfig.class})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String MAIN = "/main";

    private static final String WELCOME = "/welcome";

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        final int REMEMBER_ME_SECONDS = 86400;  // 24h
        http
                .authorizeRequests()
                .antMatchers("/", "/welcome*", "/files/**", "/tracks/*", "/artist/*").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage(WELCOME).permitAll()
                .usernameParameter(Constants.EMAIL)
                .successHandler(successHandler())
                .failureHandler(failureHandler())
                .and()
                .rememberMe().tokenValiditySeconds(REMEMBER_ME_SECONDS)
                .rememberMeParameter("_spring_security_remember_me")
                .and()
                .logout().permitAll()
                .and()
                .apply(new SpringSocialConfigurer()
                        .postLoginUrl(MAIN)
                        .defaultFailureUrl(WELCOME)
                        .alwaysUsePostLoginUrl(true));
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                /* Allow access to the static resources (e.g., css, js files). */
                .ignoring().antMatchers("/resources/**");
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {

        final CustomAuthenticationSuccessHandler authSuccessHandler =
                new CustomAuthenticationSuccessHandler();
        authSuccessHandler.setDefaultTargetUrl(MAIN);
        authSuccessHandler.setTargetUrlParameter("targetUrl");
        return authSuccessHandler;
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {

        final CustomAuthenticationFailureHandler authFailureHandler =
                new CustomAuthenticationFailureHandler();
        authFailureHandler.setDefaultFailureUrl("/welcome?error");
        return authFailureHandler;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        /* Use a custom AuthenticationProvider. */
        auth.authenticationProvider(authenticationProvider());
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }
}
