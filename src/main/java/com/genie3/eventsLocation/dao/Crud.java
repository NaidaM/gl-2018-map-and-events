package com.genie3.eventsLocation.dao;

import com.genie3.eventsLocation.models.EventMap;
import com.genie3.eventsLocation.models.User;

import java.util.List;

public class Crud <T> implements CrudInterface<T> {



    public T create(T t) {
        return t;
    }

    public List<T> read(String[] fields, String whereClause,String orderBy,Integer limit) {
        return null;
    }

    public T get(int id) {
        return null;
    }

    public T update(T t) {
        return t;
    }

    public Boolean delete(int t) {
        //Make some action
        return true;
    }


}
