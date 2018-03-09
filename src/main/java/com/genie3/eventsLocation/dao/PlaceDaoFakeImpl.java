package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.models.Place;


import java.util.ArrayList;


public class PlaceDaoFakeImpl extends Crud<Place> implements PlaceDaoInterface{



    public ArrayList<Place> getPlaceForMap(String mapId) {
        ArrayList<Place> places = new ArrayList<Place>();
        for (int i = 0; i<5;i++){
            Place p = new Place("Place "+i,"Lorem ipsum dolor sit amet, " +
                    "consectetur adipiscing elit, sed do eiusmod tempor","20","20");
                p.setId(i+1);
                places.add(p);



        }
        //return super.read(fields, whereClause, orderBy, limit);
        return places;
    }
}
