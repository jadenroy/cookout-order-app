package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Invitation;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcInvitationDao implements InvitationDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcInvitationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Invitation getInvitationById(int id) {
        String sql = """
            SELECT invitation_id, from_id, to_id, cookout_id, active, accepted
            FROM invitations
            WHERE invitation_id = ?
        """;

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
            if (rs.next()) {
                return mapRowToInvitation(rs);
            }
            return null;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect", e);
        }
    }

    public List<Invitation> getInvitationsByFromId(int fromId) {
        String sql = """
            SELECT invitation_id, from_id, to_id, cookout_id, active, accepted
            FROM invitations
            WHERE from_id = ?
        """;

        List<Invitation> invitations = new ArrayList<>();

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, fromId);
            while (rs.next()) {
                invitations.add(mapRowToInvitation(rs));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect", e);
        }

        return invitations;
    }

    // OPTION B — enriched for UI
    @Override
    public List<Invitation> getInvitationsByToId(int toId) {
        String sql = """
            SELECT
                i.invitation_id,
                i.from_id,
                i.to_id,
                i.cookout_id,
                i.active,
                i.accepted,
                c.name AS cookout_name,
                u.username AS from_username
            FROM invitations i
            JOIN cookouts c ON c.cookout_id = i.cookout_id
            JOIN users u ON u.user_id = i.from_id
            WHERE i.to_id = ?
        """;

        List<Invitation> invitations = new ArrayList<>();

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, toId);
            while (rs.next()) {
                invitations.add(mapRowToInvitation(rs));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect", e);
        }

        return invitations;
    }

    @Override
    public List<Invitation> getInvitationsByUserId(int userId) {
        String sql = """
            SELECT invitation_id, from_id, to_id, cookout_id, active, accepted
            FROM invitations
            WHERE from_id = ? OR to_id = ?
        """;

        List<Invitation> invitations = new ArrayList<>();

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, userId, userId);
            while (rs.next()) {
                invitations.add(mapRowToInvitation(rs));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect", e);
        }

        return invitations;
    }

    @Override
    public List<Invitation> getInvitationsByCookoutId(int cookoutId) {
        String sql = """
            SELECT invitation_id, from_id, to_id, cookout_id, active, accepted
            FROM invitations
            WHERE cookout_id = ?
        """;

        List<Invitation> invitations = new ArrayList<>();

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, cookoutId);
            while (rs.next()) {
                invitations.add(mapRowToInvitation(rs));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect", e);
        }

        return invitations;
    }

    @Override
    public Invitation createInvitation(Invitation invitation) {
        String sql = """
            INSERT INTO invitations (from_id, to_id, cookout_id)
            VALUES (?, ?, ?)
            RETURNING invitation_id
        """;

        try {
            Integer newId = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                invitation.getFromId(),
                invitation.getToId(),
                invitation.getCookoutId()
            );

            return getInvitationById(newId);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect", e);
        }
    }

    @Override
    public boolean acceptInvitation(int invitationId, int userId) {
        String sql = """
            UPDATE invitations
            SET accepted = TRUE, active = FALSE
            WHERE invitation_id = ? AND to_id = ? AND active = TRUE
        """;
        return jdbcTemplate.update(sql, invitationId, userId) > 0;
    }

    @Override
    public boolean declineInvitation(int invitationId, int userId) {
        String sql = """
            UPDATE invitations
            SET accepted = FALSE, active = FALSE
            WHERE invitation_id = ? AND to_id = ? AND active = TRUE
        """;
        return jdbcTemplate.update(sql, invitationId, userId) > 0;
    }

    private Invitation mapRowToInvitation(SqlRowSet rs) {
        Invitation invitation = new Invitation();
        invitation.setInvitationId(rs.getInt("invitation_id"));
        invitation.setFromId(rs.getInt("from_id"));
        invitation.setToId(rs.getInt("to_id"));
        invitation.setCookoutId(rs.getInt("cookout_id"));
        invitation.setActive(rs.getBoolean("active"));
        invitation.setAccepted(rs.getBoolean("accepted"));

        if (hasColumn(rs, "cookout_name")) {
            invitation.setCookoutName(rs.getString("cookout_name"));
        }
        if (hasColumn(rs, "from_username")) {
            invitation.setFromUsername(rs.getString("from_username"));
        }

        return invitation;
    }

    private boolean hasColumn(SqlRowSet rs, String column) {
        try {
            rs.findColumn(column);
            return true;
        } catch (InvalidResultSetAccessException e) {
            return false;
        }
    }
}
