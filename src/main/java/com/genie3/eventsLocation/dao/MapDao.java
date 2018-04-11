package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.exception.DaoException.DaoInternalError;
import com.genie3.eventsLocation.entities.EventMap;

import java.util.List;


public interface MapDao extends Crud<EventMap> {
    
    List<EventMap> readUserMap(String id) throws DaoInternalError,DaoException.NotFoundException;
    List<EventMap> getFriendMap(String pseudo) throws DaoInternalError,DaoException.NotFoundException;
    List<EventMap> getPublicMap() throws DaoInternalError,DaoException.NotFoundException;
}
