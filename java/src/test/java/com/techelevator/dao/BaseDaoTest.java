package com.techelevator.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestingDatabaseConfig.class)
public abstract class BaseDaoTest {

    @Autowired
    protected DataSource dataSource;

    private Connection connection;

    @BeforeEach
    public void setupTransaction() throws SQLException {
        connection = dataSource.getConnection();
        connection.setAutoCommit(false);
    }

    @AfterEach
    public void rollback() throws SQLException {
        connection.rollback();
        connection.close();
    }

}
