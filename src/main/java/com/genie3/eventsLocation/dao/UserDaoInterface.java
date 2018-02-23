package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.models.User;
import com.genie3.eventsLocation.models.EventMap;

import java.util.List;

public interface UserDaoInterface {
    User createUser(User user);
    User getUser(String pseudo);
    User updateUser(User user);
    boolean deleteUser(String pseudo);
    List<EventMap> getMaps(String pseudo);
}
