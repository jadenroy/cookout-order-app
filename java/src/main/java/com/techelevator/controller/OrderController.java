package com.techelevator.controller;

import com.techelevator.dao.CookoutDao;
import com.techelevator.dao.OrderDao;
import com.techelevator.dao.UserDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Cookout;
import com.techelevator.model.Order;
import com.techelevator.model.OrderDto;
import com.techelevator.model.User;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/orders")
public class OrderController {

    private final OrderDao orderDao;
    private final UserDao userDao;
    private final CookoutDao cookoutDao;

    public OrderController(OrderDao orderDao, UserDao userDao, CookoutDao cookoutDao) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.cookoutDao = cookoutDao;
    }

    // ---------------------------------------------------
    // PLACE ORDER (ATTENDEE)
    // ---------------------------------------------------
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestBody OrderDto orderDto, Principal principal) {

        User user = userDao.getUserByUsername(principal.getName());
        orderDto.setAttendeeId(user.getId());

        boolean isAttending = cookoutDao
                .getAttendees(orderDto.getCookoutId())
                .stream()
                .anyMatch(u -> u.getId() == user.getId());

        if (!isAttending) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User is not attending this cookout"
            );
        }

        try {
            return orderDao.createOrder(orderDto);
        } catch (DaoException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to place order"
            );
        }
    }

    // ---------------------------------------------------
    // GET ORDERS FOR COOKOUT (HOST / CHEF ONLY)
    // ---------------------------------------------------
    @GetMapping("/cookout/{cookoutId}")
    public List<Order> getOrdersByCookout(
            @PathVariable int cookoutId,
            Principal principal
    ) {
        User user = userDao.getUserByUsername(principal.getName());
        Cookout cookout = cookoutDao.getCookoutById(cookoutId);

        if (cookout == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Cookout not found"
            );
        }

        boolean isHost = cookout.getHostId() == user.getId();
        boolean isChef = cookout.getChefId() != null
                && cookout.getChefId() == user.getId();

        if (!isHost && !isChef) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only host or chef can view kitchen orders"
            );
        }

        return orderDao.getOrdersByCookoutId(cookoutId);
    }

    // ---------------------------------------------------
    // FINISH ORDER (HOST / CHEF)
    // ---------------------------------------------------
    @PutMapping("/{orderId}/finish")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void finishOrder(@PathVariable int orderId) {
        orderDao.finishOrder(orderId);
    }
}
