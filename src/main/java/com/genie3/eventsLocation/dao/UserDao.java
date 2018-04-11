package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.entities.User;

public interface UserDao extends Crud<User> {

    User getWithPseudo(String pseudo) throws DaoException.NotFoundException;
    void updatePassord(User user,String password);
    Boolean authenticate(String pseudo,String password) throws DaoException.NotFoundException;

}
