package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.models.User;

import java.util.List;
import java.util.Map;

public interface CrudInterface<T> {

    T create(T t,String table) throws DaoException.DaoInternalError;
    List<T> read(String[] fields, String whereClause,String orderBy,Integer limit);
    T get(String id,String table)  throws DaoException.NotFoundException;
    T update(T t,String table)  throws DaoException.DaoInternalError ;
    Boolean delete(String id,String table)  throws DaoException.DaoInternalError;
	void updatePassord(User user, String password);
}
