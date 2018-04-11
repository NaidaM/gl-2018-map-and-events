package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.elastic.DB;
import com.genie3.eventsLocation.exception.DaoException.DaoInternalError;
import com.genie3.eventsLocation.entities.Place;

import java.util.List;


public class PlaceDaoImpl extends CrudImpl<Place> implements PlaceDao {

	public List<Place> getPlaceForMap(String mapId) throws DaoInternalError {

		return DB.getPlaces(mapId);
	}

	public List<String> getPhoto(String placeId) throws DaoInternalError {
		
		return DB.getPhoto(placeId);
	}

}

