package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.models.User;


public class UserDaoFakeImpl extends Crud<User> implements UserDaoInterface {

    public UserDaoFakeImpl() {

    }

    public User get(String pseudo) {

        return new User(pseudo,pseudo+"@example.com",null,null);
    }

    public Boolean delete(String pseudo) {
        return true;
    }
}
