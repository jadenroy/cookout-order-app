package com.techelevator.dao;

import com.techelevator.model.Cookout;
import com.techelevator.model.User;

import java.util.List;

public interface CookoutDao {

    Cookout getCookoutById(int id);

    List<Cookout> getCookouts();

    Cookout createCookout(Cookout cookout);

    List<User> getAttendees(int cookoutId);

    boolean addAttendee(int cookoutId, int attendeeId);
    List<Cookout> getCookoutsByHostId(int userId);
    List<Cookout> getCookoutsByAttendeeId(int userId);
}
