package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.elastic.Database;
import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.models.User;
import org.mindrot.jbcrypt.BCrypt;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class UserDaoFakeImpl extends Crud<User> implements UserDaoInterface {

    public UserDaoFakeImpl() {

    }


    @Override
    public User create(User user)  throws DaoException.DaoInternalError {

        try {
            Database.createUser(user);
            return user;
        }catch (IOException ex){
            throw new DaoException.DaoInternalError("Something goes wrong");
        }

    }

    @Override
    public User get(int id) throws DaoException.NotFoundException{

        User u = new User("pseudo","example@example.com",null,null);
        // just for the test
        u.setRole("user");
        u.setId("1");
        if (u == null){
            throw new DaoException.NotFoundException("User not Found");
        }
        return u;
    }

    @Valid
    public User getWithId(String id) throws DaoException.NotFoundException{

       /* User u = new User(pseudo,pseudo+"@example.com",null,null);
        //Just for the test
        u.setRole("user");
        u.setId(1);
        if (u == null){
            throw new DaoException.NotFoundException("User not Found");
        }
        return u;*/
        try {
            return  Database.getUserWithId(id);
        }catch (IOException ex){
            return null;
        }

    }

    @Valid
    public User getWithPseudo(String pseudo) throws DaoException.NotFoundException{

       /* User u = new User(pseudo,pseudo+"@example.com",null,null);
        //Just for the test
        u.setRole("user");
        u.setId(1);
        if (u == null){
            throw new DaoException.NotFoundException("User not Found");
        }
        return u;*/
        try {
            return  Database.getUserWithPseudo(pseudo);
        }catch (IOException ex){
           return null;
        }

    }

    @Override
    public Boolean delete(String id) throws DaoException.DaoInternalError{

        try {

            return Database.deleteUser(id);
        }catch (DaoException.DaoInternalError ex){
            throw new DaoException.DaoInternalError(ex.getMessage());
        }
        catch (IOException ex){

            ex.fillInStackTrace();
            return false;
        }

    }

    private static boolean checkpassd(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }


    public Boolean authenticate(String password, String passwordHashed) throws DaoException.NotFoundException {

        return checkpassd(password,passwordHashed);


        /*if((pseudo.equals("yannis") && password.equals("password"))
                || (pseudo.equals("eric") && password.equals("password"))
                || (pseudo.equals("naida") && password.equals("password"))
                || (pseudo.equals("naida") && password.equals("password"))
                || (pseudo.equals("imed") && password.equals("password") )){
            return true;
        }else {
            throw new DaoException.NotFoundException("Authentification failed");
        }*/


    }

    public  HashMap<String,String> getToken(String pseudo) {
        HashMap<String,String> token = new HashMap<String, String>();
        token.put("token","Fksds4dekkdjjjj654565");
        token.put("expire_at","2018-11-03 10:50");
        return token;
    }
}
