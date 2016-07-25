package com.musicforall.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Properties;


/**
 * Contains beans related to the enterprise information system.
 * Production version.
 */
@Configuration
@EnableTransactionManagement
@Profile("mysql")
public class HibernateConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.musicforall.model", "com.musicforall.history.table");
        sessionFactory.setHibernateProperties(additionalProperties());
        return sessionFactory;
    }

    @Bean
    public DriverManagerDataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("db.driver"));
        dataSource.setUrl(env.getRequiredProperty("db.url"));
        dataSource.setUsername(env.getRequiredProperty("db.userName"));
        dataSource.setPassword(env.getRequiredProperty("db.password"));
        return dataSource;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
        final HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(s);
        txManager.setDataSource(dataSource());
        return txManager;
    }

    private Properties additionalProperties() {
        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        properties.setProperty("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        properties.setProperty("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
        properties.setProperty("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
        return properties;
    }
}
