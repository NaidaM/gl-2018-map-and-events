package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.models.EventMap;
import com.genie3.eventsLocation.models.User;

import java.util.List;

public class UserDaoFakeImpl  implements UserDaoInterface{

    public User createUser(User user) {
        return user;
    }

    public  User getUser(String pseudo){
        return new User(pseudo,"john.doe@example.com","",null);
    }
    public User updateUser(User user){
        return user;
    }
    public  boolean deleteUser(String pseudo){
        return true;
    }
    public List<EventMap> getMaps(String pseudo){
        return null;
    }
}
