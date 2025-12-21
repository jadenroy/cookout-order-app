package com.techelevator.dao;

import com.techelevator.model.Invitation;

import java.util.List;

public interface InvitationDao {
    public Invitation getInvitationById(int id);
    public List<Invitation> getInvitationsByFromId(int fromId);
    public List<Invitation> getInvitationsByToId(int toId);
    public List<Invitation> getInvitationsByUserId(int userId);
    public List<Invitation> getInvitationsByCookoutId(int cookoutId);
    public Invitation createInvitation(Invitation invitation);

    boolean declineInvitation(int invitationId, int id);

    boolean acceptInvitation(int invitationId, int id);
}
