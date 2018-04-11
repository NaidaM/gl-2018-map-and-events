package com.genie3.eventsLocation.dao;


import com.genie3.eventsLocation.elastic.DB;
import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.exception.DaoException.DaoInternalError;
import com.genie3.eventsLocation.entities.EventMap;

import java.util.List;

public class MapDaoImpl extends CrudImpl<EventMap> implements MapDao {


    @Override
    public EventMap get(String id,String table) throws DaoException.NotFoundException {
        EventMap map = DB.get(id,"map");
        map.setId(id);
        return map;
    }



	public List<EventMap> readUserMap(String id) throws DaoInternalError,DaoException.NotFoundException {

		try{
			return DB.getUserMap(id);
		}catch (DaoException.NotFoundException ex){
			throw new DaoException.NotFoundException(ex.getMessage());
		}
		catch (Exception e) {

			throw new DaoException.DaoInternalError(e.getMessage());
		}
	}


	public List<EventMap>  getFriendMap(String pseudo)  throws DaoInternalError {
		try{
			return DB.getFriendMap(pseudo);
		}catch (Exception e) {
			throw new DaoException.DaoInternalError(e.getMessage());
		}
	}
		
	
	public List<EventMap> getPublicMap() throws DaoInternalError {

		try{
			return DB.getPublicMap();
		}catch (Exception e) {
			throw new DaoException.DaoInternalError(e.getMessage());
		}
	}




}

