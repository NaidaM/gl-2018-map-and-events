package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.models.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


public class UserDaoFakeImpl extends Crud<User> implements UserDaoInterface {

    public UserDaoFakeImpl() {

    }

    @Valid
    public User get(String pseudo) throws DaoException.NotFoundException{

        User u = new User(pseudo,pseudo+"@example.com",null,null);

        if (u == null){
            throw new DaoException.NotFoundException("User not Found");
        }
        return u;
    }

    public Boolean delete(String pseudo) {
        return true;
    }

    public Boolean authenticate(String pseudo, String password) throws DaoException.NotFoundException {
        if((pseudo.equals("yannis") && password.equals("password"))
                || (pseudo.equals("eric") && password.equals("password"))
                || (pseudo.equals("naida") && password.equals("password"))
                || (pseudo.equals("naida") && password.equals("password"))
                || (pseudo.equals("imed") && password.equals("password") )){
            return true;
        }else {
            throw new DaoException.NotFoundException("Authentification failed");
        }
    }

    public  HashMap<String,String> getToken(String pseudo) {
        HashMap<String,String> token = new HashMap<String, String>();
        token.put("token","Fksds4dekkdjjjj654565");
        token.put("expire_at","2018-11-03 10:50");
        return token;
    }
}
