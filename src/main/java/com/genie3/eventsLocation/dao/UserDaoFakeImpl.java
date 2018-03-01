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

    public Boolean authenticate(String pseudo, String password) {
        if((pseudo.equals("yannis") && password.equals("password"))
                || (pseudo.equals("eric") && password.equals("password"))
                || (pseudo.equals("naida") && password.equals("password"))
                || (pseudo.equals("naida") && password.equals("password"))
                || (pseudo.equals("imed") && password.equals("password") )){
            return true;
        }else {
            return false;
        }
    }

    public String getToken(String pseudo) {
        return "Fksds4dekkdjjjj654565";
    }
}
