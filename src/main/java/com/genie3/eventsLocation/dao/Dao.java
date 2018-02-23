package com.genie3.eventsLocation.dao;

public final class Dao {

    public static UserDaoInterface getUserDao(){
        return new UserDaoFakeImpl();
    }
    public static MapDaoInterface getMapDao(){
        return new MapDaoFakeImpl();
    }
}

