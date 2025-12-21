package com.techelevator.dao;

import com.techelevator.model.Cookout;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;



public class JdbcCookoutDaoTest extends BaseDaoTest{


        private static final Cookout COOKOUT_1 = new Cookout(1, 2, 3, 4, LocalDateTime.of(2025,1,15,12,30),"Backyard BBQ","backyard","event_one");

        private JdbcCookoutDao sut;
        private Cookout testCookout;
        private JdbcTemplate jdbcTemplate;

        @BeforeEach
    public void setup() {
            jdbcTemplate = new JdbcTemplate(dataSource);
            sut = new JdbcCookoutDao(jdbcTemplate);
            testCookout = new Cookout(2, 3, 4, 5, LocalDateTime.of(2025,1,15,12,30),"Front-yard BBQ","backyard","event_two");
        }

        @Test
    public void getCookoutById() {
            Cookout cookout = sut.getCookoutById(1);
            assertCookoutsMatch(COOKOUT_1,cookout);

        }

        private void assertCookoutsMatch(Cookout expected,Cookout actual) {
            assertEquals(expected.getId(),actual.getId());
            assertEquals(expected.getHostId(),actual.getHostId());
            assertEquals(expected.getChefId(),actual.getChefId());
            assertEquals(expected.getMenuId(),actual.getMenuId());
            assertEquals(expected.getStartTime(), actual.getStartTime());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getLocation(), actual.getLocation());
            assertEquals(expected.getDescription(), actual.getDescription());
        }
}
