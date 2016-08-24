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

    private static final String EMAIL = "email";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ConnectionSignUp connectionSignUp;

    @Autowired
    private Environment environment;

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer configurer,
                                       Environment env) {
        final FacebookConnectionFactory facebook = new FacebookConnectionFactory(
                env.getRequiredProperty("spring.social.facebook.appId"),
                env.getRequiredProperty("spring.social.facebook.appSecret"));
        facebook.setScope(EMAIL);

        final TwitterConnectionFactory twitter = new TwitterConnectionFactory(
                env.getRequiredProperty("spring.social.twitter.appId"),
                env.getRequiredProperty("spring.social.twitter.appSecret"));

        final GoogleConnectionFactory google = new GoogleConnectionFactory(
                env.getRequiredProperty("spring.social.google.appId"),
                env.getRequiredProperty("spring.social.google.appSecret"));
        google.setScope(EMAIL);

        configurer.addConnectionFactory(facebook);
        configurer.addConnectionFactory(google);
        configurer.addConnectionFactory(twitter);
    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator locator) {
        DatabaseUtil.createUsersConnectionRepositoryTable(dataSource);
        final JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
                locator, Encryptors.text(environment.getProperty("social.security.encryptPassword"),
                environment.getProperty("social.security.encryptSalt")));
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
