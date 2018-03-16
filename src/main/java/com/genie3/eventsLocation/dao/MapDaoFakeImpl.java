package com.genie3.eventsLocation.dao;


import java.util.ArrayList;
import java.util.List;

import com.genie3.eventsLocation.elastic.DB;
import com.genie3.eventsLocation.exception.DaoException;
import com.genie3.eventsLocation.exception.DaoException.DaoInternalError;
import com.genie3.eventsLocation.models.EventMap;

public class MapDaoFakeImpl extends Crud<EventMap> implements MapDaoInterface{


    @Override
    public EventMap get(String id,String table) throws DaoException.NotFoundException {
        EventMap map = DB.get(id,"map");
        map.setId(id);
        return map;
    }

    @Override
    public List<EventMap> read(String[] fields, String whereClause, String orderBy, Integer limit) {
        /* l'idée ici c'est de récupérer les données avec une requete generique
            dans Crud et adapter le retour en fonction de la resource


        */
        String [] users = { "yannis","naida","imed","eric","zak"};
        ArrayList<EventMap> maps = new ArrayList<EventMap>();
       /* for (int i = 0; i<users.length;i++){
            EventMap map = new EventMap("Title"+i,"Lorem ipsum dolor sit amet, " +
                    "consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore " +
                    "et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                    "ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure " +
                    "dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat");
            try {
               User u = Dao.getUserDao().getWithPseudo(users[i]);
               u.setId(""+(i+1));
                map.setUser(u);
                map.setId(""+(i+1));
                maps.add(map);
            }catch (DaoException.NotFoundException ex){

            }



        }*/
        //return super.read(fields, whereClause, orderBy, limit);
        return maps;
    }


/*    public List<EventMap> readForUser(String pseudo,String[] fields, String whereClause, String orderBy, Integer limit) {

        ArrayList<EventMap> maps = new ArrayList<EventMap>();
        for (int i = 0; i<5;i++){

            EventMap map = new EventMap("Title"+i,"Lorem ipsum dolor sit amet, " +
                    "consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore " +
                    "et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                    "ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure " +
                    "dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat");
            map.setId(""+i+1);
            map.setPlaces(null);
            maps.add(map);
        }
        //return super.read(fields, whereClause, orderBy, limit);
        return maps;
    }
    */

	public List<EventMap> readUserMap(String id) throws DaoInternalError {

		try{
			return DB.getUserMap(id);
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

