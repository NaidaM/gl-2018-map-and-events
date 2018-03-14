package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.exception.DaoException.DaoInternalError;
import com.genie3.eventsLocation.models.EventMap;

import java.util.List;


public interface MapDaoInterface extends CrudInterface<EventMap>{
    
    List<EventMap> readUserMap(String id) throws DaoInternalError;
    List<EventMap> getPublicMap() throws DaoInternalError;

    
}
