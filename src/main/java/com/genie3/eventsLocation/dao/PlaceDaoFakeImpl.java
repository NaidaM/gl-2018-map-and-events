package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.elastic.DB;
import com.genie3.eventsLocation.exception.DaoException.DaoInternalError;
import com.genie3.eventsLocation.models.Place;


import java.util.ArrayList;


public class PlaceDaoFakeImpl extends Crud<Place> implements PlaceDaoInterface{



	public ArrayList<Place> getPlaceForMap(String mapId) throws DaoInternalError {

		return DB.getPlaces(mapId);
	}

}

