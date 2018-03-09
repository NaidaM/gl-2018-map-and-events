package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.exception.DaoException.DaoInternalError;
import com.genie3.eventsLocation.models.Place;

import java.util.ArrayList;


public interface PlaceDaoInterface extends  CrudInterface<Place> {
    ArrayList<Place> getPlaceForMap(String mapId) throws DaoInternalError;
}
