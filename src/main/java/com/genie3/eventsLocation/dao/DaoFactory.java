package com.genie3.eventsLocation.dao;

public final class DaoFactory {

    public static UserDao getUserDao(){
        return new UserDaoImpl();
    }
    public static MapDao getMapDao(){
        return new MapDaoImpl();
    }
    public static PlaceDao getPlaceDao(){
        return new PlaceDaoImpl();
    }
    public static ImageDao getImageDAO(){
        return new ImageDaoImpl();
    }
}