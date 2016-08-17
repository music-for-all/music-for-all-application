package com.musicforall.config;

import com.musicforall.util.DatabaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.*;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ReconnectFilter;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;

import javax.sql.DataSource;

/**
 * Created by Andrey on 8/11/16.
 */
@Configuration
@EnableSocial
public class SocialConfig implements SocialConfigurer {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ConnectionSignUp connectionSignUp;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer,
                                       Environment environment) {
        final FacebookConnectionFactory facebookConnectionFactory = new FacebookConnectionFactory(
                environment.getRequiredProperty("spring.social.facebook.appId"),
                environment.getRequiredProperty("spring.social.facebook.appSecret"));
        facebookConnectionFactory.setScope("email");
        connectionFactoryConfigurer.addConnectionFactory(facebookConnectionFactory);
        connectionFactoryConfigurer.addConnectionFactory(new TwitterConnectionFactory(
                environment.getRequiredProperty("spring.social.twitter.appId"),
                environment.getRequiredProperty("spring.social.twitter.appSecret")));
        final GoogleConnectionFactory googleConnectionFactory = new GoogleConnectionFactory(
                environment.getRequiredProperty("spring.social.google.appId"),
                environment.getRequiredProperty("spring.social.google.appSecret"));
        googleConnectionFactory.setScope("email");
        connectionFactoryConfigurer.addConnectionFactory(googleConnectionFactory);
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator
                                                                          connectionFactoryLocator) {
        DatabaseUtil.createUsersConnectionRepositoryTable(dataSource);
        final JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
                connectionFactoryLocator, Encryptors.noOpText());
        repository.setConnectionSignUp(connectionSignUp);
        return repository;
    }

    @Bean
    public ConnectController connectController(ConnectionFactoryLocator connectionFactoryLocator,
                                               ConnectionRepository connectionRepository) {
        return new ConnectController(connectionFactoryLocator, connectionRepository);
    }

    @Bean
    public ReconnectFilter apiExceptionHandler(UsersConnectionRepository usersConnectionRepository,
                                               UserIdSource userIdSource) {
        return new ReconnectFilter(usersConnectionRepository, userIdSource);
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public Facebook facebook(ConnectionRepository repository) {
        final Connection<Facebook> connection = repository.findPrimaryConnection(Facebook.class);
        return connection == null ? null : connection.getApi();
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public Twitter twitter(ConnectionRepository repository) {
        final Connection<Twitter> connection = repository.findPrimaryConnection(Twitter.class);
        return connection == null ? null : connection.getApi();
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
    public Google google(ConnectionRepository repository) {
        final Connection<Google> connection = repository.findPrimaryConnection(Google.class);
        return connection == null ? null : connection.getApi();
    }
}
