package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Cookout;
import com.techelevator.model.User;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import javax.sql.DataSource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcCookoutDao implements CookoutDao {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcUserDao jdbcUserDao;

    public JdbcCookoutDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcUserDao = new JdbcUserDao(jdbcTemplate);
    }

    @Override
    public Cookout getCookoutById(int id) {
        String sql = """
            SELECT cookout_id, host_id, chef_id, start_time, name, location, description
            FROM cookouts WHERE cookout_id = ?
            """;

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
            if (rs.next()) {
                return mapRowToCookout(rs);
            }
            return null;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect", e);
        }
    }

    @Override
    public List<Cookout> getCookouts() {
        List<Cookout> cookouts = new ArrayList<>();
        String sql = """
            SELECT cookout_id, host_id, chef_id, start_time, name, location, description
            FROM cookouts
            """;

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
            while (rs.next()) {
                cookouts.add(mapRowToCookout(rs));
            }
            return cookouts;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect", e);
        }
    }

    @Override
    public Cookout createCookout(Cookout cookout) {

        String sql = """
            INSERT INTO cookouts
            (host_id, chef_id, start_time, name, location, description)
            VALUES (?, ?, ?, ?, ?, ?)
            RETURNING cookout_id
            """;

        try {
            Integer newId = jdbcTemplate.queryForObject(
                    sql,
                    Integer.class,
                    cookout.getHostId(),
                    cookout.getChefId(),   // can be null
                    cookout.getStartTime(),
                    cookout.getName(),
                    cookout.getLocation(),
                    cookout.getDescription()
            );

            return getCookoutById(newId);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect", e);

        } catch (DataIntegrityViolationException e) {
            e.printStackTrace(); // helpful while you're debugging
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public List<User> getAttendees(int cookoutId) {
        List<User> attendees = new ArrayList<>();
        String sql = "SELECT attendee_id FROM cookout_attendees WHERE cookout_id = ?";

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, cookoutId);
            while (rs.next()) {
                attendees.add(jdbcUserDao.getUserById(rs.getInt("attendee_id")));
            }
            return attendees;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect", e);
        }
    }

    @Override
    public boolean addAttendee(int cookoutId, int attendeeId) {
        String sql = "INSERT INTO cookout_attendees (cookout_id, attendee_id) VALUES (?, ?)";

        try {
            return jdbcTemplate.update(sql, cookoutId, attendeeId) == 1;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }
    @Override
    public List<Cookout> getCookoutsByHostId(int userId) {
        List<Cookout> list = new ArrayList<>();
        String sql = "SELECT * FROM cookouts where host_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql,userId);

        while (rowSet.next()) list.add(mapRowToCookout(rowSet));
        return list;
    }
    @Override
    public List<Cookout> getCookoutsByAttendeeId(int userId) {
        List<Cookout> list = new ArrayList<>();
        String sql = "Select c.* from cookouts c join cookout_attendees a on c.cookout_id = a.cookout_id where a.attendee_id = ?";
        try{
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            while (rowSet.next()) {
                list.add(mapRowToCookout(rowSet));
            }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connet to server or database");
        }
        return list;
    }

    private Cookout mapRowToCookout(SqlRowSet rs) {
        Cookout c = new Cookout();
        c.setId(rs.getInt("cookout_id"));
        c.setHostId(rs.getInt("host_id"));
        c.setChefId((Integer) rs.getObject("chef_id"));
//        c.setMenuId((Integer) rs.getObject("menu_id"));

        if (rs.getTimestamp("start_time") != null) {
            c.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
        }

        c.setName(rs.getString("name"));
        c.setLocation(rs.getString("location"));
        c.setDescription(rs.getString("description"));

        return c;
    }
}




