package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.models.User;

public interface UserDaoInterface extends CrudInterface<User>{

    User get(String pseudo) throws DaoException.NotFoundException;
    Boolean delete(String pseudo);
    Boolean authenticate(String pseudo,String password) throws DaoException.NotFoundException;
    String getToken(String pseudo);

}
