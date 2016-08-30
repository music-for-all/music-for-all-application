package com.musicforall.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Andrey on 8/15/16.
 */
public final class DatabaseUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseUtil.class);

    private DatabaseUtil() {
    }

    public static void createUsersConnectionRepositoryTable(DataSource dataSource) {
        try (Statement statement = dataSource.getConnection().createStatement()) {
            statement.execute("select count(*) from UserConnection");
        } catch (SQLException e) {
            final ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
            databasePopulator.addScript(new ClassPathResource("JdbcUsersConnectionRepository.sql",
                    JdbcUsersConnectionRepository.class));
            DatabasePopulatorUtils.execute(databasePopulator, dataSource);
            LOG.info("Table UserConnection successfully created");
        }
    }
}
