package com.musicforall.util;

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

    private DatabaseUtil() {
    }

    public static void createUsersConnectionRepositoryTable(DataSource dataSource) {
        Statement statement;
        try {
            statement = dataSource.getConnection().createStatement();
            statement.execute("select count(*) from UserConnection");
            statement.execute("DROP TABLE UserConnection");
        } catch (SQLException e) { /*NOP*/ }
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("JdbcUsersConnectionRepository.sql",
                JdbcUsersConnectionRepository.class));
        DatabasePopulatorUtils.execute(databasePopulator, dataSource);
    }
}
