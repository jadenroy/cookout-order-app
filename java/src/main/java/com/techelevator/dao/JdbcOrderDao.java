package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Order;
import com.techelevator.model.OrderDto;
import com.techelevator.model.OrderItem;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcOrderDao implements OrderDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcOrderDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Order getOrderById(int orderId) {
        String sql = """
            SELECT o.order_id, o.cookout_id, o.attendee_id, o.finished,
                   u.username
            FROM orders o
            JOIN users u ON o.attendee_id = u.user_id
            WHERE o.order_id = ?
        """;

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, orderId);
            if (rs.next()) {
                Order order = mapRowToOrder(rs);
                loadItems(order);
                return order;
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect", e);
        }
        return null;
    }

    @Override
    public List<Order> getOrdersByCookoutId(int cookoutId) {
        List<Order> orders = new ArrayList<>();

        String sql = """
            SELECT o.order_id, o.cookout_id, o.attendee_id, o.finished,
                   u.username
            FROM orders o
            JOIN users u ON o.attendee_id = u.user_id
            WHERE o.cookout_id = ?
            ORDER BY o.order_id DESC
        """;

        try {
            SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, cookoutId);
            while (rs.next()) {
                Order order = mapRowToOrder(rs);
                loadItems(order);
                orders.add(order);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect", e);
        }

        return orders;
    }

    @Override
    public Order createOrder(OrderDto orderDto) {
        String orderSql = """
            INSERT INTO orders (cookout_id, attendee_id)
            VALUES (?, ?)
            RETURNING order_id
        """;

        try {
            Integer orderId = jdbcTemplate.queryForObject(
                orderSql,
                Integer.class,
                orderDto.getCookoutId(),
                orderDto.getAttendeeId()
            );

            String itemSql = """
                INSERT INTO order_items (order_id, item_id, quantity)
                VALUES (?, ?, ?)
            """;

            for (int i = 0; i < orderDto.getItemIds().size(); i++) {
                jdbcTemplate.update(
                    itemSql,
                    orderId,
                    orderDto.getItemIds().get(i),
                    orderDto.getItemQuantities().get(i)
                );
            }

            return getOrderById(orderId);

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect", e);
        }
    }

    @Override
    public int finishOrder(int orderId) {
        return jdbcTemplate.update(
            "UPDATE orders SET finished = TRUE WHERE order_id = ?",
            orderId
        );
    }

    private void loadItems(Order order) {
        String itemSql = """
            SELECT id, order_id, item_id, quantity
            FROM order_items
            WHERE order_id = ?
        """;

        SqlRowSet items = jdbcTemplate.queryForRowSet(itemSql, order.getOrderId());
        while (items.next()) {
            OrderItem item = new OrderItem();
            item.setId(items.getInt("id"));
            item.setOrderId(items.getInt("order_id"));
            item.setItemId(items.getInt("item_id"));
            item.setQuantity(items.getInt("quantity"));
            order.addItem(item);
        }
    }

    private Order mapRowToOrder(SqlRowSet rs) {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setCookoutId(rs.getInt("cookout_id"));
        order.setAttendeeId(rs.getInt("attendee_id"));
        order.setAttendeeUsername(rs.getString("username"));
        order.setFinished(rs.getBoolean("finished"));
        return order;
    }
}
