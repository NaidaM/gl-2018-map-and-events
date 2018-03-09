package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.elastic.DB;
import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.models.EventMap;
import com.genie3.eventsLocation.models.User;

import java.util.List;

public class Crud <T> implements CrudInterface<T> {



    public T create(T t,String table) throws DaoException.DaoInternalError {

            return DB.create(t,table);

    }

    public List<T> read(String[] fields, String whereClause,String orderBy,Integer limit)
    {
        return null;
    }

    public T get(String id, String table) throws DaoException.NotFoundException {
        return DB.get(id,table);
    }

    public T update(T t,String table) throws DaoException.DaoInternalError {
        return DB.update(t,table);
    }

    public Boolean delete(String id,String table)  throws DaoException.DaoInternalError {
        try {

            return DB.delete(id,table);
        }catch (DaoException.DaoInternalError ex){
            throw new DaoException.DaoInternalError(ex.getMessage());
        }
    }
}
