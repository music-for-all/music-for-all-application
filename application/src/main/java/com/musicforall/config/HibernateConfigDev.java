package com.musicforall.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by Pukho on 04.07.2016.
 */

/**
 * Contains beans related to the enterprise information system.
 * Development version.
 */
@Configuration
@EnableTransactionManagement
@Profile("dev")
public class HibernateConfigDev {

    @Bean(name = "main_session")
    public LocalSessionFactoryBean sessionFactory() {
        final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.musicforall.model", "com.musicforall.history.model");
        sessionFactory.setHibernateProperties(additionalProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setName("music_for_all")
                .setType(EmbeddedDatabaseType.H2)
                .build();
    }

    @Bean
    @Primary
    @Autowired
    public HibernateTransactionManager transactionManager(@Qualifier("main_session") SessionFactory s) {
        final HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(s);
        txManager.setDataSource(dataSource());
        return txManager;
    }

    private Properties additionalProperties() {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "create");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.setProperty("hibernate.format_sql", Boolean.TRUE.toString());
        properties.setProperty("hibernate.show_sql", Boolean.TRUE.toString());
        /* Disable the second-level cache  */
        properties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.internal.NoCacheProvider");
        return properties;
    }
}
