package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.elastic.DB;
import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.models.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.validation.Valid;


public class UserDaoFakeImpl extends Crud<User> implements UserDaoInterface {

    public UserDaoFakeImpl() {

    }


    @Valid
    public User getWithPseudo(String pseudo) throws DaoException.NotFoundException{

        try {
            return  DB.getUserWithPseudo(pseudo);
        }catch (Exception ex){
           throw new DaoException.NotFoundException(ex.getMessage());
        }

    }


    private static boolean checkPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }


    public Boolean authenticate(String password, String passwordHashed) throws DaoException.NotFoundException {
    	
        if(checkPassword(password,passwordHashed)) {
        	return true;
        }
        else {
        	throw new DaoException.NotFoundException("login or password incorect");
        }
    }

    @Override
    public void updatePassord(User user, String password) {
        DB.updateUserPassword(user,password);
    }
}
