package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.models.EventMap;
import com.genie3.eventsLocation.models.User;

import java.util.List;

public class Crud <T> implements CrudInterface<T> {



    public T create(T t) throws DaoException.DaoInternalError {
        return t;
    }

    public List<T> read(String[] fields, String whereClause,String orderBy,Integer limit)
    {
        return null;
    }

    public T get(int id) throws DaoException.NotFoundException {
        return null;
    }

    public T update(T t) throws DaoException.DaoInternalError {
        return t;
    }

    public Boolean delete(String id)  throws DaoException.DaoInternalError {
        //Make some action
        // If ok return true else throw Internal exception
        return true;
    }


}
