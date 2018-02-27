package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.models.Place;

public final class Dao {

    public static UserDaoInterface getUserDao(){
        return new UserDaoFakeImpl();
    }
    public static MapDaoInterface getMapDao(){
        return new MapDaoFakeImpl();
    }
    public static PlaceDaoInterface getPlaceDao(){
        return new PlaceDaoFakeImpl();
    }
}

