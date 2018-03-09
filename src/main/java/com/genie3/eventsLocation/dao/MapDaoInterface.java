package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.exception.DaoException.DaoInternalError;
import com.genie3.eventsLocation.models.EventMap;

import java.util.List;


public interface MapDaoInterface extends CrudInterface<EventMap>{

    List<EventMap> readForUser(String pseudo ,String[] fields, String whereClause,String orderBy,Integer limit);
    
    List<EventMap> readUserMap(String id) throws DaoInternalError;
    
}
