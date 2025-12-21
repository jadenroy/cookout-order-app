package com.techelevator.controller;

import com.techelevator.dao.CookoutDao;
import com.techelevator.dao.InvitationDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Invitation;
import com.techelevator.model.User;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
@RequestMapping("/invitations")
public class InvitationController {

    private final InvitationDao invitationDao;
    private final UserDao userDao;
    private final CookoutDao cookoutDao;

    public InvitationController(
            InvitationDao invitationDao,
            UserDao userDao,
            CookoutDao cookoutDao
    ) {
        this.invitationDao = invitationDao;
        this.userDao = userDao;
        this.cookoutDao = cookoutDao;
    }

    @GetMapping
    public List<Invitation> getInvitationsByToId(Principal principal) {
        User user = userDao.getUserByUsername(principal.getName());

        return invitationDao.getInvitationsByToId(user.getId())
                .stream()
                .filter(Invitation::isActive)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void createInvitation(
            @RequestBody Invitation invitation,
            Principal principal
    ) {
        User sender = userDao.getUserByUsername(principal.getName());
        invitation.setFromId(sender.getId());

        // 🔒 BLOCK inviting users already attending
        List<User> attendees = cookoutDao.getAttendees(invitation.getCookoutId());
        boolean alreadyAttending = attendees.stream()
                .anyMatch(u -> u.getId() == invitation.getToId());

        if (alreadyAttending) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "User already attending cookout"
            );
        }

        invitationDao.createInvitation(invitation);
    }

    @PostMapping("/{invitationId}/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptInvitation(
            @PathVariable int invitationId,
            Principal principal
    ) {
        User user = userDao.getUserByUsername(principal.getName());

        Invitation invitation = invitationDao.getInvitationById(invitationId);
        if (invitation == null ||
                invitation.getToId() != user.getId() ||
                !invitation.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid invitation"
            );
        }

        // Accept invitation
        invitationDao.acceptInvitation(invitationId, user.getId());

        // ✅ SAFE: only add if not already attending
        List<User> attendees = cookoutDao.getAttendees(invitation.getCookoutId());
        boolean alreadyAttending = attendees.stream()
                .anyMatch(u -> u.getId() == user.getId());

        if (!alreadyAttending) {
            cookoutDao.addAttendee(invitation.getCookoutId(), user.getId());
        }
    }

    @PostMapping("/{invitationId}/decline")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void declineInvitation(
            @PathVariable int invitationId,
            Principal principal
    ) {
        User user = userDao.getUserByUsername(principal.getName());

        Invitation invitation = invitationDao.getInvitationById(invitationId);
        if (invitation == null ||
                invitation.getToId() != user.getId() ||
                !invitation.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid invitation"
            );
        }

        invitationDao.declineInvitation(invitationId, user.getId());
    }
}
