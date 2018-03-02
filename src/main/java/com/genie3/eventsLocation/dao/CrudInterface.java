package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.exception.DaoException;

import java.util.List;
import java.util.Map;

public interface CrudInterface<T> {

    T create(T t) throws DaoException.DaoInternalError;
    List<T> read(String[] fields, String whereClause,String orderBy,Integer limit);
    T get(int id)  throws DaoException.NotFoundException;
    T update(T t);
    Boolean delete(int id);
}
