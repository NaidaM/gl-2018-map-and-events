package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.models.User;


public class UserDaoFakeImpl extends Crud<User> implements UserDaoInterface {

    public UserDaoFakeImpl() {

    }

    public User get(String pseudo) {
        return null;
    }

    public Boolean delete(String pseudo) {
        return true;
    }
}
