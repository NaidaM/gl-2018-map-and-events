package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.models.User;

public interface UserDaoInterface extends CrudInterface<User>{

    User get(String pseudo);
    Boolean delete(String pseudo);
    Boolean authenticate(String pseudo,String password);
    String getToken(String pseudo);

}
