package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.exception.DaoException.DaoInternalError;
import com.genie3.eventsLocation.entities.Place;

import java.util.List;


public interface PlaceDao extends Crud<Place> {
    List<Place> getPlaceForMap(String mapId) throws DaoInternalError;
    List<String> getPhoto(String placeId) throws DaoInternalError;
}
