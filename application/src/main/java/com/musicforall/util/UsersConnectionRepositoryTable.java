package com.musicforall.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Andrey on 8/15/16.
 */
public final class UsersConnectionRepositoryTable {

    private static final Logger LOG = LoggerFactory.getLogger(UsersConnectionRepositoryTable.class);

    private static DataSource dataSource;

    private UsersConnectionRepositoryTable() {
    }

    public static void update(DataSource data) {
        dataSource = data;
        if (!isExist()) {
            create();
        }
    }

    private static void create() {
        final ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("JdbcUsersConnectionRepository.sql",
                JdbcUsersConnectionRepository.class));
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
        LOG.info("Table UserConnection successfully created");
    }

    private static boolean isExist() {
        final String query = "SELECT count(*) FROM INFORMATION_SCHEMA.TABLES" +
                " WHERE TABLE_NAME = 'UserConnection'";
        try (final ResultSet resultSet = dataSource.getConnection().createStatement().executeQuery(query)) {
            if (resultSet.next() && (resultSet.getInt(1) == 1)) {
                return true;
            }
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }
        return false;
    }
}
