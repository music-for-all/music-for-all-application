package com.musicforall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        final int REMEMBER_ME_SECONDS = 86400;  // 24h
        http
                .authorizeRequests()
                .antMatchers("/", "/index", "/welcome*").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/welcome").permitAll()
                .successHandler(successHandler())
                .failureHandler(failureHandler())
                .and()
                .rememberMe().tokenValiditySeconds(REMEMBER_ME_SECONDS)
                .rememberMeParameter("_spring_security_remember_me")
                .and()
                .logout().permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring().antMatchers("/resources/**");
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {

        final SavedRequestAwareAuthenticationSuccessHandler authSuccessHandler =
                new SavedRequestAwareAuthenticationSuccessHandler();
        authSuccessHandler.setDefaultTargetUrl("/");
        return authSuccessHandler;
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() {

        final SimpleUrlAuthenticationFailureHandler authFailureHandler = new SimpleUrlAuthenticationFailureHandler();
        authFailureHandler.setDefaultFailureUrl("/welcome?error");
        return authFailureHandler;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }
}
