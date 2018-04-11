package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.exception.DaoException;
import java.util.List;


public interface Crud<T> {

    T create(T t,String table) throws DaoException.DaoInternalError;
    List<T> read(String[] fields, String whereClause,String orderBy,Integer limit);
    T get(String id,String table)  throws DaoException.NotFoundException;
    T update(T t,String table)  throws DaoException.DaoInternalError ;
    Boolean delete(String id,String table)  throws DaoException.DaoInternalError;
}
