package com.techelevator.controller;

import com.techelevator.dao.CookoutDao;
import com.techelevator.dao.UserDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Cookout;
import com.techelevator.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/cookouts")
public class CookoutController {

    private final CookoutDao cookoutDao;
    private final UserDao userDao;

    public CookoutController(CookoutDao cookoutDao, UserDao userDao) {
        this.cookoutDao = cookoutDao;
        this.userDao = userDao;
    }

    @GetMapping
    public List<Cookout> getAllCookouts() {
        return cookoutDao.getCookouts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cookout createCookout(@RequestBody Cookout cookout, Principal principal) {
        try {

            // Logged-in user's username → get full User object
            String username = principal.getName();
            User user = userDao.getUserByUsername(username);

            // ALWAYS force hostId to logged-in user (ignore client value)
            cookout.setHostId(user.getId());

            return cookoutDao.createCookout(cookout);

        } catch (DaoException e) {
            // 🔥 PRINT REAL ERROR so we know EXACT database failure
            e.printStackTrace();

            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to create cookout."
            );
        }
    }

    // -------------------------------------------------------------
    // GET ATTENDEES FOR A COOKOUT
    // -------------------------------------------------------------
    @GetMapping("/{cookoutId}/attendees")
    public List<User> getAttendees(@PathVariable int cookoutId) {
        return cookoutDao.getAttendees(cookoutId);
    }

    // -------------------------------------------------------------
    // ADD ATTENDEE
    // -------------------------------------------------------------
    @PostMapping("/{cookoutId}/attendees")
    @ResponseStatus(HttpStatus.CREATED)
    public void addAttendee(@PathVariable int cookoutId, Principal principal) {

        String username = principal.getName();
        User user = userDao.getUserByUsername(username);

        boolean added = cookoutDao.addAttendee(cookoutId, user.getId());

         if (!added) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User already attending or invalid cookout."
            );
        }
    }

    @GetMapping("/attending")
    public List<Cookout> getAttendingCookouts(Principal principal) {
        String username = principal.getName();
        User user = userDao.getUserByUsername(username);
        return cookoutDao.getCookoutsByAttendeeId(user.getId());
    }

    @GetMapping("/hosted")
    public List<Cookout> getHostedCookouts(Principal principal) {
        String username = principal.getName();
        User user = userDao.getUserByUsername(username);
        return cookoutDao.getCookoutsByHostId(user.getId());
    }

    @GetMapping("/{id}")
    public Cookout getCookout(@PathVariable int id) {
        return cookoutDao.getCookoutById(id);
    }
}
