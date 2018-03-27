package com.genie3.eventsLocation.dao;

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
     public static ImageInterface getImageDAO(){
        return new ImageImpl();
    }
}