package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Menu;
import com.techelevator.model.MenuDto;
import com.techelevator.model.MenuItem;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcMenuDao implements MenuDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMenuDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Menu getMenuByCookoutId(int cookoutId) {
        Menu menu = new Menu();
        menu.setCookoutId(cookoutId);

        String sql = """
            SELECT id, cookout_id, item_name
            FROM menu_items
            WHERE cookout_id = ?
        """;

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, cookoutId);
            while (rs.next()) {
                menu.addItem(mapRowToMenuItem(rs));
            }
            return menu;

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
    }

    @Override
    public Menu createMenu(MenuDto menuDto) {
        try {
            String sql = """
                INSERT INTO menu_items (cookout_id, item_name)
                VALUES (?, ?)
            """;

            for (String itemName : menuDto.getItemNames()) {
                jdbcTemplate.update(sql, menuDto.getCookoutId(), itemName);
            }

            return getMenuByCookoutId(menuDto.getCookoutId());

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
    }

    @Override
    public boolean updateMenuItem(int itemId, String itemName) {
        String sql = "UPDATE menu_items SET item_name = ? WHERE id = ?";
        try {
            return jdbcTemplate.update(sql, itemName, itemId) == 1;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
    }

    @Override
    public boolean deleteMenuItem(int itemId) {
        String sql = "DELETE FROM menu_items WHERE id = ?";
        try {
            return jdbcTemplate.update(sql, itemId) == 1;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
    }

    private MenuItem mapRowToMenuItem(SqlRowSet rs) {
        MenuItem item = new MenuItem();
        item.setId(rs.getInt("id"));
        item.setCookoutId(rs.getInt("cookout_id"));
        item.setItemName(rs.getString("item_name"));
        return item;
    }
}
